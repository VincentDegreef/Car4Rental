package be.ucll.se.groep26backend.car.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep26backend.car.model.Car;
import be.ucll.se.groep26backend.car.service.CarService;
import be.ucll.se.groep26backend.car.service.CarServiceException;
import be.ucll.se.groep26backend.rental.model.Rental;
import be.ucll.se.groep26backend.rental.service.RentalServiceException;
import jakarta.validation.Valid;
// @CrossOrigin(origins = "*")
@CrossOrigin(origins = {"http://localhost:3000", "https://group26-frontend.azurewebsites.net", "https://group26-ac-frontend.azurewebsites.net", "https://groep26-frontend.azurewebsites.net"})
@RestController
@RequestMapping("/cars")
public class CarRestController {
    @Autowired
    private CarService carService;
    
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
    
    @ExceptionHandler({ CarServiceException.class})
    public Map<String, String>
    handleServiceExceptions(CarServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
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
    public List<Car> getAllCars() throws CarServiceException {
        return carService.getAllCars();
    }


    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())")
    @DeleteMapping("/delete/{carId}") 
    public void deleteCar(@PathVariable int carId) throws CarServiceException {
        carService.deleteCar(carId);
    }


    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())")
    @PostMapping("{carId}/makeRental")
    public Car makeRental(@PathVariable("carId") Long carId, @Valid @RequestBody Rental newRental) throws CarServiceException, RentalServiceException{
        return carService.makeRental(carId, newRental);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())")
    @GetMapping("getCar/{carId}")
    public Car getCarById(@PathVariable("carId") Long carId) throws CarServiceException{
        return carService.getCarById(carId);
    }
    
    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())")
    @GetMapping("getByUserId/{userId}")
    public List<Car> getCarsByUserId(@PathVariable("userId") long userId) throws CarServiceException{
        return carService.getCarsByUserId(userId);
    }

    @PreAuthorize("hasAnyRole(T(be.ucll.se.groep26backend.role.model.RoleType).OWNER.getRoleName(),T(be.ucll.se.groep26backend.role.model.RoleType).ADMIN.getRoleName())")
    @GetMapping("/getRentalsByCarId/{carId}")
    public List<Rental> getRentalsByCarId(@PathVariable("carId") long carId) throws CarServiceException{
        return carService.getRentalsByCarId(carId);
    }
}