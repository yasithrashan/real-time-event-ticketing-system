import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicketingService {

    public static boolean running=false;
    private final List<Thread> vendorThreads = new ArrayList<>();
    private final List<Thread> customerThreads = new ArrayList<>();


    // Start the ticketing system
    public synchronized void startSystem() {
        if (running) {
            System.out.println("System is already running.");
            return;
        }

        running = true;

        Scanner scanner = new Scanner(System.in);
        TicketPool ticketPool = new TicketPool();

        System.out.println("How many Vendors would you like to use?");
        int vendors = scanner.nextInt();

        System.out.println("How many customers would you like to use?");
        int customers = scanner.nextInt();

        // Start vendor threads
        for (int i = 1; i <= vendors; i++) {
            Vendor vendor = new Vendor(i, ticketPool); // Pass vendor ID and ticketPool
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Start customer threads
        for (int i = 1; i <= customers; i++) {
            Customer customer = new Customer(i, ticketPool); // Pass customer ID and ticketPool
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
            customerThread.start();
        }




        System.out.println("Ticketing system started with " + vendors + " vendors and " + customers + " customers.");

        Main.mainMenu();


    }


    // Stop the ticketing system
    public synchronized void stopSystem() {
        if (!running) {
            System.out.println("System is not running.");
            return;
        }

        running = false;

        // Interrupt all vendor threads
        for (Thread thread : vendorThreads) {
            thread.interrupt();
        }
        vendorThreads.clear();

        // Interrupt all customer threads
        for (Thread thread : customerThreads) {
            thread.interrupt();
        }
        customerThreads.clear();

        System.out.println("Ticketing system stopped.");
    }

    public static boolean isRunning() {
        return running;
    }

}
