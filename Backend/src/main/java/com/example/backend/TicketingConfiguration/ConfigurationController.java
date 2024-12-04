package com.example.backend.TicketingConfiguration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/config")
public class ConfigurationController {

    public TicketingSystemConfiguration ticketingSystemConfiguration;

    public ConfigurationController() {
        ticketingSystemConfiguration = ConfigurationFileHandler.loadConfiguration();

        // Check Validation
        if (ticketingSystemConfiguration == null) {
            ticketingSystemConfiguration = TicketingSystemConfiguration.getInstance(); // Default Configuration

        }
    }

    @PostMapping
    public ResponseEntity<String> postConfiguration(@RequestBody TicketingSystemConfiguration newConfiguration) {

        if (newConfiguration.getMaxTicketCapacity() < newConfiguration.getTotalTickets()) {
            return ResponseEntity.badRequest().body("Max ticket capacity cannot be less than total tickets!");
        }

        // Save to Files

        this.ticketingSystemConfiguration = newConfiguration;
        ConfigurationFileHandler.saveConfiguration(newConfiguration);

        // Print the configuration parameters to the terminal
        System.out.println("Configuration updated:");
        System.out.println("Total Tickets: " + newConfiguration.getTotalTickets());
        System.out.println("Ticket Release Rate: " + newConfiguration.getTicketReleaseRate());
        System.out.println("Customer Retrieval Rate: " + newConfiguration.getCustomerRetrievalRate());
        System.out.println("Max Ticket Capacity: " + newConfiguration.getMaxTicketCapacity());


        return ResponseEntity.ok("Configuration successfully updated!");
    }

    @GetMapping
    public ResponseEntity<TicketingSystemConfiguration> getConfig() {
        return ResponseEntity.ok(ticketingSystemConfiguration);
    }

}
