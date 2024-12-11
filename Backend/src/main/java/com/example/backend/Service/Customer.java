package com.example.backend.Service;

import com.example.backend.FrontendService.LogService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Customer implements Runnable {

    // Initialize Variables

    private final TicketPool ticketPool;
    private final Long customerID;
    private final int customerRetrievalRate; // Tickets retrieved per second
    private final TicketingService ticketingService;
    private final LogService logService;


    /**
     * Constructor
     * @param customerID
     * @param ticketPool
     * @param customerRetrievalRate
     * @param ticketingService
     * @param logService
     */
    public Customer(Long customerID, TicketPool ticketPool, int customerRetrievalRate, TicketingService ticketingService, LogService logService) {
        this.customerID = customerID;
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketingService = ticketingService;
        this.logService = logService;
    }

    /**
     * Customer run method
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && TicketingService.isRunning()) {
            try {
                ticketPool.removeTicket(customerID); // Retrieve one ticket from the pool
                Thread.sleep(1000/customerRetrievalRate); // Thread Wait based on customer retrieval rate

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("Customer " + customerID + " stopped.");
                logService.addLog("Customer " + customerID + " stopped.");
                break;
            }
        }

    }
}
