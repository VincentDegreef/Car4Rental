package be.ucll.se.groep26backend.helpbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.groep26backend.helpbot.service.HelpbotService;
import be.ucll.se.groep26backend.helpbot.service.HelpbotServiceException;

// @CrossOrigin(origins = "*")
@CrossOrigin(origins = {"http://localhost:3000", "https://group26-frontend.azurewebsites.net", "https://group26-ac-frontend.azurewebsites.net", "https://groep26-frontend.azurewebsites.net"})

@RestController
@RequestMapping("/helpbot")
public class HelpbotRestController {
    
    @Autowired
    private HelpbotService helpbotService;

    public HelpbotRestController() {}

    @GetMapping("/command/{command}")
    public String getCommandInfo(@PathVariable String command) throws HelpbotServiceException{
        try{
            return helpbotService.getCommandInfo(command);
        } catch (HelpbotServiceException e) {
            return e.getMessage();
        }
    }
}
