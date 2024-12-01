package com.example.backend.TicketingConfiguration;



import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
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

}


