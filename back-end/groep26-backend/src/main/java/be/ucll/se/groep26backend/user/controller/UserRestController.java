package be.ucll.se.groep26backend.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep26backend.notification.model.Notification;
import be.ucll.se.groep26backend.rents.model.Rent;
import be.ucll.se.groep26backend.car.model.Car;
import be.ucll.se.groep26backend.car.service.CarServiceException;
import be.ucll.se.groep26backend.rental.service.RentalServiceException;
import be.ucll.se.groep26backend.rents.service.RentServiceException;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.service.UserService;
import be.ucll.se.groep26backend.user.service.UserServiceException;
import be.ucll.se.groep26backend.wallet.model.Wallet;
import be.ucll.se.groep26backend.wallet.service.WalletServiceException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


// @CrossOrigin(origins = "*")
@CrossOrigin(origins = {"http://localhost:3000", "https://group26-frontend.azurewebsites.net", "https://group26-ac-frontend.azurewebsites.net", "https://groep26-frontend.azurewebsites.net"})
@RestController
@RequestMapping("/users")
public class UserRestController {
    
    @Autowired
    UserService userService;

    @ResponseStatus(HttpStatus.BAD_REQUEST )
    @ExceptionHandler({
    MethodArgumentNotValidException.class})
    public Map<String, String>
    handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST )
    @ExceptionHandler({ UserServiceException.class})
    public Map<String, String>
    handleServiceExceptions(UserServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }

    @PostMapping("/register")
    public User createUser(@Valid @RequestBody User newUser) throws UserServiceException {
        return userService.createUser(newUser);
    }   

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())")
    @GetMapping("/username/{username}") 
    public User getUserByUsername(@PathVariable String username) throws UserServiceException{
        return userService.getUserByUsername(username);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())")
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) throws UserServiceException{
        return userService.getUserByEmail(email);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/usersNotfications/{email}")
    public List<Notification> getUsersNotifications(@PathVariable String email) throws UserServiceException{
        return userService.getUsersNotifications(email);
    }

    @GetMapping("/usersRents/{email}")
    public List<Rent> getUsersRents(@PathVariable String email) throws UserServiceException{
        return userService.getUsersRents(email);
    }

    @PutMapping("/clearNotifications/{email}")
    public List<Notification> clearNotifications(@PathVariable String email) throws UserServiceException{
        return userService.clearUserNotificationsList(email);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())")
    @PostMapping("{userId}/addCar")
    public Car addCar(@Valid @RequestBody Car newCar, @PathVariable long userId) throws CarServiceException, UserServiceException{
        return userService.addCarToUser(userId, newCar);
    }

    @PostMapping("{userId}/addRent/{rentalId}")
    public Rent addRent(@Valid @RequestBody Rent newRent, @PathVariable long userId, @PathVariable long rentalId ) throws RentalServiceException, UserServiceException, RentServiceException, WalletServiceException{
        return userService.addRentToUser(userId, newRent, rentalId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/balance/{username}")
    public Wallet getWalletOfUser(@PathVariable String username) throws UserServiceException {
        return userService.getWalletByUsername(username);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ACCOUNTANT.getRoleName())") 
    @DeleteMapping("/delete/{email}")
    public void deleteUser(@PathVariable String email) throws UserServiceException {
        userService.deleteUser(email);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())")
    @PutMapping("/ban/{id}")
    public User banUser(@PathVariable long id) throws UserServiceException {
        return userService.banUser(id);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())")
    @PutMapping("/unban/{id}")
    public User unbanUser(@PathVariable long id) throws UserServiceException {
        return userService.unbanUser(id);
    }

    @GetMapping("/checkUserBanned/{email}")
    public boolean checkUserBanned(@PathVariable String email) throws UserServiceException {
        return userService.checkIfUserIsBanned(email);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())")
    @GetMapping("/getUsersCars/{id}")
    public List<Car> getUsersCars(@PathVariable long id) throws UserServiceException {
        return userService.getUsersCars(id);
    }

}