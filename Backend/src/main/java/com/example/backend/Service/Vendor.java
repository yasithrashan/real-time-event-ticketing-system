package com.example.backend.Service;

import com.example.backend.FrontendService.LogService;
import lombok.extern.slf4j.Slf4j;



@Slf4j
public class Vendor implements Runnable {

    // Initialize Variable

    private final TicketPool ticketPool;
    private final Long vendorID;
    private final int ticketReleaseRate; // Tickets added per second
    private final TicketingService ticketingService;
    private final LogService logService;


    /**
     * Constructor
     * @param vendorID
     * @param ticketPool
     * @param ticketReleaseRate
     * @param ticketingService
     * @param logService
     */
    public Vendor(Long vendorID, TicketPool ticketPool, int ticketReleaseRate, TicketingService ticketingService, LogService logService) {
        this.vendorID = vendorID;
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketingService = ticketingService;
        this.logService = logService;
    }

    /**
     * Vendor run method
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && TicketingService.isRunning()) {
            try {
                ticketPool.addTicket(vendorID); // Add one ticket to the pool
                Thread.sleep(1000/ticketReleaseRate); // Thread Wait based on ticket release rate

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("Vendor " + vendorID + " stopped.");
                logService.addLog("Vendor " + vendorID + " stopped.");
                break;
            }
        }

    }

}
