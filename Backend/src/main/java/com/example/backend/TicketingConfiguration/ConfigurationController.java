package com.example.backend.TicketingConfiguration;

import com.example.backend.Service.TicketingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class ConfigurationController {

    private final TicketingService ticketingService;
    private TicketingSystemConfiguration ticketingSystemConfiguration;

    // Constructor
    public ConfigurationController(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
        this.ticketingSystemConfiguration = ConfigurationFileHandler.loadConfiguration();

        // Check Validation
        if (ticketingSystemConfiguration == null) {
            this.ticketingSystemConfiguration = TicketingSystemConfiguration.getInstance(); // Default Configuration
        }
    }

    // API to update configuration
    @PostMapping
    public ResponseEntity<String> postConfiguration(@Valid @RequestBody TicketingSystemConfiguration newConfiguration) {
        if (newConfiguration.getMaxTicketCapacity() < newConfiguration.getTotalTickets()) {
            return ResponseEntity.badRequest().body("Max ticket capacity cannot be less than total tickets!");
        }

//        if (newConfiguration.getMaxTicketCapacity() < 1 ||
//                newConfiguration.getTotalTickets() < 1 ||
//                newConfiguration.getTicketReleaseRate() < 1 ||
//                newConfiguration.getCustomerRetrievalRate() < 1) {
//            return ResponseEntity.badRequest().body("All configuration values must be greater than 0!");
//        }

        // Save to Files
        this.ticketingSystemConfiguration = newConfiguration;
        ConfigurationFileHandler.saveConfiguration(newConfiguration);

        ticketingService.initializeTicketPool(newConfiguration);

        // Print the configuration parameters to the terminal
        System.out.println("Configuration updated:");
        System.out.println("Total Tickets: " + newConfiguration.getTotalTickets());
        System.out.println("Ticket Release Rate: " + newConfiguration.getTicketReleaseRate());
        System.out.println("Customer Retrieval Rate: " + newConfiguration.getCustomerRetrievalRate());
        System.out.println("Max Ticket Capacity: " + newConfiguration.getMaxTicketCapacity());

        return ResponseEntity.ok("Configuration successfully updated!");
    }



    // API to get the current configuration
    @GetMapping
    public ResponseEntity<TicketingSystemConfiguration> getConfig() {
        return ResponseEntity.ok(ticketingSystemConfiguration);
    }

    // API to start the ticketing system
    @PostMapping("/start")
    public ResponseEntity<String> startSystem() {
        ticketingService.startSystem(); // Start threads
        return ResponseEntity.ok("System started successfully.");
    }

    // API to stop the ticketing system
    @PostMapping("/stop")
    public ResponseEntity<String> stopSystem() {
        ticketingService.stopSystem(); // Stop threads
        return ResponseEntity.ok("System stopped.");
    }
}
