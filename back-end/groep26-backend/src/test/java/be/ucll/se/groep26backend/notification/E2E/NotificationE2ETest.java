package be.ucll.se.groep26backend.notification.E2E;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import be.ucll.se.groep26backend.email.service.EmailService;
import be.ucll.se.groep26backend.notification.controller.NotificationRestController;
import be.ucll.se.groep26backend.notification.model.Notification;
import be.ucll.se.groep26backend.notification.repo.NotificationRepository;
import be.ucll.se.groep26backend.notification.service.NotificationService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class NotificationE2ETest {

    @InjectMocks
    private NotificationRestController notificationController;

    @Mock
    private NotificationService notificationService;

    @Mock
    private EmailService emailService;

    @Mock
    private NotificationRepository notificationRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    public void testGetAllNotifications() throws Exception {
        List<Notification> notifications = new ArrayList<>();
        
        notifications.add(new Notification("Test message 1", false, false, "tag1", LocalDateTime.now()));
        notifications.add(new Notification("Test message 2", false, false, "tag2", LocalDateTime.now()));

        when(notificationService.getAllNotifications()).thenReturn(notifications);

        mockMvc.perform(MockMvcRequestBuilders.get("/notifications")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(notifications.size()));
    }

    @Test
    public void testAddNotification() throws Exception {

        Notification newNotification = new Notification("Test message", false, false, "tag", LocalDateTime.now());
        
        when(notificationService.addNotification(any(Notification.class))).thenReturn(newNotification);

        mockMvc.perform(MockMvcRequestBuilders.post("/notifications/addNotification")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\": \"Test message\", \"confirmed\": false, \"seen\": false, \"tag\": \"tag\", \"receivedAt\": \"2024-04-22T12:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Test message"));
    }

    // @Test
    // public void testGetNotificationById() throws Exception {

    //     Notification notification = new Notification("Test message", false, false, "tag", LocalDateTime.now());
    //     long notificationId = 1L;
        
    //     when(notificationService.getNotificationById(notificationId)).thenReturn(notification);

    //     mockMvc.perform(MockMvcRequestBuilders.get("/notifications/getNotificationById/{id}", notificationId)
    //             .contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(status().isOk())
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Test message"));
    // }

    @Test
    public void testDeleteNotification() throws Exception {
        when(notificationService.deleteNotification(anyLong())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/notifications/deleteNotification/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void testMakeConfirmNotification() throws Exception {
    
        when(notificationService.makeConfirmNotification(anyLong())).thenReturn(new Notification("Test message", true, false, "small", LocalDateTime.now()));

        mockMvc.perform(MockMvcRequestBuilders.post("/notifications/makeConfirmNotification/{notificationId}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Test message"));
    }

    @Test
    public void testMakeCancelNotification() throws Exception {

        when(notificationService.makeCancelNotification(anyLong())).thenReturn(new Notification("Test message", false, false, "small", LocalDateTime.now()));

        mockMvc.perform(MockMvcRequestBuilders.post("/notifications/makeCancelNotification/{notificationId}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Test message"));
    }

    

    
}