package be.ucll.se.groep26backend.notification.service;

import be.ucll.se.groep26backend.car.model.Car;
import be.ucll.se.groep26backend.email.service.EmailService;
import be.ucll.se.groep26backend.notification.model.Notification;
import be.ucll.se.groep26backend.notification.repo.NotificationRepository;
import be.ucll.se.groep26backend.rental.model.Rental;
import be.ucll.se.groep26backend.rental.service.RentalServiceException;
import be.ucll.se.groep26backend.rents.model.Rent;
import be.ucll.se.groep26backend.rents.repo.RentRepository;
import be.ucll.se.groep26backend.rents.service.RentServiceException;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;
import be.ucll.se.groep26backend.user.service.UserServiceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

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

    private Rent rent;
    private User owner;
    private User renter;

    /*@BeforeEach
    void setUp() throws UserServiceException, RentalServiceException {
        owner = new User("owner@example.com", "Owner123");
        renter = new User("renter@example.com", "Renter123");
        Car car = new Car("Toyota", "Corolla", "ABC123", "Sedan", 5, 2, true, false, owner);
        Rental rental = new Rental(LocalDate.now(), null, LocalDate.now().plusDays(7), null, "Street", "1", "1234", "City", "123456789", "email@example.com", 0);
        rental.setCar(car);
        rent = new Rent("1234567890", "renter@example.com", "yy.mm.dd-xxx.zz", LocalDate.now(), "0000000000", null);
        rent.setUser(owner);
        rent.setRental(rental);
    }

    @Test
    void addNotification() {
        Notification notification = new Notification("Test Notification", false, false, "tag", LocalDateTime.now());
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification savedNotification = notificationService.addNotification(notification);

        assertEquals(notification, savedNotification);
        verify(notificationRepository).save(notification);
    }

    @Test
    void getAllNotifications() {
        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(new Notification("Notification 1", false, false, "tag", LocalDateTime.now()));
        notificationList.add(new Notification("Notification 2", false, false, "tag", LocalDateTime.now()));

        when(notificationRepository.findAll()).thenReturn(notificationList);

        List<Notification> retrievedNotifications = notificationService.getAllNotifications();

        assertEquals(2, retrievedNotifications.size());
        assertEquals("Notification 1", retrievedNotifications.get(0).getMessage());
        assertEquals("Notification 2", retrievedNotifications.get(1).getMessage());
    }

    @Test
    void deleteNotification() throws NotificationServiceException {
        Notification notification = new Notification("Test Notification", false, false, "tag", LocalDateTime.now());

        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));

        notificationService.deleteNotification(1L);

        verify(notificationRepository).delete(notification);
    }

    // @Test
    // void makeBigNotification() throws RentServiceException, NotificationServiceException {
    //     Rent rentMock = mock(Rent.class);

    //     when(rentRepository.findById(anyLong())).thenReturn(Optional.of(rentMock));

    //     Rental rentalMock = mock(Rental.class);
    //     when(rentMock.getRental()).thenReturn(rentalMock);

    //     Notification notification = new Notification("Test Notification", false, false, "big", LocalDateTime.now());

    //     when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

    //     Notification savedNotification = notificationService.makeBigNotification(renter.getEmail(), rentMock.getId());

    //     verify(notificationRepository).save(any(Notification.class));
    //     verify(emailService).sendEmail(eq(owner.getEmail()), anyString(), anyString());
    //     verify(rentMock).getRental();
    // }
        */
    
}
