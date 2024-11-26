package be.ucll.se.groep26backend.rental.service;

public class RentalServiceException extends Exception {
    
    private String field;

    public RentalServiceException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

