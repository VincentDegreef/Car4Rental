package be.ucll.se.groep26backend.rents.service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ucll.se.groep26backend.complaints.model.Complaint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.se.groep26backend.rental.model.Rental;
import be.ucll.se.groep26backend.rental.repo.RentalRepository;
import be.ucll.se.groep26backend.rents.model.Rent;
import be.ucll.se.groep26backend.rents.repo.RentRepository;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;

@Service
public class RentService {

    public static final int FREE_MILES = 1000;
    public static final double FUEL_PRICE_PER_LITER = 1.5;
    public static final double EXTRA_MILE_PRICE = 0.2;

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;


    public List<Rent> getAllRents() throws RentServiceException{
        if(rentRepository.findAll().isEmpty()) {
            throw new RentServiceException("No rents found", "rents");
        }
        return rentRepository.findAll();
    }

    public void cancelRent(long id) throws RentServiceException{
        if(rentRepository.findRentById(id) == null){
            throw new RentServiceException("Rent was not found", "id");
        }
        Rent rent = rentRepository.findRentById(id);

        rentRepository.delete(rent);
    }

    public List<Rent> getRentsByEmail(String email) throws RentServiceException{
        if(email.isBlank()) {
            throw new RentServiceException("You need to choose one or more values to get search results", "email");
        }

        return rentRepository.findRentsByEmail(email);
    }

    public Rent addRent(Rent newRent, Long rentalId, String userEmail) throws RentServiceException {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentServiceException("Rental with id " + rentalId + " does not exist", "rentalId"));
        
        if (newRent == null) {
            throw new RentServiceException("Rent is null", "rent");
        }
        
        if (newRent.getEmail().isBlank()) {
            throw new RentServiceException("Email is required", "email");
        }
        
        if (!newRent.getEmail().equals(userEmail)) {
            throw new RentServiceException("Provided email does not match user's email", "email");
        }
        
        User user = userRepository.findUserByEmail(userEmail);
        
        if (user == null) {
            throw new RentServiceException("User with email " + userEmail + " does not exist", "userEmail");
        }
        
        newRent.setUser(user);
        rental.setRent(newRent);
        newRent.setRental(rental);
        
        rentRepository.save(newRent);
        rentalRepository.save(rental);
        
        return newRent;
    }

    public void deleteRent(long id) throws RentServiceException {
        Rent rent = rentRepository.findRentById(id);

        if(rent == null){
            throw new RentServiceException("Rent not found", "id");
        }

        rentRepository.delete(rent);

    }

    public Map<String, Double> checkOut(long id, LocalDate endDate, int returnMillage, int returnFuelQuantity) throws RentServiceException {

        if(rentRepository.findRentById(id) == null){
            throw new RentServiceException("Rent was not found", "id");
        }

        Rent rent = rentRepository.findRentById(id);
        

        long daysBetween = ChronoUnit.DAYS.between(rent.getStartDate(), endDate);

        double priceNrOfDays = daysBetween * rent.getRental().getPrice();
        double priceFuel = 0;
        double priceExtraMile = 0;

        double nrOfMiles = returnMillage - rent.getStartMillage() - FREE_MILES;

        if (nrOfMiles > 0) {
            priceExtraMile = nrOfMiles * EXTRA_MILE_PRICE;
        }

        if(returnFuelQuantity < rent.getStartFuelQuantity()) {
            priceFuel = Math.abs(returnFuelQuantity - rent.getStartFuelQuantity()) * FUEL_PRICE_PER_LITER;
            priceFuel = Math.abs(returnFuelQuantity - rent.getStartFuelQuantity()) * FUEL_PRICE_PER_LITER;
        }

        double totalPrice = priceNrOfDays + priceFuel + priceExtraMile;

        Map<String, Double> costDetails = new HashMap<>();
        costDetails.put("totalPrice", totalPrice);
        costDetails.put("priceNrOfDays", priceNrOfDays);
        costDetails.put("priceFuel", priceFuel);
        costDetails.put("priceExtraMile", priceExtraMile);
    
        return costDetails;
    }



    // public Double checkOut(long id, int returnMillage, int returnFuelQuantity) throws RentServiceException {
    //     return null;
    // }

    public Rent getRentById(long id) throws RentServiceException {

        return rentRepository.findRentById(id);

    }

    public Rent updateRent(Rent rent) throws RentServiceException {

        return rentRepository.save(rent);

    }


    public List<Rent> getRentsByUserId(long userId) throws RentServiceException{
        List<Rent> rents = getAllRents();
        rents.removeIf(rent -> rent.getRental().getCar().getUser().getId() != userId);
        return rents;
    }

}
