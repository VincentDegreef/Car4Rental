package be.ucll.se.groep26backend.car.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import be.ucll.se.groep26backend.rental.model.Rental;
import be.ucll.se.groep26backend.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    @NotBlank(message = "Brand is required")
    private String brand;
    @NotBlank(message = "Model is required")
    private String model;
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    @NotBlank(message = "Type is required")
    private String type;
    @Positive(message= "Number of seats must be greater than 0")
    private int numberOfSeats;
    @PositiveOrZero(message= "Number of child seats must be greater than or equal to 0")
    private int numberOfChildSeats;
    private boolean foldableRearSeats;
    private boolean towbar;

    

    @OneToMany(mappedBy = "car")
    @JsonIgnore
    private List<Rental> carRentals;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public Car() {}

    public Car(String brand, String model, String licensePlate, String type,int numberOfSeats, int numberOfChildSeats, boolean foldableRearSeats, boolean towbar, User user) {
        setBrand(brand);
        setModel(model);
        setLicensePlate(licensePlate);
        setType(type);
        setNumberOfSeats(numberOfSeats);
        setNumberOfChildSeats(numberOfChildSeats);
        setFoldableRearSeats(foldableRearSeats);
        setTowbar(towbar);
        setUser(user);
    }

    
    public void setTowbar(boolean towbar2) {
        this.towbar = towbar2;
    }

    public void setFoldableRearSeats(boolean foldableRearSeats2) {
        this.foldableRearSeats = foldableRearSeats2;
    }

    public void setNumberOfChildSeats(int numberOfChildSeats2) {
        this.numberOfChildSeats = numberOfChildSeats2;
    }

    public void setNumberOfSeats(int numberOfSeats2) {
        this.numberOfSeats = numberOfSeats2;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public void setLicensePlate(String licensePlate2) {
        this.licensePlate = licensePlate2;
    }

    public void setModel(String model2) {
        this.model = model2;
    }

    public void setBrand(String brand2) {
       this.brand = brand2;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getType() {
        return type;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public int getNumberOfChildSeats() {
        return numberOfChildSeats;
    }

    public boolean isFoldableRearSeats() {
        return foldableRearSeats;
    }

    public boolean isTowbar() {
        return towbar;
    }

    public List<Rental> getCarRentals() {
        return carRentals;
    }

    public void addCarRental(Rental rental) {
        if(carRentals == null){
            carRentals = new ArrayList<>();
        }
        carRentals.add(rental);
    }

    public void setUser(User user) {
        this.owner = user;
    }

    public User getUser() {
        return owner;
    }

    public long getId() {
        return id;
    }
}
