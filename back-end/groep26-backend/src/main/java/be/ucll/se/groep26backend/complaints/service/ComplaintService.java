package be.ucll.se.groep26backend.complaints.service;

import be.ucll.se.groep26backend.rents.service.RentServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import be.ucll.se.groep26backend.complaints.model.Complaint;
import be.ucll.se.groep26backend.complaints.repo.ComplaintRepository;
import be.ucll.se.groep26backend.email.service.EmailService;
import be.ucll.se.groep26backend.notification.model.Notification;
import be.ucll.se.groep26backend.notification.service.NotificationService;
import be.ucll.se.groep26backend.rents.model.Rent;
import be.ucll.se.groep26backend.rents.service.RentService;

import java.time.LocalDateTime;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RentService rentService;

    public void addComplaint(Long rentId, String message) throws RentServiceException {
        Rent rent = rentService.getRentById(rentId);
        Complaint complaint = new Complaint(message, rent);

        // Save complaint to repository
        complaintRepository.save(complaint);

        // Send email to notify about the complaint
        sendEmailForComplaint(rent, complaint);

        // Send notification to notify about the complaint
        notificationService.makeComplaintNotification(rent.getEmail(), rentId, complaint);
    }

    private void sendEmailForComplaint(Rent rent, Complaint complaint) {

        emailService.sendEmail(rent.getEmail(), "Complaint filed", complaint.getMessage());
        emailService.sendEmail(rent.getRental().getEmail(), "Complaint filed", complaint.getMessage());
    }

    private void sendNotificationForComplaint(Rent rent, Complaint complaint) {
        Notification notification = new Notification(complaint.getMessage(), false, false, "Complaint", rent.getId(), LocalDateTime.now());
        notificationService.addNotification(notification);
    }
}
