package be.ucll.se.groep26backend.car.repo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import be.ucll.se.groep26backend.car.model.Car;
import be.ucll.se.groep26backend.car.service.CarServiceException;
import be.ucll.se.groep26backend.role.model.Role;
import be.ucll.se.groep26backend.role.repo.RoleRepository;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.repo.UserRepository;
import be.ucll.se.groep26backend.user.service.UserServiceException;

@DataJpaTest
@Transactional
public class CarRepositoryTest {

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

        username = "Jenthevz";
        password = "Password";
        email = "jenthe.vanzomeren@student.ucll.be";
        phoneNumber = "0123456789";
        role = new Role(1, "OWNER");

        roleRepository.save(role);
    }

    @Test
    void whenSaveCar_thenFindCarById() throws CarServiceException, UserServiceException {
        User user = new User(username, password, email, phoneNumber, role);
        userRepository.save(user);

        Car car = new Car("BMW", "X5", "1-ABC-123", "SUV", 5, 2, true, true, user);
        carRepository.save(car);

        Long carId = car.getId();

        Car foundCar = carRepository.findById(carId).orElseThrow();
        assertNotNull(foundCar);
        assertEquals(car, foundCar);
    }

    @Test
    void whenSaveCar_thenFindCarByLicensePlate() throws CarServiceException, UserServiceException {
        User user = new User(username, password, email, phoneNumber, role);
        userRepository.save(user);

        Car car = new Car("BMW", "X5", "1-ABC-123", "SUV", 5, 2, true, true, user);
        carRepository.save(car);

        String licensePlate = car.getLicensePlate();

        Car foundCar = carRepository.findCarByLicensePlate(licensePlate);
        assertNotNull(foundCar);
        assertEquals(car, foundCar);
    }

    @Test
    void whenGetAllCars_thenReturnAllCars() throws CarServiceException, UserServiceException {
        User user = new User(username, password, email, phoneNumber, role);
        userRepository.save(user);

        Car car1 = new Car("BMW", "X5", "1-ABC-123", "SUV", 5, 2, true, true, user);
        Car car2 = new Car("Audi", "Q7", "2-DEF-456", "SUV", 5, 2, true, true, user);
        carRepository.save(car1);
        carRepository.save(car2);

        List<Car> cars = carRepository.findAll();
        assertEquals(2, cars.size());
    }

    @Test
    void whenDeleteCar_thenCarIsDeleted() throws CarServiceException, UserServiceException {
        User user = new User(username, password, email, phoneNumber, role);
        userRepository.save(user);

        Car car = new Car("BMW", "X5", "1-ABC-123", "SUV", 5, 2, true, true, user);
        carRepository.save(car);

        Long carId = car.getId();

        carRepository.delete(car);

        assertFalse(carRepository.findById(carId).isPresent());
    }

    @Test
    void whenFindCarsByUserId_thenReturnCars() throws CarServiceException, UserServiceException {
        User user = new User(username, password, email, phoneNumber, role);
        userRepository.save(user);

        Car car1 = new Car("BMW", "X5", "1-ABC-123", "SUV", 5, 2, true, true, user);
        Car car2 = new Car("Audi", "Q7", "2-DEF-456", "SUV", 5, 2, true, true, user);
        carRepository.save(car1);
        carRepository.save(car2);

        List<Car> cars = carRepository.findAll();
        cars.removeIf(car -> car.getUser().getId() != user.getId());

        assertEquals(2, cars.size());
    }

    @Test
    void whenUpdateCar_thenCarIsUpdated() throws CarServiceException, UserServiceException {
        User user = new User(username, password, email, phoneNumber, role);
        userRepository.save(user);

        Car car = new Car("BMW", "X5", "1-ABC-123", "SUV", 5, 2, true, true, user);
        carRepository.save(car);

        car.setModel("X6");
        carRepository.save(car);

        Car updatedCar = carRepository.findById(car.getId()).orElseThrow();
        assertEquals("X6", updatedCar.getModel());
    }
    

    @Test
    void whenSaveCarWithNullFields_thenThrowValidationException() throws CarServiceException, UserServiceException {
        User user = new User(username, password, email, phoneNumber, role);
        userRepository.save(user);

        Car car = new Car(null, null, null, null, 0, 0, false, false, user);

        Exception exception = assertThrows(Exception.class, () -> {
            carRepository.save(car);
        });

        assertNotNull(exception);
    }

    
}
