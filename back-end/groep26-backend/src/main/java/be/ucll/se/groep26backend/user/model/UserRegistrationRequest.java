package be.ucll.se.groep26backend.user.model;
// UserRegistrationRequest.java
public class UserRegistrationRequest {
    private User user;
    private String roleName;

    public UserRegistrationRequest(User user, String roleName) {
        this.user = user;
        this.roleName = roleName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}