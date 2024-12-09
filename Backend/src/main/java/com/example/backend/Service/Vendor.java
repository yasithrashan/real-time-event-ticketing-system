package com.example.backend.Service;

import lombok.extern.slf4j.Slf4j;



@Slf4j
public class Vendor implements Runnable {

    private final TicketPool ticketPool;
    private final Long vendorID;
    private final int ticketReleaseRate; // Tickets added per second
    private final TicketingService ticketingService;



    // Constructor
    public Vendor(Long vendorID, TicketPool ticketPool, int ticketReleaseRate,TicketingService ticketingService) {
        this.vendorID = vendorID;
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate; // Dynamically passed
        this.ticketingService = ticketingService;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && TicketingService.isRunning()) {
            try {
                ticketPool.addTicket(vendorID); // Add one ticket to the pool
                Thread.sleep(1000/ticketReleaseRate); // Wait based on ticket release rate

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("Vendor " + vendorID + " stopped.");
                break;
            }
        }

    }

}
