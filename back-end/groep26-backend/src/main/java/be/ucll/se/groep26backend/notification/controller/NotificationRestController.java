package be.ucll.se.groep26backend.notification.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep26backend.email.service.EmailService;
import be.ucll.se.groep26backend.notification.model.Notification;
import be.ucll.se.groep26backend.notification.service.NotificationService;
import be.ucll.se.groep26backend.notification.service.NotificationServiceException;
import be.ucll.se.groep26backend.rents.service.RentServiceException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


// @CrossOrigin(origins = "*")
@CrossOrigin(origins = {"http://localhost:3000", "https://group26-frontend.azurewebsites.net", "https://group26-ac-frontend.azurewebsites.net", "https://groep26-frontend.azurewebsites.net"})

@RestController
@RequestMapping("/notifications")
public class NotificationRestController {
    
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

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
    @ExceptionHandler({ NotificationServiceException.class})
    public Map<String, String>
    handleServiceExceptions(NotificationServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST )
    @ExceptionHandler({ RentServiceException.class})
    public Map<String, String>
    handleServiceExceptions(RentServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }


    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName())")
    @GetMapping("")
    public List<Notification> getAllNotifications(){
        return notificationService.getAllNotifications();
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName())")
    @PostMapping("/addNotification")
    public Notification addNotification(@Valid @RequestBody Notification newNotification){
        return notificationService.addNotification(newNotification);
    }
    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName())")
    @PostMapping("/makeRentRequestNotification/{renterEmail}/{rentId}")
    public Notification makeBigNotification(@PathVariable String renterEmail, @PathVariable Long rentId) throws RentServiceException, NotificationServiceException{
        try {
            return notificationService.makeBigNotification(renterEmail, rentId);
        } catch (RentServiceException e) {
            throw new RentServiceException("Rent not found", "rentId");
        }
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName())")
    @PostMapping("/makeConfirmNotification/{notificationId}")
    public Notification makeConfirmNotification(@PathVariable Long notificationId) throws NotificationServiceException, RentServiceException{
        try {
            return notificationService.makeConfirmNotification(notificationId);
        } catch (NotificationServiceException e) {
            throw new NotificationServiceException("Notification not found", "notificationId");
        }
    }
    
    
    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName())")
    @PostMapping("/makeCancelNotification/{notificationId}")
    public Notification makeCancelNotification(@PathVariable Long notificationId) throws NotificationServiceException, RentServiceException{
        try {
            return notificationService.makeCancelNotification(notificationId);
        } catch (NotificationServiceException e) {
            throw new NotificationServiceException("Notification not found", "notificationId");
        }
    }
    
    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName())")
    @PostMapping("/rentCancelNotification/{rentId}/{renterEmail}")
    public Notification rentCancelNotification(@PathVariable Long rentId, @PathVariable String renterEmail) throws NotificationServiceException, RentServiceException{
        try {
            return notificationService.cancelRentNotification(rentId, renterEmail);
        } catch (RentServiceException e) {
            throw new RentServiceException("Rent not found", "rentId");
        }
    }
    
    @DeleteMapping("/deleteNotification/{notificationId}")
    public void deleteNotification(@PathVariable Long notificationId) throws NotificationServiceException{
        notificationService.deleteNotification(notificationId);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName())")
    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable("id") Long id) throws NotificationServiceException {
        return notificationService.getNotificationById(id);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName())")
    @GetMapping("/getNotificationByRentId/{rentId}")
    public Notification getNotificationsByRentId(@PathVariable Long rentId) throws RentServiceException{
        return notificationService.getNotificationsByRentId(rentId);
    }

    // @PreAuthorize("hasAnyRole('OWNER', 'RENTER')")
    // @DeleteMapping("/deleteNotifications")
    // public List<Notification> deleteNotifications() throws NotificationServiceException{
    //     return notificationService.deleteNotifications();
    // }

    
}