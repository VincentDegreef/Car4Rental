package be.ucll.se.groep26backend.rent.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.ucll.se.groep26backend.rents.model.Rent;
import be.ucll.se.groep26backend.rents.service.RentServiceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class RentTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    // rent attributes
    private String phoneNumber = "0491257948";
    private String email = "jago.onzea@student.ucll.be";
    private String identification = "01.01.01-001.01";
    private LocalDate birthDate = LocalDate.of(2000, 1, 1);
    private String drivingLicenceNumber = "0000000000";
    private Boolean isCancelled = false;

    @BeforeAll
    public static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    // @Test
    // void givenValidValues_whenCreatingRent_thenRentIsCreatedWithTheseValues() throws RentServiceException{
    //     Rent rent = new Rent(phoneNumber, email, identification, birthDate, drivingLicenceNumber, birthDate, 0, 0);

    //     assertNotNull(rent);
    //     assertEquals(phoneNumber, rent.getPhoneNumber());
    //     assertEquals(email, rent.getEmail());
    //     assertEquals(identification, rent.getIdentification());
    //     assertEquals(birthDate, rent.getBirthDate());
    //     assertEquals(drivingLicenceNumber, rent.getDrivingLicenceNumber());
    //     assertEquals(isCancelled, rent.getIsCancelled());
    //     Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
    //     assertTrue(violations.isEmpty());
    // }

    @Test
    void givenInvalidPhoneNumber_whenCreatingRent_thenConstraintViolationExceptionIsThrown() {
        String invalidPhoneNumber = "";
        Rent rent = new Rent(invalidPhoneNumber, email, identification, birthDate, drivingLicenceNumber, birthDate, 0, 0);

        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertEquals(2, violations.size());
    }

    @Test
    void givenInvalidEmail_whenCreatingRent_thenConstraintViolationExceptionIsThrown() {
        String invalidEmail = "ja";
        Rent rent = new Rent(phoneNumber, invalidEmail, identification, birthDate, drivingLicenceNumber, birthDate, 0, 0);

        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertEquals(2, violations.size());
    }

    @Test
    void givenInvalidIdentification_whenCreatingRent_thenConstraintViolationExceptionIsThrown() {
        String invalidIdentification = "01.01.01-001.0";
        Rent rent = new Rent(phoneNumber, email, invalidIdentification, birthDate, drivingLicenceNumber, birthDate, 0, 0);

        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertEquals(2, violations.size());
    }

    @Test
    void givenNoBirthDate_whenCreatingRent_thenConstraintViolationExceptionIsThrown() {
        LocalDate invalidBirthDate = null;
        Rent rent = new Rent(phoneNumber, email, identification, invalidBirthDate, drivingLicenceNumber, invalidBirthDate, 0, 0);

        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertEquals(2, violations.size());
    }
}