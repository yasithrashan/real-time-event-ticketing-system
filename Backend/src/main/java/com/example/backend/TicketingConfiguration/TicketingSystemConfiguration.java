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

    // Private constructor to prevent direct instantiation
    private TicketingSystemConfiguration() {
    }

    // Static method to get the single instance
    public static synchronized TicketingSystemConfiguration getInstance() {
        if (instance == null) {
            instance = new TicketingSystemConfiguration();
        }
        return instance;
    }
}
