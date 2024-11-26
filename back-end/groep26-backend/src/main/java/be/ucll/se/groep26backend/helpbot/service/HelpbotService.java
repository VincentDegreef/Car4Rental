package be.ucll.se.groep26backend.helpbot.service;

import org.springframework.stereotype.Service;

@Service
public class HelpbotService {
    
    public HelpbotService() {}


    public String getCommandInfo(String command) throws HelpbotServiceException{

        String info ="";

        
        if (command.equals("rent")) {
            info = "When an user is logged in, they can see an overview of rentals they rented on the rent overview page.";
        
    }

        else if (command.equals("car")) {
            info = "See the overview of all cars available on the car overview page. If the user has the role Owner, They can add a car on the add car page.";
        }

        else if (command.equals("rental")) {
            info = "See the overwiew of all rentals on the rental overview page. If the user has the role Owner, They can add a rental on the add rental page.";
        }

        else if (command.equals("role")) {
            info = "A Owner has permission to add a car, make rentals and rents. A Renter can rent cars. An Admin can see all the rentals and rents. An Accountant can see the wallet balance of all users.";
        }

        else if (command.equals("wallet")) {
            info = "On the profile page, you can check your wallet balance and increase your wallet balance by adding money to it.";
        }

        else {
            throw new HelpbotServiceException("Command not found", "command");
        }

        return info;
    }
}



