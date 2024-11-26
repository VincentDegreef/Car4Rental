package be.ucll.se.groep26backend.car.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.ucll.se.groep26backend.Auth.JwtUtil;
import be.ucll.se.groep26backend.car.model.Car;
import be.ucll.se.groep26backend.car.repo.CarRepository;
import be.ucll.se.groep26backend.role.model.Role;
import be.ucll.se.groep26backend.role.repo.RoleRepository;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class carIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Role role;

    @BeforeEach
    void setUp() {
        carRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        username = "Jenthe";
        password = "Password";
        email = "jenthe.vanzomeren@student.ucll.be";
        phoneNumber = "0123456789";
        role = new Role(1, "OWNER");

        roleRepository.save(role);
    }

    @Test
    @WithMockUser(username = "Jenthe", roles = {"OWNER"})
    void testRegisterCar() throws Exception {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car = new Car("BMW", "X5", "123ABC", "SUV", 5, 2, true, true, owner);

        mockMvc.perform(post("/users/" + owner.getId() + "/addCar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("BMW")));
    }

    @Test
    void testBrandIsRequired() throws Exception {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car = new Car("", "X5", "123ABC", "SUV", 5, 2, true, true, owner);

        mockMvc.perform(post("/users/" + owner.getId() + "/addCar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Brand is required")));
    }

    @Test
    void testModelIsRequired() throws Exception {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car = new Car("BMW", "", "123ABC", "SUV", 5, 2, true, true, owner);

        mockMvc.perform(post("/users/" + owner.getId() + "/addCar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Model is required")));
    }

    @Test
    void testLicensePlateIsRequired() throws Exception {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car = new Car("BMW", "X5", "", "SUV", 5, 2, true, true, owner);

        mockMvc.perform(post("/users/" + owner.getId() + "/addCar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("License plate is required")));
    }

    @Test
    void testTypeIsRequired() throws Exception {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car = new Car("BMW", "X5", "123ABC", "", 5, 2, true, true, owner);

        mockMvc.perform(post("/users/" + owner.getId() + "/addCar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Type is required")));
    }

    @Test
    void testNumberOfSeatsValidation() throws Exception {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car = new Car("BMW", "X5", "123ABC", "SUV", -1, 2, true, true, owner);

        mockMvc.perform(post("/users/" + owner.getId() + "/addCar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Number of seats must be greater than 0")));
    }

    @Test
    void testNumberOfChildSeatsValidation() throws Exception {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car = new Car("BMW", "X5", "123ABC", "SUV", 5, -1, true, true, owner);

        mockMvc.perform(post("/users/" + owner.getId() + "/addCar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Number of child seats must be greater than or equal to 0")));
    }

    @Test
    @WithMockUser(username = "Jenthe", roles = {"OWNER"})
    void testAddMultipleCars() throws Exception {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car1 = new Car("Mercedes", "C-Class", "789GHI", "Sedan", 5, 0, true, true, owner);
        Car car2 = new Car("Toyota", "Corolla", "101JKL", "Sedan", 5, 2, false, true, owner);

        mockMvc.perform(post("/users/" + owner.getId() + "/addCar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car1)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Mercedes")))
                .andExpect(content().string(containsString("C-Class")));

        mockMvc.perform(post("/users/" + owner.getId() + "/addCar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car2)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Toyota")))
                .andExpect(content().string(containsString("Corolla")));
    }

    
}