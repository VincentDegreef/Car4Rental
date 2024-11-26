package be.ucll.se.groep26backend.car.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.se.groep26backend.car.model.Car;
import be.ucll.se.groep26backend.car.repo.CarRepository;
import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarService carService;
    private Car car1;
    private Car car2;
    private Car car3;

    UserService userService;
    private User user1;
    

    @BeforeEach
    public void setup() throws CarServiceException{
        car1 = new Car("Audi", "A4", "1-ABC-123", "Sedan", 5, 2, true, false, user1);
        car2 = new Car("BMW", "X5", "1-DEF-456", "SUV", 5, 2, true, true,user1);
        car3 = new Car("Mercedes", "C-Class", "1-GHI-789", "Sedan", 5, 2, true, false,user1);
    }

    @Test
    public void givenNoCars_whenGetAllCars_thenReturnError() throws CarServiceException {
        // given
        when(carRepository.findAll()).thenReturn(new ArrayList<>());
        // when
        CarServiceException exception = assertThrows(CarServiceException.class, () -> carService.getAllCars());
        // then
        assertEquals("No cars found", exception.getMessage());

    }

    

    @Test

    public void givenCars_whenGetAllCars_thenReturnListOfCars() throws CarServiceException {
        // given
        List<Car> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);
        cars.add(car3);
        when(carRepository.findAll()).thenReturn(cars);
        // when
        List<Car> allCars = carService.getAllCars();
        // then
        assertEquals(3, allCars.size());
    }

}