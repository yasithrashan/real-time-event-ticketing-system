import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicketingService {

    private static boolean running = false;
    private final List<Thread> vendorThreads = new ArrayList<>();
    private final List<Thread> customerThreads = new ArrayList<>();

    // Start the ticketing system
    public synchronized void startSystem() {
        if (running) {
            System.out.println("System is already running.");
            return;
        }

        Configuration configuration = Configuration.getInstance();
        if (configuration == null || !configuration.isValid()) {
            System.out.println("Configuration is not set. Please enter configuration first.");
            return;
        }

        running = true;
        Scanner scanner = new Scanner(System.in);
        TicketPool ticketPool = new TicketPool();

        int vendors = getPositiveInput(scanner, "How many vendors would you like to use?");
        int customers = getPositiveInput(scanner, "How many customers would you like to use?");

        // Start vendor threads
        for (int i = 1; i <= vendors; i++) {
            Vendor vendor = new Vendor(i, ticketPool);
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Start customer threads
        for (int i = 1; i <= customers; i++) {
            Customer customer = new Customer(i, ticketPool);
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
            customerThread.start();
        }

        System.out.println("Ticketing system started with " + vendors + " vendors and " + customers + " customers.");
    }

    // Stop the ticketing system
    public synchronized void stopSystem() {
        if (!running) {
            System.out.println("System is not running.");
            return;
        }

        running = false;

        // Interrupt vendor threads
        for (Thread thread : vendorThreads) {
            thread.interrupt();
        }
        vendorThreads.clear();

        // Interrupt customer threads
        for (Thread thread : customerThreads) {
            thread.interrupt();
        }
        customerThreads.clear();

        System.out.println("Ticketing system stopped.");
    }

    private int getPositiveInput(Scanner scanner, String message) {
        int input = -1;
        while (input <= 0) {
            try {
                System.out.println(message);
                input = scanner.nextInt();
                if (input <= 0) {
                    System.out.println("Please enter a positive number.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.nextLine(); // Clear invalid input
            }
        }
        return input;
    }

    public static boolean isRunning() {
        return running;
    }
}
