package be.ucll.se.groep26backend.car.model;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.ucll.se.groep26backend.car.service.CarServiceException;
import be.ucll.se.groep26backend.role.model.Role;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.service.UserServiceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


public class CarTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private String brand = "Audi";
    private String licensePlate = "1-ABC-123";
    private String type = "Sedan";
    private String model = "A4";
    private int numberOfSeats = 5;
    private int numberOfChildSeats = 2;
    private boolean foldableRearSeats = true;
    private boolean towbar = false;

    private String username = "VincentD";
    private String password = "Password";
    private String email = "vincent.degreef@student.ucll.be";
    private String phoneNumber = "0491257948";
    private Role role = new Role("OWNER");


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
    void givenValidValues_whenCreatingCar_thenCarIsCreatedWithTheseValues() throws CarServiceException, UserServiceException{
        User user = new User(username, password, email, phoneNumber, role);
        Car car = new Car(brand, model, licensePlate, type, numberOfSeats, numberOfChildSeats, foldableRearSeats, towbar, user);

        assertNotNull(car);
        assertEquals(brand, car.getBrand());
        assertEquals(licensePlate, car.getLicensePlate());
        assertEquals(type, car.getType());
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertTrue(violations.isEmpty());
    }

    @Test
    void givenInvalidBrand_whenCreatingCar_thenConstraintViolationExceptionIsThrown() throws CarServiceException, UserServiceException{
        User user = new User(username, password, email, phoneNumber, role);
        Car car = new Car("", model, licensePlate, type, numberOfSeats, numberOfChildSeats, foldableRearSeats, towbar, user);
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Brand is required", violation.getMessage());
    }

    @Test
    void givenInvalidModel_whenCreatingCar_thenConstraintViolationExceptionIsThrown() throws CarServiceException, UserServiceException{
        User user = new User(username, password, email, phoneNumber, role);
        Car car = new Car(brand, "", licensePlate, type, numberOfSeats, numberOfChildSeats, foldableRearSeats, towbar, user);
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Model is required", violation.getMessage());
    }

    @Test
    void givenInvalidLicensePlate_whenCreatingCar_thenConstraintViolationExceptionIsThrown() throws CarServiceException, UserServiceException{
        User user = new User(username, password, email, phoneNumber, role);
        Car car = new Car(brand, model, "", type, numberOfSeats, numberOfChildSeats, foldableRearSeats, towbar, user);
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("License plate is required", violation.getMessage());
    }

    @Test
    void givenInvalidType_whenCreatingCar_thenConstraintViolationExceptionIsThrown() throws CarServiceException, UserServiceException{
        User user = new User(username, password, email, phoneNumber, role);
        Car car = new Car(brand, model, licensePlate, "", numberOfSeats, numberOfChildSeats, foldableRearSeats, towbar, user);
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Type is required", violation.getMessage());
    }

    @Test
    void givenInvalidNumberOfSeats_whenCreatingCar_thenConstraintViolationExceptionIsThrown() throws CarServiceException, UserServiceException{
        User user = new User(username, password, email, phoneNumber, role);
        Car car = new Car(brand, model, licensePlate, type, 0, numberOfChildSeats, foldableRearSeats, towbar, user);
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Number of seats must be greater than 0", violation.getMessage());
    }

    @Test
    void givenInvalidNumberOfChildSeats_whenCreatingCar_thenConstraintViolationExceptionIsThrown() throws CarServiceException, UserServiceException{
        User user = new User(username, password, email, phoneNumber, role);
        Car car = new Car(brand, model, licensePlate, type, numberOfSeats, -1, foldableRearSeats, towbar, user);
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Number of child seats must be greater than or equal to 0", violation.getMessage());
    }

    @Test
    void givenInvalidValues_whenCreatingCar_thenConstraintViolationExceptionIsThrown() throws CarServiceException, UserServiceException{
        User user = new User(username, password, email, phoneNumber, role);
        Car car = new Car("", "", "", "", 0, -1, false, false, user);
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(6, violations.size());
    }
    
}