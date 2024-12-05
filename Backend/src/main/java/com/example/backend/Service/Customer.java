package com.example.backend.Service;

import java.util.Random;

public class Customer implements Runnable {

    private final TicketPool ticketPool;
    private final Long customerID;
    private final int customerRetrievalRate; // Tickets retrieved per second
    private final Random random = new Random();

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

                // Generate a random number between 1-5
                int ticketsToBuy=random.nextInt(5)+1;

                ticketPool.removeTicket(customerID,ticketsToBuy); // Retrieve one ticket from the pool

                Thread.sleep(1000 / customerRetrievalRate); // Wait based on customer retrieval rate

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer " + customerID + " interrupted.");
                break;
            }
        }
    }
}
