package be.ucll.se.groep26backend.car.bdd.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import be.ucll.se.groep26backend.Groep26BackendApplication;
import be.ucll.se.groep26backend.car.model.Car;
import be.ucll.se.groep26backend.car.repo.CarRepository;
import be.ucll.se.groep26backend.role.model.Role;
import be.ucll.se.groep26backend.role.repo.RoleRepository;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;
import be.ucll.se.groep26backend.user.service.UserServiceException;
import be.ucll.se.groep26backend.wallet.model.Wallet;
import be.ucll.se.groep26backend.wallet.repo.WalletRepository;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@CucumberContextConfiguration
@SpringBootTest(classes = Groep26BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

public class CarSteps {
    @Autowired
    private WebTestClient client;

    @Autowired
    private CarRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private WebTestClient.ResponseSpec response;

    private String jwtToken = null;

    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Role role;
    private String hashedPassword;

    private Wallet wallet;
    private Long id;

    @Before
    public void setUp() throws UserServiceException{
        
        username = "Jenthe";
        password = "Password";
        hashedPassword = passwordEncoder.encode(password);
        email = "jenthe.vanzomeren@student.ucll.be";
        phoneNumber = "0123456789";

    
        
        role = new Role(1, "OWNER");
        roleRepository.save(role);

        wallet = new Wallet(0);
        walletRepository.save(wallet);
        
        
        loginAndGetToken();
    }

    @After
    public void tearDown() {
        repository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        walletRepository.deleteAll();
    }

    private User loginAndGetToken() throws UserServiceException {
        User owner = new User(username, hashedPassword, email, phoneNumber, role);
        userRepository.save(owner);

        owner.setWallet(wallet);
        userRepository.save(owner);

        WebTestClient.ResponseSpec loginResponse = client.post()
            .uri("/rest/auth/login")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue("{\"email\": \"" + email + "\", \"password\" : \"" + password + "\"}")
            .exchange()
            .expectStatus().isOk();

        jwtToken = loginResponse.returnResult(String.class).getResponseBody().blockFirst();
        JsonObject jsonObject = JsonParser.parseString(jwtToken).getAsJsonObject();
        id = jsonObject.get("id").getAsLong();
        jwtToken = jsonObject.get("token").getAsString();

        return owner;
    }

    @Given("I have no cars")
    public void i_have_no_cars() {
        repository.deleteAll();
    }

    @When("I get all cars")
    public void i_get_all_cars() {
        response = client.get()
            .uri("/cars")
            .header("Authorization", "Bearer " + jwtToken)
            .exchange()
            .expectStatus()
            .isBadRequest();
    }

    @Then("I should get an empty list")
    public void i_should_get_an_empty_list() {
        response.expectBodyList(Car.class).equals(null);
    }

    //Get car when cars are there
    @Given("I have cars")
    public void i_have_cars() {
        repository.deleteAll();
        Car car = new Car("BMW", "X5", "123ABC", "SUV", 5, 2, true, true, null);
        repository.save(car);

        Car car2 = new Car("Audi", "A3", "456DEF", "Sedan", 5, 2, true, true, null);
        repository.save(car2);
    }

    @When("I get all cars2")
    public void i_get_all_cars2() {
        response = client.get()
            .uri("/cars")
            .header("Authorization", "Bearer " + jwtToken)
            .exchange()
            .expectStatus()
            .isOk();
    }

    @Then("I should get a list of cars")
    public void i_should_get_a_list_of_cars() {
        response.expectBodyList(Car.class).value(cars -> {
            assertEquals(2, cars.size());
            Car car1 = cars.get(0);
            Car car2 = cars.get(1);

            assertTrue(car1.getBrand().equals("BMW"));
            assertTrue(car2.getBrand().equals("Audi"));
        });
    }

    //Add car
    @Given("No cars")
    public void no_cars() {
        repository.deleteAll();
    }

    @When("I add a car")
    public void i_add_a_car() {
        Car car = new Car("BMW", "X5", "123ABC", "SUV", 5, 2, true, true, null); // Assuming loggedInUser is correctly set up
    
        response = client.post()
            .uri(uriBuilder -> uriBuilder.path("/users/{id}/addCar").build(id))
            .header("Authorization", "Bearer " + jwtToken)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(car)
            .exchange()
            .expectStatus()
            .isOk();
    }
    

    @Then("The car should be added")
    public void the_car_should_be_added() {
        response.expectBody(Car.class).value(car -> {
            assertTrue(car.getBrand().equals("BMW"));
        });
    }

    //Delete car

    @Given("I have a car")
    public void i_have_a_car() {
        repository.deleteAll();
        Car car = new Car("BMW", "X5", "123ABC", "SUV", 5, 2, true, true, null);
        repository.save(car);
    }

    @When("I delete a car")
    public void i_delete_a_car() {
        Car car = repository.findAll().get(0);
        response = client.delete()
            .uri(uriBuilder -> uriBuilder.path("/cars/delete/{carId}").build(car.getId()))
            .header("Authorization", "Bearer " + jwtToken)
            .exchange()
            .expectStatus()
            .isOk();
    }

    @Then("The car should be deleted")
    public void the_car_should_be_deleted() {
        response.expectBodyList(Car.class).equals(null);

    }

}
