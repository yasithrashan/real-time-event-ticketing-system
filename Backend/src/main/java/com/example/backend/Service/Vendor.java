package com.example.backend.Service;

import java.util.Random;

public class Vendor implements Runnable {

    private final TicketPool ticketPool;
    private final Long vendorID;
    private final int ticketReleaseRate; // Tickets added per second
    private final Random random = new Random();

    // Constructor
    public Vendor(Long vendorID, TicketPool ticketPool, int ticketReleaseRate) {
        this.vendorID = vendorID;
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate; // Dynamically passed
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Generates random tickets
                int ticketsToAdd=random.nextInt(5)+1;

                // Add random number to the ticket pool
                ticketPool.addTicket(vendorID,ticketsToAdd); // Add one ticket to the pool

                Thread.sleep(1000 / ticketReleaseRate); // Wait based on ticket release rate

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorID + " interrupted.");
                break;
            }
        }
    }
}
