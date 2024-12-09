package com.example.backend.Service;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Customer implements Runnable {

    private final TicketPool ticketPool;
    private final Long customerID;
    private final int customerRetrievalRate; // Tickets retrieved per second
    private final TicketingService ticketingService;



    // Constructor
    public Customer(Long customerID, TicketPool ticketPool, int customerRetrievalRate, TicketingService ticketingService) {
        this.customerID = customerID;
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate; // Dynamically passed
        this.ticketingService = ticketingService;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && TicketingService.isRunning()) {
            try {

                ticketPool.removeTicket(customerID); // Retrieve one ticket from the pool

                Thread.sleep(1000/customerRetrievalRate); // Wait based on customer retrieval rate

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("Vendor " + customerID + " stopped.");
                break;
            }
        }

    }
}
