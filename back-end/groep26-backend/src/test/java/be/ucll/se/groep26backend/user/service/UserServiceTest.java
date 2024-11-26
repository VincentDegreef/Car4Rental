/*package be.ucll.se.groep26backend.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setup() throws UserServiceException{
        user1 = new User("user1", "password1", "user1@gmail.com", "0495565898");
        user2 = new User("user2", "password2", "user2@gmail.com", "0495565698");
        user3 = new User("user3", "password3", "user3@gmail.com", "0495565598");
    }

    @Test
    public void givenNoUsers_whenGetAllUsers_thenReturnEmptyList() {
        // given
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        // when
        List<User> users = userService.getAllUsers();
        // then
        verify(userRepository).findAll();
        assert(users.isEmpty());
    }

    @Test
    public void givenNoUsers_whenValidUserIsCreated_thenUserIsReturned() throws UserServiceException{
        // given
        when(userRepository.save(user1)).thenReturn(user1);
        // when
        User newUser = userService.createUser(user1);
        // then
        verify(userRepository).save(user1);
        assertEquals(user1.getUsername(), newUser.getUsername());
        assertEquals(user1.getPassword(), newUser.getPassword());
        assertEquals(user1.getEmail(), newUser.getEmail());
        assertEquals(user1.getPhoneNumber(), newUser.getPhoneNumber());
    }

    @Test
    public void givenUsers_whenNewUserWithAlreadyExistingUsernameIsCreated_thenUserServiceExceptionIsThrown() throws UserServiceException{
        // given
        User otherUser1 = new User("user1", "password1", "otherUser1@gmail.com", "0469257849");
        when(userRepository.findUserByUsername(otherUser1.getUsername())).thenReturn(user1);
        // when
        UserServiceException exception = assertThrows(UserServiceException.class, () -> userService.createUser(user1));
        // then
        assertEquals("Username already exists", exception.getMessage());
        assertEquals("username", exception.getField());
    }

    @Test
    public void givenUsers_whenNewUserWithAlreadyExistingEmailIsCreated_thenUserServiceExceptionIsThrown() throws UserServiceException{
        // given
        User otherUser1 = new User("otherUser1", "password1", "user1@gmail.com", "0469257849");
        when(userRepository.findUserByEmail(otherUser1.getEmail())).thenReturn(user1);
        // when
        UserServiceException exception = assertThrows(UserServiceException.class, () -> userService.createUser(user1));
        // then
        assertEquals("Email already exists", exception.getMessage());
        assertEquals("email", exception.getField());
    }

       

    @Test
    public void givenUsers_whenGetAllUsers_thenReturnListOfUsers() {
        // given
        when(userRepository.findAll()).thenReturn(List.of(user1, user2, user3));
        // when
        List<User> allUsers = userService.getAllUsers();
        // then
        verify(userRepository).findAll();
        assertEquals(3, allUsers.size());
    }

    @Test
    public void givenUsers_whenGetUserByUsername_thenReturnUser() throws UserServiceException {
        // given
        when(userRepository.findUserByUsername("user1")).thenReturn(user1);
        // when
        User user = userService.getUserByUsername("user1");
        // then
        assertEquals(user1.getUsername(), user.getUsername());
        assertEquals(user1.getPassword(), user.getPassword());
 
    }

    @Test
    public void givenUsers_whenGetUserByUsernameWithNonExistingUsername_thenUserServiceExceptionIsThrown() {
        // given
        when(userRepository.findUserByUsername("user1")).thenReturn(null);
        // when
        UserServiceException exception = assertThrows(UserServiceException.class, () -> userService.getUserByUsername("user1"));
        // then
        assertEquals("User not found", exception.getMessage());
        assertEquals("username", exception.getField());
    }

    @Test
    public void givenUsers_whenGetUserByEmail_thenReturnUser() throws UserServiceException {
        // given
        when(userRepository.findUserByEmail("user1@gmail.com")).thenReturn(user1);
        // when
        User user = userService.getUserByEmail("user1@gmail.com");
        // then
        assertEquals(user1.getUsername(), user.getUsername());
        assertEquals(user1.getPassword(), user.getPassword());
    }

    @Test
    public void givenUsers_whenGetUserByEmailWithNonExistingEmail_thenUserServiceExceptionIsThrown() {
        // given
        when(userRepository.findUserByEmail("user2@gmail.com")).thenReturn(null);
        // when
        UserServiceException exception = assertThrows(UserServiceException.class, () -> userService.getUserByEmail("user2@gmail.com"));
        // then
        assertEquals("User not found", exception.getMessage());
        assertEquals("email", exception.getField());
    }
}*/
