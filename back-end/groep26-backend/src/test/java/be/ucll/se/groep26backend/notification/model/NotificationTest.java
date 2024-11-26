package be.ucll.se.groep26backend.notification.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.ucll.se.groep26backend.user.service.UserServiceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class NotificationTest {
    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification();
    }

    @Test
    void setMessage() {
        String message = "Test message";
        notification.setMessage(message);
        assertEquals(message, notification.getMessage());
    }

    @Test
    void setConfirmed() {
        notification.setConfirmed(true);
        assertTrue(notification.getConfirmed());
    }

    @Test
    void setSeen() {
        notification.setSeen(true);
        assertTrue(notification.getSeen());
    }

    @Test
    void setTag() {
        String tag = "Test Tag";
        notification.setTag(tag);
        assertEquals(tag, notification.getTag());
    }

    @Test
    void setRentId() {
        Long rentId = 12345L;
        notification.setRentId(rentId);
        assertEquals(rentId, notification.getRentId());
    }

    @Test
    void setReceivedAt() {
        LocalDateTime receivedAt = LocalDateTime.now();
        notification.setReceivedAt(receivedAt);
        assertEquals(receivedAt, notification.getReceivedAt());
    }
}
