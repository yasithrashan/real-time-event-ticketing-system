public class Vendor implements Runnable {
    // Instance Variable
    private final int vendorID;
    private final TicketPool ticketPool;
    Configuration configuration = Configuration.getInstance();

    /**
     * Constructor
     * @param vendorID
     * @param ticketPool
     */
    public Vendor(int vendorID, TicketPool ticketPool) {
        this.vendorID = vendorID;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && TicketingService.isRunning()) {
            try {
                ticketPool.addTicket(vendorID); // Add one ticket to the pool
                Thread.sleep(1000 / configuration.getTicketReleaseRate()); // Wait for ticket release rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // if thread interrupted
                System.out.println("Vendor " + vendorID + " stopped.");
                break;
            }
        }
    }
}
