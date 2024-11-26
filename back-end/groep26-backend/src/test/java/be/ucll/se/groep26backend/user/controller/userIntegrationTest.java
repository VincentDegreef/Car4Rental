package be.ucll.se.groep26backend.user.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.core.Authentication;


import com.fasterxml.jackson.databind.ObjectMapper;

import be.ucll.se.groep26backend.Auth.JwtUtil;
import be.ucll.se.groep26backend.request.model.LoginReq;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;
import be.ucll.se.groep26backend.user.service.UserServiceException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class userIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


//     @Test
//     void testRegisterUser() throws Exception {
//         User user = new User("tester", "test123456", "tester@gmail.com", "0495752246");

//         mockMvc.perform(post("/rest/auth/register")
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string(containsString("tester")));

//     }

//     @Test
//     void testRegisterUserUsernameAlreadyExists() throws Exception {
//         User user = new User("tester", "test123456", "tester@gmail.com", "0495752246");
//         userRepository.save(user);

//         mockMvc.perform(post("/rest/auth/register")
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string(containsString("Username already exists")));
//     }

//     @Test
//     void testRegisterUserEmailAlreadyExists() throws Exception {
//     User existingUser = new User("existinguser", "test123456", "tester@gmail.com", "0495752246");
//     userRepository.save(existingUser);

//     User newUser = new User("tester", "test123456", "tester@gmail.com", "0495752246");

//     mockMvc.perform(post("/rest/auth/register")
//             .contentType("application/json")
//             .content(objectMapper.writeValueAsString(newUser)))
//             .andExpect(status().isBadRequest())
//             .andExpect(content().string(containsString("Email already exists")));
// }


// @Test
//     void testUsernameIsRequired() throws Exception {
//         User user = new User("", "test123456", "tester@gmail.com", "0495752246");

//         mockMvc.perform(post("/rest/auth/register")
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string(containsString("Username is required")));
//     }

//     @Test
//     void testPasswordIsRequired() throws Exception {
//         User user = new User("tester", "", "tester@gmail.com", "0495752246");

//         mockMvc.perform(post("/rest/auth/register")
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string(containsString("Password is required")));

//     }

//     @Test
//     void testEmailIsRequired() throws Exception {
//         User user = new User("tester", "test123456", "", "0495752246");

//         mockMvc.perform(post("/rest/auth/register")
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string(containsString("Email is required")));
//     }

//     @Test
//     void testEmailFormat() throws Exception {
//         User user = new User("tester", "test123456", "invalidemail", "0495752246");

//         mockMvc.perform(post("/rest/auth/register")
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string(containsString("Email must contain @")));
//     }

//     @Test
//     void testPhoneNumberIsRequired() throws Exception {
//         User user = new User("tester", "test123456", "tester@gmail.com", "");

//         mockMvc.perform(post("/rest/auth/register")
//                 .contentType("application/json")
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string(containsString("Phone number is required")));
//     }







//     @Test
//     void testLogin() throws Exception {
//     // Create a LoginReq object
//     LoginReq loginReq = new LoginReq("tester@gmail.com", "test123456");

//     // Mock the authentication manager to return a valid Authentication object
//     Authentication authentication = new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword());
//     when(authenticationManager.authenticate(any())).thenReturn(authentication);

//     // Perform the test
//     mockMvc.perform(post("/rest/auth/login")
//             .contentType("application/json")
//             .content(objectMapper.writeValueAsString(loginReq)))
//             .andExpect(status().isOk())
//             .andExpect(content().string(containsString("token")));
// }

    
}