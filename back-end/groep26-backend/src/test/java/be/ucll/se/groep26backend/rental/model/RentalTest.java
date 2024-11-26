package be.ucll.se.groep26backend.rental.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.ucll.se.groep26backend.rental.service.RentalServiceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class RentalTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    // rental attributes
    private LocalDate startDate = LocalDate.of(2025, 1, 1);
    private LocalDate endDate = LocalDate.of(2025, 1, 5);
    private String city = "Leuven";
    private String phoneNumber = "0491257948";
    private String email = "quentin.gillis@student.ucll.be";
    private double price = 100.0;


    @BeforeAll
    public static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    void givenValidValues_whenCreatingRental_thenRentalIsCreatedWithTheseValues() throws RentalServiceException{
        Rental rental = new Rental(startDate, null, endDate, null, "", "", "", city, phoneNumber, email, price);

        assertNotNull(rental);
        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertTrue(violations.isEmpty());
    }

    @Test
    void givenInvalidCity_whenCreatingRental_thenConstraintViolationExceptionIsThrown() throws RentalServiceException{
        Rental rental = new Rental(startDate, null, endDate, null, "", "", "", "", phoneNumber, email, price);

        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertEquals(1, violations.size());
    }

    @Test
    void givenInvalidPhoneNumber_whenCreatingRental_thenConstraintViolationExceptionIsThrown() throws RentalServiceException{
        Rental rental = new Rental(startDate, null, endDate, null, "", "", "", city, "", email, price);

        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertEquals(1, violations.size());
    }

    @Test
    void givenInvalidEmail_whenCreatingRental_thenConstraintViolationExceptionIsThrown() throws RentalServiceException{
        Rental rental = new Rental(startDate, null, endDate, null, "", "", "", city, phoneNumber, "", price);

        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertEquals(1, violations.size());
    }

    @Test
    void givenInvalidPrice_whenCreatingRental_thenConstraintViolationExceptionIsThrown() throws RentalServiceException{
        Rental rental = new Rental(startDate, null, endDate, null, "", "", "", city, phoneNumber, email, -1.0);

        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertEquals(1, violations.size());
    }
}