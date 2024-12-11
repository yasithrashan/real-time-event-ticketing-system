import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {

    private final List<String> ticketList = Collections.synchronizedList(new ArrayList<>()); // Holds ticket details

    private int currentPoolSize = 0; // Tracks the current size of the ticket pool
    private int ticketsAdded = 0; // Tracks how many tickets have been added to the system
    private boolean isFinished = false;

    Configuration configuration=Configuration.getInstance();


    // Add a ticket to the pool (Vendor action)
    public void addTicket(int vendorID) {
        synchronized (this) {
            // Wait if the pool is full or all tickets are added
            while (currentPoolSize >= configuration.getMaxTicketCapacity() ||
                    ticketsAdded >= configuration.getTotalTickets()) {
                if(!TicketingService.isRunning()) return;
                if(isFinished)return; // Stop if the process is complete
                try {

                    if (ticketsAdded >= configuration.getTotalTickets()) {
                        System.out.println("All tickets have been added. Vendor " + vendorID + " is waiting...");

                    } else {
                        System.out.println("Ticket Pool is full. Vendor " + vendorID + " is waiting...");


                    }

                    wait();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Vendor " + vendorID + " thread interrupted.");





                }
            }

            // Increment counters and add ticket
            ticketsAdded++;
            String ticket = "Ticket-" + ticketsAdded;
            ticketList.add(ticket);
            currentPoolSize++;



            System.out.println("Vendor " + vendorID + " added " +ticket+
                    " tickets. Current Pool Size: " + currentPoolSize +
                    "/" + configuration.getMaxTicketCapacity());



            if (ticketsAdded >= configuration.getTotalTickets()) {
                isFinished = true;
            }

            notifyAll(); // Notify consumers
        }
    }

    // Remove a ticket from the pool (Customer action)
    public void removeTicket(int customerID) {
        synchronized (this) {
            // Wait if the pool is empty
            while (currentPoolSize <= 0 ) {
                if(!TicketingService.isRunning()) return;
                try {
                    if(isFinished)return;
                    if(currentPoolSize <=0) {
                        System.out.println("Ticket Pool is empty. Customer " + customerID + " is waiting...");
                    }

                    wait();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Customer " + customerID + " thread interrupted.");



                }
            }

            String ticket = ticketList.remove(0);
            currentPoolSize--;


            System.out.println("Customer " + customerID + " bought " + ticket+ " tickets " +
                    ". Current Pool Size: " + currentPoolSize+ "/" + configuration.getMaxTicketCapacity());


            if (ticketsAdded >= configuration.getTotalTickets()&& currentPoolSize <= 0) {
                isFinished = true;
            }

            notifyAll(); // Notify vendors
        }
    }




}
