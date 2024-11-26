package be.ucll.se.groep26backend.notification.repo;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import be.ucll.se.groep26backend.email.service.EmailService;
import be.ucll.se.groep26backend.notification.model.Notification;
import be.ucll.se.groep26backend.notification.repo.NotificationRepository;
import be.ucll.se.groep26backend.notification.service.NotificationService;
import be.ucll.se.groep26backend.notification.service.NotificationServiceException;
import be.ucll.se.groep26backend.rents.model.Rent;
import be.ucll.se.groep26backend.rents.repo.RentRepository;
import be.ucll.se.groep26backend.rents.service.RentServiceException;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;

public class NotificationRepositoryTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private RentRepository rentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNotification() {
        Notification newNotification = new Notification("Test Message", false, false, "Test Tag", LocalDateTime.now());
        when(notificationRepository.save(any(Notification.class))).thenReturn(newNotification);

        Notification addedNotification = notificationService.addNotification(newNotification);

        assertEquals(newNotification, addedNotification);
    }

    @Test
    void testGetAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Test Message 1", false, false, "Test Tag 1", LocalDateTime.now()));
        notifications.add(new Notification("Test Message 2", false, false, "Test Tag 2", LocalDateTime.now()));

        when(notificationRepository.findAll()).thenReturn(notifications);

        List<Notification> retrievedNotifications = notificationService.getAllNotifications();

        assertEquals(notifications.size(), retrievedNotifications.size());
    }

    @Test
    void testDeleteNotification() throws NotificationServiceException {
        Long id = 1L;
        Notification notificationToDelete = new Notification("Test Message", false, false, "Test Tag", LocalDateTime.now());

        when(notificationRepository.findById(id)).thenReturn(Optional.of(notificationToDelete));

        notificationService.deleteNotification(id);

        verify(notificationRepository).delete(notificationToDelete);
    }

    
}
