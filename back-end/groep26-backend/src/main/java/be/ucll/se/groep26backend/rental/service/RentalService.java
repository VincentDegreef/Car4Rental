package be.ucll.se.groep26backend.rental.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.se.groep26backend.rental.model.Rental;
import be.ucll.se.groep26backend.rental.repo.RentalRepository;


@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;



    public Rental addRental(Rental newRental) {
        return rentalRepository.save(newRental);
    }

    public List<Rental> getAllRentals() throws RentalServiceException {
        if (rentalRepository.findAll().isEmpty()) {
            throw new RentalServiceException("No rentals found", "rentals");
        }
        return rentalRepository.findAll();
    }

    public Rental getRentalById(long id) throws RentalServiceException {
        if (rentalRepository.findRentalById(id) == null) {
            throw new RentalServiceException("Rental not found", "id");
        }
        return rentalRepository.findRentalById(id);
    }
    

    public Rental cancelRental(long id) throws RentalServiceException {
        if(rentalRepository.findRentalById(id) == null) {
            throw new RentalServiceException("Rental not found", "id");
        }
        Rental rental = rentalRepository.findRentalById(id);
        if(rental.getRent() != null) {
            throw new RentalServiceException("Rental has a rent", "rent");
        }
        rentalRepository.delete(rental);
        return rental;
    }

    public List<Rental> getRentalsByEmail(String email) throws RentalServiceException {
        if (rentalRepository.findRentalsByEmail(email).isEmpty()) {
            throw new RentalServiceException("There are no rentals with this email address found as owner", "email");
        }
        return rentalRepository.findRentalsByEmail(email);
    }

    public List<Rental> getRentalsByCarBrand(String brand) throws RentalServiceException {
        if (rentalRepository.findRentalsByCarBrand(brand).isEmpty()) {
            throw new RentalServiceException("There are no rentals with this car brand found", "brand");
        }
        return rentalRepository.findRentalsByCarBrand(brand);
    }

    public List<Rental> getRentalsByCity(String city) throws RentalServiceException {
        if (rentalRepository.findRentalsByCity(city).isEmpty()) {
            throw new RentalServiceException("There are no rentals in this city found", "city");
        }
        return rentalRepository.findRentalsByCity(city);
    }

    public List<Rental> getRentalsByStartDate(LocalDate startDate) throws RentalServiceException {
        if (rentalRepository.findRentalsByStartDate(startDate).isEmpty()) {
            throw new RentalServiceException("There are no rentals with this start date found", "startDate");
        }
        return rentalRepository.findRentalsByStartDate(startDate);
    }

    public List<Rental> getRentalsByEndDate(LocalDate endDate) throws RentalServiceException {
        if (rentalRepository.findRentalsByEndDate(endDate).isEmpty()) {
            throw new RentalServiceException("There are no rentals with this end date found", "endDate");
        }
        return rentalRepository.findRentalsByEndDate(endDate);
    }
    
    public List<Rental> getRentalsByUserId(long userId) throws RentalServiceException {
        List<Rental> rentals = getAllRentals();
        rentals.removeIf(rental -> rental.getCar().getUser().getId() != userId);
        return rentals;
    }

    public List<Rental> getRentalsByPriceUnder(double price) throws RentalServiceException {
        List<Rental> rentals = getAllRentals();
        rentals.removeIf(rental -> rental.getPrice() > price);
        return rentals;
    }

    public List<Double> compareRentals(long rentalId1, long rentalId2, int miles, int fuelQuantity, LocalDate endDate) throws RentalServiceException{
        if(rentalRepository.findRentalById(rentalId1) == null) {
            throw new RentalServiceException("Rental 1 not found", "rentalId1");
        }

        if(rentalRepository.findRentalById(rentalId2) == null) {
            throw new RentalServiceException("Rental 2 not found", "rentalId2");
        }

        Rental rental1 = rentalRepository.findRentalById(rentalId1);
        Rental rental2 = rentalRepository.findRentalById(rentalId2);



        double EXTRA_MILE_PRICE = 0.2;
        double FUEL_PRICE_PER_LITER = 1.5;
        double FREE_MILES = 1000;

        

        long daysBetween1 = ChronoUnit.DAYS.between(rental1.getStartDate(), endDate);
        long daysBetween2 = ChronoUnit.DAYS.between(rental2.getStartDate(), endDate);

        double priceNrOfDays1 = daysBetween1 * rental1.getPrice();
        double priceNrOfDays2 = daysBetween2 * rental2.getPrice();

        double priceFuel = fuelQuantity * FUEL_PRICE_PER_LITER;
        double priceExtraMile = (miles - FREE_MILES ) * EXTRA_MILE_PRICE;

        double totalPrice1 = priceNrOfDays1 + priceFuel + priceExtraMile;
        double totalPrice2 = priceNrOfDays2 + priceFuel + priceExtraMile;

        List<Double> prices = List.of(totalPrice1, totalPrice2);
        return prices;
        
    }
}
    
