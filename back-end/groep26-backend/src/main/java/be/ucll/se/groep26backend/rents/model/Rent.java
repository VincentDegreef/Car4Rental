package be.ucll.se.groep26backend.rents.model;

import java.time.LocalDate;
import java.time.LocalTime;
import be.ucll.se.groep26backend.rental.model.Rental;
import be.ucll.se.groep26backend.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;


@Entity
@Table(name = "rents")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    @NotBlank(message = "Email is required")
    @Email(message="Email value is invalid, it has to be of the following format xxx@yyy.zzz")
    private String email;
    @Pattern(regexp = "\\d{2}\\.\\d{2}\\.\\d{2}-\\d{3}\\.\\d{2}", message = "Identification number of national register is invalid, it has to be of the following format yy.mm.dd-xxx.zz")
    @NotBlank(message = "Identification number of national register is required")
    private String identification;
    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;
    @NotBlank(message = "Driving licence number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Driving licence number is invalid, it has to be of the following format 0000000000 (where each 0 is a number between 0 and 9)")
    private String drivingLicenceNumber;

    private Boolean isCancelled = false;

    private LocalTime startTime;
    
    //@NotNull(message = "End date is required")
    //@Future(message = "End date is invalid, it has to be in the future")
    private LocalDate endDate;
    
    private LocalTime endTime;

    @Min(0)
    private int startMillage;

    @Min(0)
    private int returnMillage;

    @Min(0)
    private int startFuelQuantity;

    @Min(0)
    private int returnFuelQuantity;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date is invalid, it has to be in the future")
    private LocalDate startDate;
    


    private double totalPrice;


    @OneToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;
    

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Rent() {
    }
    
    public Rent(String phoneNumber, String email, String identification, LocalDate birthDate, String drivingLicenceNumber, LocalDate startDate, int startMillage, int startFuelQuantity) {
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setIdentification(identification);
        setBirthDate(birthDate);
        setDrivingLicenceNumber(drivingLicenceNumber);
        setStartDate(startDate);
        setStartMillage(startMillage);
        setStartFuelQuantity(startFuelQuantity);
        setStartDate(startDate);
        setStartTime(startTime);
        setStartMillage(startMillage);
        setStartFuelQuantity(startFuelQuantity);
    }

    public Rent(String phoneNumber, String email, String identification, LocalDate birthDate, String drivingLicenceNumber, Boolean isCancelled, LocalDate startDate, LocalTime startTime, int startMillage, int startFuelQuantity) {
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setIdentification(identification);
        setBirthDate(birthDate);
        setDrivingLicenceNumber(drivingLicenceNumber);
        setIsCancelled(isCancelled);
    }  
    
    public void setDrivingLicenceNumber(String drivingLicenceNumber) {
        this.drivingLicenceNumber = drivingLicenceNumber;
    }

    public String getDrivingLicenceNumber() {
        return drivingLicenceNumber;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getIdentification() {
        return identification;
    }

    public void setEmail(String email) {
       this.email = email;
    }

    public String getEmail() {
        return email;
    }

    private void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public Rental getRental() {
        return rental;
    }


    public String toString() {
        return "Rent for " + rental.getCar().getBrand() + " " + 
        rental.getCar().getModel() + " " + rental.getCar().getLicensePlate() + " from " + rental.getStartDate() + " until " + rental.getEndDate();
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Long getId() {
        return id;
    }
    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
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

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getStartMillage() {
        return startMillage;
    }

    public void setStartMillage(int startMillage) {
        this.startMillage = startMillage;
    }

    public int getReturnMillage() {
        return returnMillage;
    }

    public void setReturnMillage(int returnMillage) {
        this.returnMillage = returnMillage;
    }

    public int getStartFuelQuantity() {
        return startFuelQuantity;
    }

    public void setStartFuelQuantity(int startFuelQuantity) {
        this.startFuelQuantity = startFuelQuantity;
    }

    public int getReturnFuelQuantity() {
        return returnFuelQuantity;
    }

    public void setReturnFuelQuantity(int returnFuelQuantity) {
        this.returnFuelQuantity = returnFuelQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
