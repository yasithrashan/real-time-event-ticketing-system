package com.example.backend.Service;

import com.example.backend.FrontendService.LogStreamingController;
import com.example.backend.TicketingConfiguration.TicketingSystemConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class TicketingService {


    private TicketPool ticketPool; // Dynamically created ticket pool
    private final List<Thread> vendorThreads = new ArrayList<>();
    private final List<Thread> customerThreads = new ArrayList<>();
    private  static volatile boolean running = false; // Tracks the system's state
    private volatile boolean isRunning = false; // Tracks the system's state



    // Service-level variables to store configuration values
    private int maxTicketCapacity;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private final LogStreamingController logStreamingController;

    public TicketingService(LogStreamingController logStreamingController) {
        this.logStreamingController = logStreamingController;
    }


    // Initialize the ticket pool and configuration values
    public void initializeTicketPool(TicketingSystemConfiguration config) {

        

        // Extract and assign values from the configuration
        this.maxTicketCapacity = config.getMaxTicketCapacity();
        this.totalTickets = config.getTotalTickets();
        this.ticketReleaseRate = config.getTicketReleaseRate();
        this.customerRetrievalRate = config.getCustomerRetrievalRate();



        // Log the initialized values
        System.out.println("Initializing TicketPool with:");
        System.out.println("Max Ticket Capacity: " + maxTicketCapacity);
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Ticket Release Rate: " + ticketReleaseRate);
        System.out.println("Customer Retrieval Rate: " + customerRetrievalRate);

        // Create the TicketPool instance with the extracted values
        this.ticketPool = new TicketPool(maxTicketCapacity, totalTickets, ticketReleaseRate, customerRetrievalRate, logStreamingController);
    }

    // Start the ticketing system
    public synchronized void startSystem() {
        if (running) {
            System.out.println("System is already running.");
            return;
        }

        if (ticketPool == null) {
            System.out.println("TicketPool is not initialized. Please configure the system first.");
            return;
        }

        running = true;

        // Start vendor threads
        for (long i = 1; i <= 5; i++) {
            Vendor vendor = new Vendor(i, ticketPool, ticketReleaseRate,this);
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Start customer threads
        for (long i = 1; i <= 5; i++) {
            Customer customer = new Customer(i, ticketPool, customerRetrievalRate,this);
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
            customerThread.start();
        }

        System.out.println("Ticketing system started with 5 vendors and 5 customers.");
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

    // Get the current state of the system
    public static boolean isRunning() {
        return running;
    }
}
