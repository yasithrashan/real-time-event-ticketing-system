public class Customer implements Runnable {
    // Define Instance Variable
    private final int customerID;
    private final TicketPool ticketPool;
    Configuration configuration = Configuration.getInstance();

    /**
     * Constructor
     * @param customerID
     * @param ticketPool
     */
    public Customer(int customerID, TicketPool ticketPool) {
        this.customerID = customerID;
        this.ticketPool = ticketPool;
    }

    /**
     * Customer Thread to Run
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && TicketingService.isRunning()) {
            try {
                ticketPool.removeTicket(customerID); // Retrieve one ticket from the pool
                Thread.sleep(1000 / configuration.getCustomerRetrievalRate()); // Wait for customer retrieval rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // if thread interrupted
                System.out.println("Customer " + customerID + " stopped.");
                break;
            }
        }
    }
}
