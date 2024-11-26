package be.ucll.se.groep26backend.car.repo;

import org.springframework.data.jpa.repository.JpaRepository;


import be.ucll.se.groep26backend.car.model.Car;
public interface CarRepository extends JpaRepository<Car, Long>{
    public Car findCarById(long id);

    public Car findCarByLicensePlate(String licensePlate);

}
