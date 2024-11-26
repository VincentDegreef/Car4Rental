package be.ucll.se.groep26backend.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import be.ucll.se.groep26backend.role.model.Role;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.model.UserRegistrationRequest;
import be.ucll.se.groep26backend.user.service.UserServiceException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class userE2ETest {
    @Autowired
    private TestRestTemplate restTemplate;


    // @Test
    // public void testCreateUser() throws UserServiceException {
    //     // Given
    //     Role role = new Role(1,"OWNER");
        
    //     User newUser = new User("tester", "test123456", "tester@gmail.com", "0495752246", role );


    //     UserRegistrationRequest expectedUser = new UserRegistrationRequest(newUser, "OWNER");
    //     // When
    //     ResponseEntity<User> response = restTemplate.postForEntity("/rest/auth/register", expectedUser, User.class);

    //     // Then
    //     assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    //     User createdUser = response.getBody();
    //     // assertEquals(newUser, createdUser);
    //     // assertEquals(expectedUser.getUser().getUsername(), createdUser.getUsername());
    //     // assertEquals(expectedUser.getUser().getPassword(), createdUser.getPassword());
    //     // assertEquals(expectedUser.getUser().getEmail(), createdUser.getEmail());
    //     // assertEquals(expectedUser.getUser().getPhoneNumber(), createdUser.getPhoneNumber());
    // }
}

