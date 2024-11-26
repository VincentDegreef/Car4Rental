package be.ucll.se.groep26backend.wallet.service;

public class WalletServiceException extends Exception {
    
    private String field;

    public WalletServiceException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

