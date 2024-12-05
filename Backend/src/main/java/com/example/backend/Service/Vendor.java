package com.example.backend.Service;

public class Vendor implements Runnable {

    private final TicketPool ticketPool;
    private final Long vendorID;
    private final int ticketReleaseRate; // Tickets added per second

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
                ticketPool.addTicket(vendorID); // Add one ticket to the pool
                Thread.sleep(1000 / ticketReleaseRate); // Wait based on ticket release rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorID + " interrupted.");
                break;
            }
        }
    }
}
