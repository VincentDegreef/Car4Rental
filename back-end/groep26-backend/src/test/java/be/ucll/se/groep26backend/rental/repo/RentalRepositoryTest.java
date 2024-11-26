package be.ucll.se.groep26backend.rental.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import be.ucll.se.groep26backend.rental.model.Rental;

@DataJpaTest
public class RentalRepositoryTest {
    // @Autowired
    // private RentalRepository rentalRepository;

    // // rental attributes
    // LocalDate startDate
    // LocalDate endDate
    // String city
    // String phoneNumber
    // String email
    // double price

    // @BeforeEach
    // void setUp() {
    //     rentalRepository.deleteAll();
    // }

    // @Test
    // void whenSaveRent_thenFindRentById() {
    //     Rental rental = new Rental(phoneNumber, email, identification, birthDate, drivingLicenceNumber, isCancelled);
    //     rentalRepository.save(rental);
    //     Rental foundRental = rentalRepository.findRentalById(1);
    //     assertEquals(rental, foundRental);
    // }

    // @Test
    // void whenSaveRent_thenFindRentByEmail() {
    //     Rental rental = new Rental(phoneNumber, email, identification, birthDate, drivingLicenceNumber, isCancelled);
    //     rentalRepository.save(rental);
    //     Rental foundRental = rentalRepository.findRentsByEmail(email).get(0);
    //     assertEquals(rental, foundRental);
    // }
}
