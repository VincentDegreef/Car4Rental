package be.ucll.se.groep26backend.rental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ucll.se.groep26backend.rental.model.Rental;
import be.ucll.se.groep26backend.rental.service.RentalService;
import be.ucll.se.groep26backend.rental.service.RentalServiceException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


// @CrossOrigin(origins = "*")
@CrossOrigin(origins = {"http://localhost:3000", "https://group26-frontend.azurewebsites.net", "https://group26-ac-frontend.azurewebsites.net", "https://groep26-frontend.azurewebsites.net"})
@RestController
@RequestMapping("/rentals")
public class RentalRestController {

    @Autowired
    private RentalService rentalService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST )
    @ExceptionHandler({ RentalServiceException.class})
    public Map<String, String>
    handleServiceExceptions(RentalServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).RENTER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ACCOUNTANT.getRoleName())")    
    @GetMapping("")
    public List<Rental> getAllRentals() throws RentalServiceException {
        return rentalService.getAllRentals();
    }

    // @PreAuthorize("hasRole('OWNER')")    
    @PostMapping("/add")
    public Rental addRental(@Valid @RequestBody Rental newRental) {
        return rentalService.addRental(newRental);
    }

    // @PreAuthorize("hasRole('OWNER')")        
    @DeleteMapping("/cancel/{rentalId}")
    public Rental cancelRental(@PathVariable("rentalId") Long rentalId) throws RentalServiceException{
        return rentalService.cancelRental(rentalId);
    }

    // @PreAuthorize("hasAnyRole('OWNER','RENTER','ACCOUNTANT')")    
    @GetMapping("/rentalsByEmail/{email}")
    public List<Rental> getRentalsByEmail(@PathVariable("email") String email) throws RentalServiceException{
        return rentalService.getRentalsByEmail(email);
    }

    // @PreAuthorize("hasAnyRole('OWNER','RENTER','ACCOUNTANT')")        
    @GetMapping("/rentalsByCarBrand/{brand}")
    public List<Rental> getRentalsByCarBrand(@PathVariable("brand") String brand) throws RentalServiceException{
        return rentalService.getRentalsByCarBrand(brand);
    }

    // @PreAuthorize("hasAnyRole('OWNER','RENTER','ACCOUNTANT')")        
    @GetMapping("/rentalsByCity/{city}")
    public List<Rental> getRentalsByCity(@PathVariable("city") String city) throws RentalServiceException{
        return rentalService.getRentalsByCity(city);
    }

    // @PreAuthorize("hasAnyRole('OWNER','RENTER','ACCOUNTANT')")        
    @GetMapping("/rentalsByStartDate/{startDate}")
    public List<Rental> getRentalsByStartDate(@PathVariable("startDate") LocalDate startDate) throws RentalServiceException{
        return rentalService.getRentalsByStartDate(startDate);
    }

    // @PreAuthorize("hasAnyRole('OWNER','RENTER','ACCOUNTANT')")        
    @GetMapping("/rentalsByEndDate/{endDate}")
    public List<Rental> getRentalsByEndDate(@PathVariable("endDate") LocalDate endDate) throws RentalServiceException{
        return rentalService.getRentalsByEndDate(endDate);
    }

    // @PreAuthorize("hasAnyRole('OWNER','RENTER','ACCOUNTANT')")        
    @GetMapping("/rentalById/{id}")
    public Rental getRentalById(@PathVariable("id") Long id) throws RentalServiceException{
        return rentalService.getRentalById(id);
    }
    
    @GetMapping("/getByUserId/{userId}")
    public List<Rental> getRentalsByUserId(@PathVariable("userId") Long userId) throws RentalServiceException{
        return rentalService.getRentalsByUserId(userId);
    }

    @GetMapping("/getByPriceUnder/{price}")
    public List<Rental> getRentalsByPriceUnder(@PathVariable("price") double price) throws RentalServiceException{
        return rentalService.getRentalsByPriceUnder(price);
    }

    @GetMapping("/compare/{rentalId1}/{rentalId2}/{miles}/{fuelQuantity}/{endDate}")
    public List<Double> compareRentals(@PathVariable("rentalId1") Long rentalId1, @PathVariable("rentalId2") Long rentalId2, @PathVariable("miles") int miles, @PathVariable("fuelQuantity") int fuelQuantity, @PathVariable("endDate") LocalDate endDate) throws RentalServiceException{
        return rentalService.compareRentals(rentalId1, rentalId2, miles, fuelQuantity, endDate);
    }
    
}