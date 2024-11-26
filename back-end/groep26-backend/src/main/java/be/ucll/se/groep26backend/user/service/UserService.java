package be.ucll.se.groep26backend.user.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import be.ucll.se.groep26backend.notification.model.Notification;
import be.ucll.se.groep26backend.rents.model.Rent;
import be.ucll.se.groep26backend.car.model.Car;
import be.ucll.se.groep26backend.car.repo.CarRepository;
import be.ucll.se.groep26backend.email.service.EmailService;
import be.ucll.se.groep26backend.rental.model.Rental;
import be.ucll.se.groep26backend.rental.repo.RentalRepository;
import be.ucll.se.groep26backend.rents.model.Rent;
import be.ucll.se.groep26backend.rents.repo.RentRepository;
import be.ucll.se.groep26backend.rents.service.RentService;
import be.ucll.se.groep26backend.rents.service.RentServiceException;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;
import be.ucll.se.groep26backend.wallet.model.Wallet;
import be.ucll.se.groep26backend.wallet.service.WalletService;
import be.ucll.se.groep26backend.wallet.service.WalletServiceException;
import jakarta.validation.constraints.Email;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final WalletService walletService;
    private final CarRepository carRepository;
    // private final PasswordEncoder passwordEncoder;
    private final RentRepository rentRepository;
    private final RentalRepository rentalRepository;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, WalletService walletService,
                       CarRepository carRepository,
                       RentRepository rentRepository, RentalRepository rentalRepository,EmailService emailService) {
        this.userRepository = userRepository;
        this.walletService = walletService;
        this.carRepository = carRepository;
        // this.passwordEncoder = passwordEncoder;
        this.rentRepository = rentRepository;
        this.rentalRepository = rentalRepository;
        this.emailService = emailService;
    }
    
    public User createUser(User newUser) throws UserServiceException{
        if(userRepository.findUserByUsername(newUser.getUsername()) != null) {
            throw new UserServiceException("Username already exists", "username");
        }
        if(userRepository.findUserByEmail(newUser.getEmail()) != null) {
            throw new UserServiceException("Email already exists", "email");
        }
        
        userRepository.save(newUser);
        Wallet wallet = walletService.createWallet();
        newUser.setWallet(wallet);
        userRepository.save(newUser);

        return newUser;
    }

    public User getUserByUsername(String username) throws UserServiceException{
        if(userRepository.findUserByUsername(username) == null) {
            throw new UserServiceException("User not found", "username");
        }
        return userRepository.findUserByUsername(username);
    }

    public User getUserByEmail(String email) throws UserServiceException{
        if(userRepository.findUserByEmail(email) == null) {
            throw new UserServiceException("User not found", "email");
        }
        return userRepository.findUserByEmail(email);
    }

    public User getUserById(long id) {
        return userRepository.findUserById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Notification> getUsersNotifications(String email) throws UserServiceException{
        if(userRepository.findUserByEmail(email) == null) {
            throw new UserServiceException("User not found", "email");
        }
        User user = userRepository.findUserByEmail(email);

        return user.getNotifications();
    }

    public List<Rent> getUsersRents(String email) throws UserServiceException{
        if(userRepository.findUserByEmail(email) == null) {
            throw new UserServiceException("User not found", "email");
        }
        User user = userRepository.findUserByEmail(email);

        return user.getRents();
    }

    public List<Notification> clearUserNotificationsList (String email) throws UserServiceException{
        if(userRepository.findUserByEmail(email) == null) {
            throw new UserServiceException("User not found", "email");
        }
        User user = userRepository.findUserByEmail(email);
        user.clearNotifications();
        userRepository.save(user);
        System.out.println(user.getNotifications());
        return user.getNotifications();
    }

    
    // @PreAuthorize("hasRole('OWNER')")
    public Car addCarToUser(long userId, Car newCar) {
        User user = userRepository.findUserById(userId);
        user.addCar(newCar);
        userRepository.save(user);
        newCar.setUser(user);
        carRepository.save(newCar);
        return newCar;
    }

    public Boolean checkForBalance (Rental rental, User user) {
        if (user.getWallet().getBalance() < rental.getPrice()) {
            return false;
        }
        return true;
    }

    public Rent addRentToUser (long userId, Rent newRent, long rentalId) throws RentServiceException, WalletServiceException {
        User user = userRepository.findById(userId).get();
        Rental rental = rentalRepository.findById(rentalId).get();
        if (!checkForBalance(rental, user)) {
            throw new RentServiceException("Not enough balance", "balance");
        }
        walletService.subtractBalance(user.getWallet().id, rental.getPrice());
        walletService.addBalance(rental.getCar().getUser().id, rental.getPrice());

        newRent.setUser(user);
        rentRepository.save(newRent);
        newRent.setRental(rental);
        rentRepository.save(newRent);
        rental.setRent(newRent);
        rentRepository.save(newRent);
        rentalRepository.save(rental);
        user.setRent(newRent);
        userRepository.save(user);

        return newRent;
    }

    public Wallet getWalletByUsername(String username) throws UserServiceException{
        if(userRepository.findUserByUsername(username) == null) {
            throw new UserServiceException("User not found", "userId");
        }
        User user = userRepository.findUserByUsername(username);
        return user.getWallet();
    }

    public int sendVerificationCode(String email) throws UserServiceException {
        if(userRepository.findUserByEmail(email) == null) {
            throw new UserServiceException("User not found", "email");
        }
        User user = userRepository.findUserByEmail(email);
        double randomCode = Math.floor(100000 + Math.random() * 900000);
        int roundedCode = (int) Math.round(randomCode);
        emailService.sendEmail(email, "Verification code", "Your verification code is: " + roundedCode);
        user.setGivenVerificationCode(roundedCode);
        userRepository.save(user);
        return roundedCode;
    }

    public User verifyCode(String email, int code) throws UserServiceException {
        if(userRepository.findUserByEmail(email) == null) {
            throw new UserServiceException("User not found", "email");
        }
        User user = userRepository.findUserByEmail(email);
        if(user.getGivenVerificationCode() != code) {
            throw new UserServiceException("Invalid code", "code");
        }
        
        LocalDate expiryDate = LocalDate.now().plusDays(30);
        
        String message = "Your account has been verified!";
        emailService.sendEmail(email, "Account verified", message);
        user.setVerified(true);
        user.setExperationDate(expiryDate);
        userRepository.save(user);
        return user;
    }

    public Boolean checkIfUserIsVerified(String email) throws UserServiceException {
        if(userRepository.findUserByEmail(email) == null) {
            throw new UserServiceException("User not found", "email");
        }
        
        User user = userRepository.findUserByEmail(email);

        if (user.getExperationDate() != null && user.getExperationDate().isBefore(LocalDate.now())) {
            user.setVerified(false);
            userRepository.save(user);
        }

        return user.isVerified();
    }

    public User deleteUser(String email) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("User not found", "email");
        }
        
        try {
            // Send email notification
            emailService.sendEmail(email, "Account deleted", "Your account has been deleted");
            // user.setWallet(null);
            // // Delete associated wallet
            // walletService.deleteWallet(user.getWallet().getId());
            // Delete user
            userRepository.delete(user);
        } catch (Exception e) {
            throw new UserServiceException("Error occurred while deleting user", "email");
        }
        
        return user;
    }

    public User banUser(long id) {
        User user = userRepository.findUserById(id);
        user.setBanned(true);
        userRepository.save(user);
        return user;
    }

    public User unbanUser(long id) {
        User user = userRepository.findUserById(id);
        user.setBanned(false);
        userRepository.save(user);
        return user;
    }

    public boolean checkIfUserIsBanned(String email) throws UserServiceException {
        if(userRepository.findUserByEmail(email) == null) {
            throw new UserServiceException("User not found", "email");
        }
        User user = userRepository.findUserByEmail(email);
        return user.isBanned();
    }


    public List<Car> getUsersCars(long id) throws UserServiceException{
        if(userRepository.findUserById(id) == null) {
            throw new UserServiceException("User not found", "id");
        }
        User user = userRepository.findUserById(id);
        return user.getCars();
    }
}


