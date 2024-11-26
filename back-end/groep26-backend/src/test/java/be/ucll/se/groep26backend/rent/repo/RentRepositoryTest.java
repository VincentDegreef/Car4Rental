package be.ucll.se.groep26backend.rent.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import be.ucll.se.groep26backend.rents.model.Rent;
import be.ucll.se.groep26backend.rents.repo.RentRepository;

@DataJpaTest
public class RentRepositoryTest {
    @Autowired
    private RentRepository rentRepository;

    // rent attributes
    private String phoneNumber = "0491257948";
    private String email = "jago.onzea@student.ucll.be";
    private String identification = "01.01.01-001.01";
    private LocalDate birthDate = LocalDate.of(2000, 1, 1);
    private String drivingLicenceNumber = "0000000000";
    private Boolean isCancelled = false;

    @BeforeEach
    void setUp() {
        rentRepository.deleteAll();
    }

    // @Test
    // void whenSaveRent_thenFindRentById() {
    //     Rent rent = new Rent(phoneNumber, email, identification, birthDate, drivingLicenceNumber, isCancelled);
    //     rentRepository.save(rent);
    //     Rent foundRent = rentRepository.findRentById(1);
    //     assertEquals(rent, foundRent);
    // }

    // @Test
    // void whenSaveRent_thenFindRentByEmail() {
    //     Rent rent = new Rent(phoneNumber, email, identification, birthDate, drivingLicenceNumber, isCancelled, birthDate, LocalDate.now(),null, 0, 0);
    //     rentRepository.save(rent);
    //     Rent foundRent = rentRepository.findRentsByEmail(email).get(0);
    //     assertEquals(rent, foundRent);
    // }
}