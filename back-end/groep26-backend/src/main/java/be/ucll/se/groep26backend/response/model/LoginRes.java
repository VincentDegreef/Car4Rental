package be.ucll.se.groep26backend.response.model;

import be.ucll.se.groep26backend.role.model.Role;

public class LoginRes {
    private long id;
    private String email;
    private String token;
    private String username;
    private String phoneNumber;
    private Role role;
    private double balance;


    public LoginRes() {}

    public LoginRes(long id, String email, String token, String username, String phoneNumber, Role role, double balance) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.balance = balance;
    }
    public LoginRes(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    private void setRole(Role role) {
        this.role = role;
    }

    private void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }
}
