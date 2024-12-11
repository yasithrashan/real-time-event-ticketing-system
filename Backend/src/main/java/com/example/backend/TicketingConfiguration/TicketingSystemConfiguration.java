package com.example.backend.TicketingConfiguration;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketingSystemConfiguration {


    // Configuration Parameters
    @Min(value = 1)
    private int totalTickets;

    @Min(value = 1)
    private int ticketReleaseRate;

    @Min(value = 1)
    private int customerRetrievalRate;

    @Min(value = 1)
    private int maxTicketCapacity;

    // Singleton instance
    private static TicketingSystemConfiguration instance;

    /**
     * Private Constructor
     */
    private TicketingSystemConfiguration() {
    }

    /**
     * Static method for return Configuration Instance
     * @return
     */
    public static synchronized TicketingSystemConfiguration getInstance() {
        if (instance == null) {
            instance = new TicketingSystemConfiguration();
        }
        return instance;
    }
}
