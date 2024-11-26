package be.ucll.se.groep26backend.user.model;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.ucll.se.groep26backend.role.model.Role;
import be.ucll.se.groep26backend.user.service.UserServiceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserTest {
    
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private String username = "VincentD";
    private String password = "Password";
    private String email = "vincent.degreef@student.ucll.be";
    private String phoneNumber = "0491257948";
    private Role role = new Role(1,"OWNER");

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
    void givenValidValues_whenCreatingUser_thenUserIsCreatedWithTheseValues() throws UserServiceException{
        User user = new User(username, password, email, phoneNumber, role);

        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertEquals(phoneNumber, user.getPhoneNumber());
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void givenInvalidUsername_whenCreatingUser_thenConstraintViolationExceptionIsThrown() throws UserServiceException{
        User user = new User("", password, email, phoneNumber, role);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Username is required.", violation.getMessage());
    }

    @Test
    void givenInvalidPassword_whenCreatingUser_thenConstraintViolationExceptionIsThrown() throws UserServiceException{
        User user = new User(username, "", email, phoneNumber, role);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Password is required.", violation.getMessage());
    }

    @Test
    void givenInvalidEmail_whenCreatingUser_thenConstraintViolationExceptionIsThrown() throws UserServiceException {
        User user = new User(username, password, "", phoneNumber, role);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Email is required.", violation.getMessage());
    }

    @Test
    void givenInvalidPhoneNumber_whenCreatingUser_thenConstraintViolationExceptionIsThrown() throws UserServiceException {
        User user = new User(username, password, email, "", role);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Phone number is required.", violation.getMessage());
    }

    @Test
    void givenInvalidValues_whenCreatingUser_thenConstraintViolationExceptionIsThrown() throws UserServiceException{
        User user = new User("", "", "", "", new Role());
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(4, violations.size());
    }

    @Test
    void givenInvalidUsername_whenCreatingUser_thenConstraintViolationExceptionIsThrown2() throws UserServiceException{
        String invalidUsername = "Vin";
        UserServiceException exception = assertThrows(UserServiceException.class, () -> new User(invalidUsername, password, email, phoneNumber, role));
        assertEquals("Username must be at least 5 characters long.", exception.getMessage());
        assertEquals("username", exception.getField());
    }

    @Test
    void givenInvalidEmail_whenCreatingUser_thenConstraintViolationExceptionIsThrown2() throws UserServiceException{
        String invalidEmail = "vincent.degreefstudent.ucll.be";
        User user = new User(username, password, invalidEmail, phoneNumber, role);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Email must contain @.", violation.getMessage());
    }

    @Test
    void givenInvalidPhoneNumber_whenCreatingUser_thenConstraintViolationExceptionIsThrown2() throws UserServiceException{
        String invalidPhoneNumber = "049125";
        UserServiceException exception = assertThrows(UserServiceException.class, () -> new User(username, password, email, invalidPhoneNumber, role));
        assertEquals("Phone number must have 10 numbers", exception.getMessage());
        assertEquals("phoneNumber", exception.getField());
    }

    @Test
    void givenShortInvalidPassword_whenCreatingUser_thenConstraintViolationExceptionIsThrown2() throws UserServiceException {
        String invalidPassword = "pass";
        UserServiceException exception = assertThrows(UserServiceException.class, () -> new User(username, invalidPassword, email, phoneNumber, role));
        assertEquals("Password must be at least 8 characters long.", exception.getMessage());
        assertEquals("password", exception.getField());
    }

}


