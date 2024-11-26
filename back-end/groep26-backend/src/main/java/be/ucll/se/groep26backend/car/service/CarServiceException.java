package be.ucll.se.groep26backend.car.service;

public class CarServiceException extends Exception {
    
    private String field;

    public CarServiceException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
