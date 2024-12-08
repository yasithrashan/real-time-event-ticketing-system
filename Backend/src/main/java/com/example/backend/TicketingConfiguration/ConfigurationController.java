package com.example.backend.TicketingConfiguration;

import com.example.backend.Service.TicketingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "http://localhost:3000")
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

        ticketingService.initializeTicketPool(ticketingSystemConfiguration);
    }



    // API to update configuration
    @PostMapping
    public ResponseEntity<String> postConfiguration(@Valid @RequestBody TicketingSystemConfiguration newConfiguration) {

        // Stop the current system
        if (ticketingService.isRunning()) {
            ticketingService.stopSystem();
            System.out.println("System stopped for reconfiguration.");
        }

        // Save to Files
        this.ticketingSystemConfiguration = newConfiguration;
        ConfigurationFileHandler.saveConfiguration(newConfiguration);
        ticketingService.initializeTicketPool(newConfiguration);

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
