public class Customer implements Runnable {
    private final int customerID;
    private final TicketPool ticketPool;
    Configuration configuration = Configuration.getInstance();

    public Customer(int customerID, TicketPool ticketPool) {
        this.customerID = customerID;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && TicketingService.isRunning()) {
            try {
                ticketPool.removeTicket(customerID); // Retrieve one ticket from the pool
                Thread.sleep(1000 / configuration.getCustomerRetrievalRate()); // Wait based on customer retrieval rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer " + customerID + " stopped.");
                break;
            }
        }
    }
}
