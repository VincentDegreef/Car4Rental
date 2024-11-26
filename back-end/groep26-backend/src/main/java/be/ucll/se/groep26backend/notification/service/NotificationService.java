package be.ucll.se.groep26backend.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import be.ucll.se.groep26backend.complaints.model.Complaint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.se.groep26backend.email.service.EmailService;
import be.ucll.se.groep26backend.notification.model.Notification;
import be.ucll.se.groep26backend.notification.repo.NotificationRepository;
import be.ucll.se.groep26backend.rents.model.Rent;
import be.ucll.se.groep26backend.rents.repo.RentRepository;
import be.ucll.se.groep26backend.rents.service.RentServiceException;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;

@Service
public class NotificationService {
    

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public NotificationService(){}

    public Notification addNotification(Notification newNotification){
        notificationRepository.save(newNotification);
        return newNotification;
    }

    public List<Notification> getAllNotifications(){
        return notificationRepository.findAll();
    }

    public Notification deleteNotification(Long id) throws NotificationServiceException{
        if(notificationRepository.findById(id) == null){
            throw new NotificationServiceException("Notification not found", "id");
        }
        Notification notification = notificationRepository.findById(id).get();
        notificationRepository.delete(notification);
        return notification;
    }


    public Notification makeBigNotification(String renterEmail, Long rentId) throws RentServiceException{
        if(rentRepository.findById(rentId) == null){
            throw new RentServiceException("Rent not found", "rentId");
        }
        Rent rent = rentRepository.findById(rentId).get();
        if(userRepository.findUserByEmail(renterEmail) == null){
            throw new RentServiceException("Renter not found", "renterEmail");
        }
        if(userRepository.findUserByEmail(rent.getRental().getEmail()) == null){
            throw new RentServiceException("Owner not found", "rent.getRental().getEmail()");
        }
        User renter = userRepository.findUserByEmail(renterEmail);
        User owner = userRepository.findUserByEmail(rent.getRental().getEmail());

        String message = "Rent for " + rent.getRental().getCar().getBrand() + " " + rent.getRental().getCar().getModel() + " " + rent.getRental().getCar().getLicensePlate() + " from " + rent.getRental().getStartDate() + " to " + rent.getRental().getEndDate() + " has been requested by " + renterEmail + " please confirm or deny this request."; 
        
        LocalDateTime receivedAt = LocalDateTime.now();
        System.out.println(receivedAt  );
        Notification notiRenter = new Notification(message, false, false, "big",receivedAt);
        Notification notiOwner = new Notification(message, false, false, "big", rentId, receivedAt);

        emailService.sendEmail(owner.getEmail(), "Rent request", message);


        renter.addNotification(notiRenter);
        owner.addNotification(notiOwner);
    
        userRepository.save(renter);
        userRepository.save(owner);
        return notificationRepository.save(notiOwner);
    }

    public Notification makeConfirmNotification(Long notificationId) throws RentServiceException, NotificationServiceException{
        if(notificationRepository.findById(notificationId) == null){
            throw new NotificationServiceException("Notification not found", "notificationId");
        }
        Notification notification = notificationRepository.findById(notificationId).get();
        notification.setSeen(true);
        Long rentId = notification.getRentId();
        if(rentRepository.findById(rentId) == null){
            throw new RentServiceException("Rent not found", "rentId");
        }
        

        Rent rent = rentRepository.findById(rentId).get();

        if(userRepository.findUserByEmail(rent.getRental().getEmail()) == null){
            throw new RentServiceException("Owner not found", "rent.getRental().getEmail()");
        }
        User owner = userRepository.findUserByEmail(rent.getRental().getEmail());
        if(userRepository.findUserByEmail(rent.getEmail()) == null){
            throw new RentServiceException("Renter not found", "rent.getEmail()");
        }
        User renter = userRepository.findUserByEmail(rent.getEmail());

        String message = "Rent for " + rent.getRental().getCar().getBrand() + " " + rent.getRental().getCar().getModel() + " " + rent.getRental().getCar().getLicensePlate() + " by " + rent.getEmail() + " from " + rent.getRental().getStartDate() + " until " + rent.getRental().getEndDate() + " confirmed by " + rent.getRental().getEmail();
        
        LocalDateTime receivedAt = LocalDateTime.now();
        Notification renterNoti = new Notification(message, true, false, "small", receivedAt);
        Notification ownerNoti = new Notification(message, true, false, "small", receivedAt);

        emailService.sendEmail(renter.getEmail(), "Rent confirmation ", message);

        renter.addNotification(renterNoti);
        owner.addNotification(ownerNoti);
    
        userRepository.save(renter);
        userRepository.save(owner);
        return notificationRepository.save(renterNoti);
    }

    public Notification makeCancelNotification(Long notificationId) throws RentServiceException, NotificationServiceException{
        if(notificationRepository.findById(notificationId) == null){
            throw new NotificationServiceException("Notification not found", "notificationId");
        }
        Notification notification = notificationRepository.findById(notificationId).get();
        notification.setSeen(true);
        Long rentId = notification.getRentId();
        if(rentRepository.findById(rentId) == null){
            throw new RentServiceException("Rent not found", "rentId");
        }
        

        Rent rent = rentRepository.findById(rentId).get();

        if(userRepository.findUserByEmail(rent.getRental().getEmail()) == null){
            throw new RentServiceException("Owner not found", "rent.getRental().getEmail()");
        }
        User owner = userRepository.findUserByEmail(rent.getRental().getEmail());
        if(userRepository.findUserByEmail(rent.getEmail()) == null){
            throw new RentServiceException("Renter not found", "rent.getEmail()");
        }
        User renter = userRepository.findUserByEmail(rent.getEmail());

        String message = "Rent for " + rent.getRental().getCar().getBrand() + " " + rent.getRental().getCar().getModel() + " " + rent.getRental().getCar().getLicensePlate() + " by " + rent.getEmail() + " from " + rent.getRental().getStartDate() + " until " + rent.getRental().getEndDate() + " Canceled by " + rent.getRental().getEmail();

        LocalDateTime receivedAt = LocalDateTime.now();
        
        Notification renterNoti = new Notification(message, false, false, "small", receivedAt);
        Notification ownerNoti = new Notification(message, false, false, "small", receivedAt);

        emailService.sendEmail(renter.getEmail(), "Rent cancellation ", message);

        renter.addNotification(renterNoti);
        owner.addNotification(ownerNoti);
    
        userRepository.save(renter);
        userRepository.save(owner);
        return notificationRepository.save(renterNoti);
    }

    public Notification cancelRentNotification(Long rentId, String renterEmail) throws RentServiceException{
        if(rentRepository.findById(rentId) == null){
            throw new RentServiceException("Rent not found", "rentId");
        }
        Rent rent = rentRepository.findById(rentId).get();
        if(userRepository.findUserByEmail(renterEmail) == null){
            throw new RentServiceException("Renter not found", "renterEmail");
        }
        if(userRepository.findUserByEmail(rent.getRental().getEmail()) == null){
            throw new RentServiceException("Owner not found", "rent.getRental().getEmail()");
        }
        User renter = userRepository.findUserByEmail(renterEmail);
        User owner = userRepository.findUserByEmail(rent.getRental().getEmail());

        String message = "Rent for " + rent.getRental().getCar().getBrand() + " " + rent.getRental().getCar().getModel() + " " + rent.getRental().getCar().getLicensePlate() + " from " + rent.getRental().getStartDate() + " to " + rent.getRental().getEndDate() + " has been canceled by " + renterEmail;
        emailService.sendEmail(owner.getEmail(),"Rent has been Cancelled", message);
        LocalDateTime receivedAt = LocalDateTime.now();
        Notification notiRenter = new Notification(message, false, false, "big", receivedAt);
        Notification notiOwner = new Notification(message, false, false, "small", rentId, receivedAt);

        renter.addNotification(notiRenter);
        owner.addNotification(notiOwner);

        userRepository.save(renter);
        userRepository.save(owner);
        return notificationRepository.save(notiOwner);
    }

    public Notification makeComplaintNotification(String renterEmail, Long rentId, Complaint complaint) throws RentServiceException{
        if(rentRepository.findById(rentId) == null){
            throw new RentServiceException("Rent not found", "rentId");
        }
        Rent rent = rentRepository.findById(rentId).get();
        if(userRepository.findUserByEmail(renterEmail) == null){
            throw new RentServiceException("Renter not found", "renterEmail");
        }
        if(userRepository.findUserByEmail(rent.getRental().getEmail()) == null){
            throw new RentServiceException("Owner not found", "rent.getRental().getEmail()");
        }
        User renter = userRepository.findUserByEmail(renterEmail);
        User owner = userRepository.findUserByEmail(rent.getRental().getEmail());

        String message = "A complaint has been filed by " + renterEmail + " for "+ rent.getRental().getCar().getBrand() + " " + rent.getRental().getCar().getModel() + " " + rent.getRental().getCar().getLicensePlate() + " with the following message: " + complaint.getMessage();
        String messageRenter = "Your complaint for " + rent.getRental().getCar().getBrand() + " " + rent.getRental().getCar().getModel() + " " + rent.getRental().getCar().getLicensePlate() + " has been sent succesfully";
        LocalDateTime receivedAt = LocalDateTime.now();
        System.out.println(receivedAt);

        Notification notiOwner = new Notification(message, false, false, "small", rentId, receivedAt);
        Notification notiRenter = new Notification(messageRenter, false, false, "small",receivedAt);

        renter.addNotification(notiRenter);
        owner.addNotification(notiOwner);

        userRepository.save(renter);
        userRepository.save(owner);
        return notificationRepository.save(notiOwner);
    }
    
    public Notification getNotificationsByRentId(Long rentId) throws RentServiceException{
        if(rentRepository.findById(rentId) == null){
            throw new RentServiceException("Rent not found", "rentId");
        }
        return notificationRepository.findNotificationByRentId(rentId);
    }
    
    public Notification getNotificationById(Long notificationId) throws NotificationServiceException{
        if(notificationRepository.findById(notificationId) == null){
            throw new NotificationServiceException("Notification not found", "notificationId");
        }
        return notificationRepository.findById(notificationId).get();
    }
}
