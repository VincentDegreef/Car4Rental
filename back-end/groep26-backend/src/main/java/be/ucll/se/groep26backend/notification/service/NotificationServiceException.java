package be.ucll.se.groep26backend.notification.service;

public class NotificationServiceException extends Exception {
        
        private String field;
    
        public NotificationServiceException(String message, String field) {
            super(message);
            this.field = field;
        }
    
        public String getField() {
            return field;
        }
    
}
