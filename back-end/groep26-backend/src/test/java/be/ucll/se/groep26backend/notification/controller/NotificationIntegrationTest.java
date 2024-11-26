package be.ucll.se.groep26backend.notification.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.ucll.se.groep26backend.notification.model.Notification;
import be.ucll.se.groep26backend.notification.repo.NotificationRepository;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;
    

    // @Test
    // void testAddNotification() throws Exception {
    //     Notification notification = new Notification("Test message", false, false, "tag", LocalDateTime.now());

    //     mockMvc.perform(post("/notifications/addNotification")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(notification)))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(jsonPath("$.message", equalTo("Test message")));
    // }

    // @Test
    // void testGetAllNotifications() throws Exception {

    //     Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
    //     SecurityContextHolder.getContext().setAuthentication(authentication);

    //     mockMvc.perform(get("/notifications")
    //             .contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    // }

    // @Test
    // void testGetNotificationById() throws Exception {
        
    //     Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
    //     SecurityContextHolder.getContext().setAuthentication(authentication);

    //     mockMvc.perform(get("/notifications/getNotificationById/{notificationId}", 1L)
    //             .contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(jsonPath("$.id", equalTo(1)));
    // }
    
}

