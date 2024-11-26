package be.ucll.se.groep26backend.car.controller;

import be.ucll.se.groep26backend.car.model.Car;
import be.ucll.se.groep26backend.car.repo.CarRepository;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;
import be.ucll.se.groep26backend.user.service.UserServiceException;
import be.ucll.se.groep26backend.role.model.Role;
import be.ucll.se.groep26backend.role.repo.RoleRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class carE2ETest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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

    @AfterEach
    public void cleanup() {
        carRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "Jenthe", roles = {"OWNER"})
    public void testCreateCar() throws UserServiceException {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car = new Car("Toyota", "Camry", "ABC123", "Sedan", 5, 0, true, false, owner);

        client.post()
                .uri("/users/" + owner.getId() + "/addCar")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(car)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.brand").isEqualTo("Toyota")
                .jsonPath("$.model").isEqualTo("Camry")
                .jsonPath("$.licensePlate").isEqualTo("ABC123")
                .jsonPath("$.type").isEqualTo("Sedan")
                .jsonPath("$.numberOfSeats").isEqualTo(5)
                .jsonPath("$.numberOfChildSeats").isEqualTo(0)
                .jsonPath("$.foldableRearSeats").isEqualTo(true)
                .jsonPath("$.towbar").isEqualTo(false);
    }

    @Test
    @WithMockUser(username = "Jenthe", roles = {"OWNER"})
    public void testGetCarById() throws UserServiceException {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car = new Car("Toyota", "Camry", "ABC123", "Sedan", 5, 0, true, false, owner);
        Car savedCar = carRepository.save(car);

        client.get()
                .uri("/cars/getCar/" + savedCar.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedCar.getId())
                .jsonPath("$.brand").isEqualTo("Toyota")
                .jsonPath("$.model").isEqualTo("Camry");
    }


    @Test
    @WithMockUser(username = "Jenthe", roles = {"OWNER"})
    public void testDeleteCar() throws UserServiceException {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car = new Car("Toyota", "Camry", "ABC123", "Sedan", 5, 0, true, false, owner);
        Car savedCar = carRepository.save(car);

        client.delete()
                .uri("/cars/delete/" + savedCar.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser(username = "Jenthe", roles = {"OWNER"})
    public void testGetAllCarsForUser() throws UserServiceException {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car1 = new Car("Toyota", "Camry", "ABC123", "Sedan", 5, 0, true, false, owner);
        Car car2 = new Car("Honda", "Civic", "XYZ789", "Coupe", 4, 1, false, true, owner);

        carRepository.save(car1);
        carRepository.save(car2);

        client.get()
                .uri("/cars/getByUserId/" + owner.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].brand").isEqualTo("Toyota")
                .jsonPath("$[1].brand").isEqualTo("Honda");
    }

    
    @Test
    @WithMockUser(username = "Jenthe", roles = {"OWNER"})
    public void testGetRentalsByCarId() throws UserServiceException {
        User owner = new User(username, password, email, phoneNumber, role);
        userRepository.save(owner);

        Car car = new Car("Toyota", "Camry", "ABC123", "Sedan", 5, 0, true, false, owner);
        Car savedCar = carRepository.save(car);

        client.get()
                .uri("/cars/getRentalsByCarId/" + savedCar.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isNotEmpty();
    }

}
