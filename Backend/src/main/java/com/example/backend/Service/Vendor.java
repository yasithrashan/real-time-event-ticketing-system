package com.example.backend.Service;

public class Vendor implements Runnable {

    private final TicketPool ticketPool;
    private final int vendorId;

    public Vendor(TicketPool ticketPool, int vendorId) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {

        while (true) {
            try {
                ticketPool.addTicket(vendorId);
                Thread.sleep(1000/ticketPool.config.getTicketReleaseRate());

            } catch (InterruptedException e) {
                System.out.println("Vendor : "+vendorId + " thread interrupted");
                Thread.currentThread().interrupt();
                return;
            }
        }

    }
}
