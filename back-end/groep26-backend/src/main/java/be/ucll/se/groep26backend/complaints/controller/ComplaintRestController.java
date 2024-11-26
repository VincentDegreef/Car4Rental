package be.ucll.se.groep26backend.complaints.controller;

import be.ucll.se.groep26backend.rents.service.RentServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import be.ucll.se.groep26backend.complaints.model.Complaint;
import be.ucll.se.groep26backend.complaints.service.ComplaintService;

@CrossOrigin(origins = {"http://localhost:3000", "https://group26-frontend.azurewebsites.net", "https://group26-ac-frontend.azurewebsites.net", "https://groep26-frontend.azurewebsites.net"})
@RestController
@RequestMapping("/complaints")
public class ComplaintRestController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping("/add/{rentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addComplaint(@PathVariable Long rentId, @RequestBody String complaintMessage) throws RentServiceException {
        complaintService.addComplaint(rentId, complaintMessage);
    }
}
