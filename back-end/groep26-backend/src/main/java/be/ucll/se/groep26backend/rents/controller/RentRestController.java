package be.ucll.se.groep26backend.rents.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ucll.se.groep26backend.notification.service.NotificationService;
import be.ucll.se.groep26backend.complaints.model.Complaint;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep26backend.rents.model.CheckOut;
import be.ucll.se.groep26backend.rents.model.Rent;
import be.ucll.se.groep26backend.rents.service.RentService;
import be.ucll.se.groep26backend.rents.service.RentServiceException;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.service.UserService;
import be.ucll.se.groep26backend.user.service.UserServiceException;
import be.ucll.se.groep26backend.wallet.service.WalletService;
import be.ucll.se.groep26backend.wallet.service.WalletServiceException;
import jakarta.validation.Valid;
import be.ucll.se.groep26backend.email.service.EmailService;

// @CrossOrigin(origins = "*")
@CrossOrigin(origins = {"http://localhost:3000", "https://group26-frontend.azurewebsites.net", "https://group26-ac-frontend.azurewebsites.net", "https://groep26-frontend.azurewebsites.net"})

@RestController
@RequestMapping("/rents")
public class RentRestController {
    @Autowired
    private RentService rentService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;
    

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
    @ExceptionHandler({ RentServiceException.class})
    public Map<String, String>
    handleServiceExceptions(RentServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ACCOUNTANT.getRoleName())")
    @GetMapping("")
    public List<Rent> getAllRents() throws RentServiceException {
        return rentService.getAllRents();
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())") 
    @PostMapping("/makeRent/{rentalId}/{userEmail}")
    public Rent addRent(@Valid @RequestBody Rent newRent, @PathVariable Long rentalId, @PathVariable String userEmail) throws RentServiceException {
        return rentService.addRent(newRent, rentalId, userEmail);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ACCOUNTANT.getRoleName())")
    @DeleteMapping("/cancel/{rentId}")
    public void cancelRent(@PathVariable("rentId") Long rentId) throws RentServiceException{
        rentService.cancelRent(rentId);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ACCOUNTANT.getRoleName())")
    @GetMapping("/rentsByEmail/{email}")
    public List<Rent> getRentsByEmail(@PathVariable("email") String email) throws RentServiceException{
        return rentService.getRentsByEmail(email);
    }
    
    @GetMapping("/getByUserId/{userId}")
    public List<Rent> getRentsByUserId(@PathVariable("userId") Long userId) throws RentServiceException{
        return rentService.getRentsByUserId(userId);
    }

    @GetMapping("/{id}")
    public Rent getRentById(@PathVariable("id") Long id) throws RentServiceException{
        return rentService.getRentById(id);
    }

    @PostMapping("/checkout/{rentId}")
    public Double checkOut(@Valid @RequestBody CheckOut checkOut, @PathVariable("rentId") Long rentId) throws RentServiceException, WalletServiceException, UserServiceException {
        Map<String, Double> costDetails = rentService.checkOut(rentId, checkOut.getEndDate(), checkOut.getReturnMillage(), checkOut.getReturnFuelQuantity());
    
        Rent rentToUpdate = rentService.getRentById(rentId);
        User renter = userService.getUserByEmail(rentToUpdate.getEmail());

        //Wallet of the renter
        long renterWalletId = userService.getWalletByUsername(renter.getUsername()).getId();
        
        //Wallet of the owner
        long ownerWalletId = rentToUpdate.getRental().getCar().getUser().getWallet().getId();
    

        rentToUpdate.setReturnMillage(checkOut.getReturnMillage());
        rentToUpdate.setReturnFuelQuantity(checkOut.getReturnFuelQuantity());
        rentToUpdate.setEndDate(checkOut.getEndDate());
        rentToUpdate.setTotalPrice(costDetails.get("totalPrice"));

        rentService.deleteRent(rentId);
    
        String emailMessage = String.format(
            "With this email, we want to let you know that your rent has been successfully checked out.\n\n" +
            "We have made a clear overview of the costs during your rental period:\n" +
            "Cost for total days: €%.2f\n" +
            "Price for driven mileage: €%.2f\n" +
            "Price for the fuel quantity: €%.2f\n" +
            "Total cost: €%.2f",
            costDetails.get("priceNrOfDays"),
            costDetails.get("priceExtraMile"),
            costDetails.get("priceFuel"),
            costDetails.get("totalPrice")
        );

        walletService.subtractBalance(renterWalletId, costDetails.get("totalPrice"));
        walletService.addBalance(ownerWalletId, costDetails.get("totalPrice"));
    
        emailService.sendEmail(rentToUpdate.getEmail(), "Rent checked out", emailMessage);
    
        return costDetails.get("totalPrice");
    }


}