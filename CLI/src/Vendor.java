public class Vendor implements Runnable {
    private final int vendorID;
    private final TicketPool ticketPool;
    Configuration configuration = Configuration.getInstance();

    public Vendor(int vendorID, TicketPool ticketPool) {
        this.vendorID = vendorID;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && TicketingService.isRunning()) {
            try {
                ticketPool.addTicket(vendorID); // Add one ticket to the pool
                Thread.sleep(1000 / configuration.getTicketReleaseRate()); // Wait based on ticket release rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorID + " stopped.");
                break;
            }
        }
    }
}
