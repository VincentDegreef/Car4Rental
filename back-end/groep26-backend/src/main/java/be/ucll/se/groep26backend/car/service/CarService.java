package be.ucll.se.groep26backend.car.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.se.groep26backend.car.repo.CarRepository;
import be.ucll.se.groep26backend.rental.model.Rental;
import be.ucll.se.groep26backend.rental.repo.RentalRepository;
import be.ucll.se.groep26backend.car.model.Car;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RentalRepository rentalRepository;

    public CarService(){}
    
    public List<Car> getAllCars() throws CarServiceException{
        
        if (carRepository.findAll().isEmpty()){
            throw new CarServiceException("No cars found", "cars");
        }

        return carRepository.findAll();
    }

    public Car deleteCar(int id) throws CarServiceException{
        Car car = carRepository.findCarById(id);
        if (car == null){
            throw new CarServiceException("Car not found", "id");
        }
        if (car.getCarRentals().size() > 0){
            throw new CarServiceException("Car has rentals", "id");
        }
        carRepository.delete(car);
        return car;}

    public Car getCarById(long id){
        return carRepository.findById(id).orElse(null);
    
    }

    public Car makeRental(Long carId, Rental newRental) throws CarServiceException{
        if(carRepository.findCarById(carId) == null){
            throw new CarServiceException("Car not found", "id");
        }
        Car car = carRepository.findCarById(carId);
        car.addCarRental(newRental);
        newRental.setCar(car);
        rentalRepository.save(newRental);
        return carRepository.save(car);
    }

    public List<Car> getCarsByUserId(long userId) throws CarServiceException{
        List<Car> cars = getAllCars();
        cars.removeIf(car -> car.getUser().getId() != userId);
        return cars;
    }

    public List<Rental> getRentalsByCarId(long carId) throws CarServiceException{
        Car car = carRepository.findCarById(carId);
        if (car == null){
            throw new CarServiceException("Car not found", "id");
        }
        return car.getCarRentals();
    }
}
