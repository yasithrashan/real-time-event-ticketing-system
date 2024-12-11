public class Configuration {

    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    // Singleton instance
    private static Configuration instance;

    /**
     * Make the Constructor Private
     * Restriction for create object
     */
    private Configuration(){}

    /**
     * Make the Constructor for Private Configurations
     * @return instance
     * Synchronized the method for safety
     */
    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    /**
     * Add validation for check configuration is update ?
     * @return
     */
    public boolean isValid() {
        return totalTickets > 0 && ticketReleaseRate > 0 &&
                customerRetrievalRate > 0 && maxTicketCapacity > 0;
    }

    // Getters and setters for configuration parameters

    /**
     * Getters for totalTickets
     * @return totalTickets
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Setters for totalTickets
     * @param totalTickets
     */
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    /**
     * Getters for ticketReleaseRate
     * @return ticketReleaseRate
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * Setters for ticketReleaseRate
     * @param ticketReleaseRate
     */
    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    /**
     * Getters for customerRetrievalRate
     * @return customerRetrievalRate
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    /**
     * Setters for customerRetrievalRate
     * @param customerRetrievalRate
     */
    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    /**
     * Getter for maxTicketCapacity
     * @return maxTicketCapacity
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    /**
     * Setters for maxTicketCapacity
     * @param maxTicketCapacity
     */
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }
}
