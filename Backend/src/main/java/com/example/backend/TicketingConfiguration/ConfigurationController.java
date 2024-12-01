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
            ticketingSystemConfiguration = new TicketingSystemConfiguration(); // Default Configuration
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


        return ResponseEntity.ok("Configuration successfully updated!");
    }

    @GetMapping
    public ResponseEntity<TicketingSystemConfiguration> getConfig() {
        return ResponseEntity.ok(ticketingSystemConfiguration);
    }
}
