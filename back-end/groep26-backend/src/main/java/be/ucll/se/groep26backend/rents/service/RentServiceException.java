package be.ucll.se.groep26backend.rents.service;

public class RentServiceException extends Exception{
    private String field;

    public RentServiceException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
