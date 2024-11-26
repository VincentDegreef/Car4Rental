package be.ucll.se.groep26backend.rental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import be.ucll.se.groep26backend.car.model.Car;
import be.ucll.se.groep26backend.rental.service.RentalServiceException;
import be.ucll.se.groep26backend.rents.model.Rent;


@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @NotNull(message = "Start date is required")


    @Future(message = "Start date is invalid, it has to be in the future")
    private LocalDate startDate;
    
    private LocalTime startTime;
    
    @NotNull(message = "End date is required")
    @Future(message = "End date is invalid, it has to be in the future")
    private LocalDate endDate;
    
    private LocalTime endTime;
    private String street;
    private String number;
    private String postal;

    @NotNull(message = "price is required")
    @Positive(message = "Price must be greater than 0")
    private double price;
    
    @NotBlank(message = "city is required")
    private String city;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @NotBlank(message = "email is required")
    @Email(message = "Email value is invalid, it has to be of the following format xxx@yyy.zzz")
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @JsonIgnore
    @OneToOne(mappedBy = "rental")
    private Rent rent;


    public Rental() throws RentalServiceException{
    }

    public Rental(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String street, String number, String postal, String city, String phoneNumber, String email, double price) throws RentalServiceException {
        setStartDate(startDate);
        setStartTime(startTime);
        setEndDate(endDate);
        setEndTime(endTime);
        setStreet(street);
        setNumber(number);
        setPostal(postal);
        setCity(city);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setPrice(price);
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) throws RentalServiceException {
        // if(!startDate.isAfter(endDate)){
        //     throw new RentalServiceException("Start date must be before the end date", "startDate");
        // }
        this.startDate = startDate;
        
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) throws RentalServiceException{
        if(endDate.isBefore(startDate)){
            throw new RentalServiceException("End date must be after the start date", "endDate");
        }
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   public void setCar(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    public void setRent(Rent rent) {
        this.rent = rent;
    }

    public Rent getRent() {
        return rent;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
