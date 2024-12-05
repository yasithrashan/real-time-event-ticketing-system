package com.example.backend.Service;

public class Customer implements Runnable {

    private final TicketPool ticketPool;
    private final Long customerID;
    private final int customerRetrievalRate; // Tickets retrieved per second

    // Constructor
    public Customer(Long customerID, TicketPool ticketPool, int customerRetrievalRate) {
        this.customerID = customerID;
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate; // Dynamically passed
    }

    @Override
    public void run() {
        while (true) {
            try {
                ticketPool.removeTicket(customerID); // Retrieve one ticket from the pool
                Thread.sleep(1000 / customerRetrievalRate); // Wait based on customer retrieval rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer " + customerID + " interrupted.");
                break;
            }
        }
    }
}
