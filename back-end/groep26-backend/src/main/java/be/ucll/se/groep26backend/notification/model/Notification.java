package be.ucll.se.groep26backend.notification.model;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @NotBlank(message = "Message is mandatory")
    private String message;

    private Boolean confirmed = false;

    private Boolean seen = false;

    private String tag;

    private Long rentId;

    private LocalDateTime receivedAt;


    public Notification() {}

    public Notification(String message, Boolean confirmed, Boolean seen, String tag, Long rentId, LocalDateTime receivedAt) {
        setMessage(message);
        setConfirmed(confirmed);
        setSeen(seen);
        setTag(tag);
        setRentId(rentId);
        setReceivedAt(receivedAt);
    }

    
    public Notification(String message, Boolean confirmed, Boolean seen, String tag, LocalDateTime receivedAt) {
        setMessage(message);
        setConfirmed(confirmed);
        setSeen(seen);
        setTag(tag);
        setReceivedAt(receivedAt);
    }

    public void setSeen(Boolean seen2) {
        this.seen = seen2;
    }

    public void setConfirmed(Boolean confirmed2) {
        this.confirmed = confirmed2;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public String getTag() {
        return tag;
    }

    public void setRentId(Long rentId2) {
        this.rentId = rentId2;
    }

    public Long getRentId() {
        return rentId;
    }

    public void setReceivedAt(LocalDateTime receivedAt2) {
        this.receivedAt = receivedAt2;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }
}
