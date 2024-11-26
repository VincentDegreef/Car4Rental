package be.ucll.se.groep26backend.user.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import be.ucll.se.groep26backend.notification.model.Notification;
import be.ucll.se.groep26backend.rents.model.Rent;


import com.fasterxml.jackson.annotation.JsonIgnore;

import be.ucll.se.groep26backend.car.model.Car;
import be.ucll.se.groep26backend.wallet.model.Wallet;
import be.ucll.se.groep26backend.role.model.Role;
import be.ucll.se.groep26backend.user.service.UserServiceException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
@Entity
@Table(name = "users")
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    
    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Password is required.")
    private String password;

    @Email(message = "Email must contain @.")
    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "given_verification_code")
    private int givenVerificationCode;

    private boolean verified;

    @Column(name = "experation_date")
    private LocalDate experationDate;

    private boolean banned = false;


    @OneToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Car> cars;

    
    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "rent_id")
    private Rent rent;


    @OneToMany(mappedBy = "user")
    private List<Rent> rents;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "notifications_users", 
    joinColumns = @JoinColumn(name = "user_id"),
     inverseJoinColumns = @JoinColumn(name = "notification_id"))
    private List<Notification> notifications;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    public User() {
    }
    
    public User(String username, String password, String email, String phoneNumber, Role role) throws UserServiceException {
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setRole(role);
    }

    public User(String email2, String password) throws UserServiceException{
        setEmail(email2);
        setPassword(password);
    }

    private void setPhoneNumber(String phoneNumber2) throws UserServiceException{
        if(!phoneNumber2.isEmpty() && !phoneNumber2.matches("[0-9]{10}")){
            throw new UserServiceException("Phone number must have 10 numbers", "phoneNumber");
        }
        this.phoneNumber = phoneNumber2;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private void setEmail(String email2) {
        this.email = email2;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password2) throws UserServiceException{
        if(!password2.isEmpty() && password2.length() < 8){
            throw new UserServiceException("Password must be at least 8 characters long.", "password");
        }
        this.password = password2;
    }

    public String getPassword() {
        return password;
    }

    private void setUsername(String username2) throws UserServiceException{
        if(!username2.isEmpty() && username2.length() < 5){
            throw new UserServiceException("Username must be at least 5 characters long.", "username");
        }
        this.username = username2;
    }

    public String getUsername() {
        return username;
    }


    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void addCar(Car car) {
        if(cars == null){
            cars = new ArrayList<>();
        }
        cars.add(car);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public void setRent(Rent rent) {
        this.rent = rent;
    }

    public Rent getRent() {
        return rent;
    }

    public List<Rent> getRents() {
        return rents;
    }

    public void addRent(Rent rent) {
        if(rents == null){
            rents = new ArrayList<>();
        }
        rents.add(rent);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void addNotification(Notification notification) {
        if(notifications == null){
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
    }

    public void removeNotification(Notification notification) {
        if(notifications == null){
            notifications = new ArrayList<>();
        }
        notifications.remove(notification);
    }

    public void clearNotifications() {
        if(notifications == null){
            notifications = new ArrayList<>();
        }
        notifications.clear();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setGivenVerificationCode(int givenVerificationCode) {
        this.givenVerificationCode = givenVerificationCode;
    }

    public int getGivenVerificationCode() {
        return givenVerificationCode;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setExperationDate(LocalDate experationDate) {
        this.experationDate = experationDate;
    }

    public LocalDate getExperationDate() {
        return experationDate;
    }
    
    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public boolean isBanned() {
        return banned;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Create a list to hold granted authorities
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add a granted authority for each role assigned to the user
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

        // Return the list of granted authorities
        return authorities;
    }
    
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }    
}