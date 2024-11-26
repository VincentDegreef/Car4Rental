package be.ucll.se.groep26backend.user.service;

public class UserServiceException extends Exception {
    private String field;
    public UserServiceException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
    
}
