/*package be.ucll.se.groep26backend.user.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.service.UserServiceException;

@DataJpaTest
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void whenSaveUser_thenFindUserById() throws UserServiceException {
        User user = new User("tester","test123456","tester@gmail.com","0495752246");

        userRepository.save(user);

        Long userId = user.getId();

        User foundUser = userRepository.findById(userId).orElseThrow();

        assertEquals(userId, foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getPassword(), foundUser.getPassword());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPhoneNumber(), foundUser.getPhoneNumber());
    }

    @Test
    void whenSaveUser_thenFindUserByUsername() throws UserServiceException {
        User user = new User("tester","test123456","tester@gmail.com","0495752246");

        userRepository.save(user);

        String username = user.getUsername();

        User foundUser = userRepository.findUserByUsername(username);

        assertEquals(username, foundUser.getUsername());
        assertEquals(user.getPassword(), foundUser.getPassword());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPhoneNumber(), foundUser.getPhoneNumber());
    }

    @Test
    void whenSaveUser_thenFindUserByEmail() throws UserServiceException {
        User user = new User("tester","test123456","tester@gmail.com","0495752246");

        userRepository.save(user);

        String email = user.getEmail();

        User foundUser = userRepository.findUserByEmail(email);

        assertEquals(email, foundUser.getEmail());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getPassword(), foundUser.getPassword());
        assertEquals(user.getPhoneNumber(), foundUser.getPhoneNumber());
    }

    @Test
    void whenSaveUser_thenFindAllUsers() throws UserServiceException {
        User user = new User("tester","test123456","tester@gmail.com","0495752246");
        User user2 = new User("tester","test123456","tester@gmail.com","0495752246");
        User user3 = new User("tester","test123456","tester@gmail.com","0495752246");
        User user4 = new User("tester","test123456","tester@gmail.com","0495752246");

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        List<User> users = userRepository.findAll();

        assertEquals(4, users.size());
    }

    // String requestBody = "{\"username\":\"tester\",\"password\":\"test123456\",\"email\":\"tester@gmail.com\",\"phoneNumber\":\"0495752246\"}";
}
*/