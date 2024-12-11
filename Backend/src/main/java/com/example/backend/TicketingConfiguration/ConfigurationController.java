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

    /**
     * Constructor
     * @param ticketingService
     */
    public ConfigurationController(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
        // Load the previous configuration from json file
        this.ticketingSystemConfiguration = ConfigurationFileHandler.loadConfiguration();

        // Check Validation
        if (ticketingSystemConfiguration == null) {
            this.ticketingSystemConfiguration = TicketingSystemConfiguration.getInstance(); // Default Configuration
        }

        ticketingService.initializeTicketPool(ticketingSystemConfiguration);
    }



    /**
     * API request to update Ticketing Configuration Parameters
     * @param newConfiguration
     * @return
     */
    @PostMapping
    public ResponseEntity<String> postConfiguration(@Valid @RequestBody TicketingSystemConfiguration newConfiguration) {

        // If user update configuration system auto stopped.
        if (ticketingService.isRunning()) {
            ticketingService.stopSystem(); // stop the system
            System.out.println("System stopped for reconfiguration.");
        }

        // The user input values save to the current configuration
        this.ticketingSystemConfiguration = newConfiguration;
        // save the configuration to the json file
        ConfigurationFileHandler.saveConfiguration(newConfiguration);
        // Pass the values to the Ticketing service class
        ticketingService.initializeTicketPool(newConfiguration);

        return ResponseEntity.ok("Configuration successfully updated!");
    }

    /**
     * API to get current configurations
     * @return
     */
    @GetMapping
    public ResponseEntity<TicketingSystemConfiguration> getConfig() {
        return ResponseEntity.ok(ticketingSystemConfiguration);
    }

    /**
     * API to start the ticketing System
     * @return
     */
    @PostMapping("/start")
    public ResponseEntity<String> startSystem() {
        ticketingService.startSystem(); // Start threads
        return ResponseEntity.ok("System started successfully.");
    }

    /**
     * API to stop the ticketing system
     * @return
     */
    @PostMapping("/stop")
    public ResponseEntity<String> stopSystem() {
        ticketingService.stopSystem(); // Stop threads
        return ResponseEntity.ok("System stopped.");
    }
}