package be.ucll.se.groep26backend.helpbot.service;

public class HelpbotServiceException extends Exception {
        
    private String field;

    public HelpbotServiceException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }

}