package com.example.backend.Service;

public class Customer implements Runnable {

    private final TicketPool ticketPool;
    private final int CustomerID;

    public Customer(TicketPool ticketPool, int customerID) {
        this.ticketPool = ticketPool;
        CustomerID = customerID;
    }


    @Override
    public void run() {
        while (true) {
            try {
                ticketPool.removeTicket(CustomerID);
                Thread.sleep(1000/ticketPool.config.getCustomerRetrievalRate());
            } catch (InterruptedException e) {
                System.out.println("Customer : " + CustomerID+ " thread interrupted");
                Thread.currentThread().interrupt();
                return;

            }

        }

    }
}
