package be.ucll.se.groep26backend.notification.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.se.groep26backend.notification.model.Notification;



public interface NotificationRepository extends JpaRepository<Notification, Long>{
    public Notification findNotificationByRentId(long RentId);

    
}
