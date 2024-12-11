package com.example.backend.Service;

import com.example.backend.FrontendService.LogService;
import com.example.backend.TicketingConfiguration.TicketingSystemConfiguration;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketingService {

    // Initialize Variable

    private TicketPool ticketPool;
    private final List<Thread> vendorThreads = new ArrayList<>();
    private final List<Thread> customerThreads = new ArrayList<>();
    private  static volatile boolean running = false; // Check the System Running Status
    private volatile boolean isRunning = false; //  Check the System Running Status



    // Service-level variables to store configuration values
    private int maxTicketCapacity;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private final LogService logService;

    /**
     * Constructor
     * @param logService
     */
    public TicketingService(LogService logService) {
        this.logService = logService;
    }


    /**
     * Initialize the ticket pool and configuration values from ConfigurationController class
     * @param config
     */
    public void initializeTicketPool(TicketingSystemConfiguration config) {

        // Extract the values from the configuration
        this.maxTicketCapacity = config.getMaxTicketCapacity();
        this.totalTickets = config.getTotalTickets();
        this.ticketReleaseRate = config.getTicketReleaseRate();
        this.customerRetrievalRate = config.getCustomerRetrievalRate();

        // Log the initialized values to development purpose
        System.out.println("Initializing TicketPool with:");
        System.out.println("Max Ticket Capacity: " + maxTicketCapacity);
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Ticket Release Rate: " + ticketReleaseRate);
        System.out.println("Customer Retrieval Rate: " + customerRetrievalRate);

        // Create the TicketPool instance with the extracted values
        this.ticketPool = new TicketPool(maxTicketCapacity, totalTickets, ticketReleaseRate, customerRetrievalRate, logService);
    }

    /**
     * Start the Ticketing System
     */
    public synchronized void startSystem() {
        // Check the System is Running
        if (running) {
            System.out.println("System is already running.");
            logService.addLog("System is already running.");
            return;
        }

        // Check the Configuration is null ?
        if (ticketPool == null) {
            System.out.println("TicketPool is not initialized. Please configure the system first.");
            logService.addLog("TicketPool is not initialized. Please configure the system first.");
            return;
        }

        // If the System not running yet, then marked the system running using boolean varibale
        running = true;

        // Start the Vendor Thread with 5 vendors
        for (long i = 1; i <= 5; i++) {
            Vendor vendor = new Vendor(i, ticketPool, ticketReleaseRate,this,logService);
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Start customer Thread with 5 Customers
        for (long i = 1; i <= 5; i++) {
            Customer customer = new Customer(i, ticketPool, customerRetrievalRate,this,logService);
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
            customerThread.start();
        }

        System.out.println("Ticketing system started with 5 vendors and 5 customers.");
        logService.addLog("Ticketing system started.");
    }

    /**
     * Stop the Ticketing system
     */
    public synchronized void stopSystem() {
        // Check the system not running then return
        if (!running) {
            System.out.println("System is not running.");
            logService.addLog("System is not running.");
            return;
        }

        // If the system run then stop the system using boolean variable
        running = false;

        // Interrupt all vendor threads
        for (Thread thread : vendorThreads) {
            thread.interrupt();
        }
        // clear all the Vendor Thread
        vendorThreads.clear();

        // Interrupt all vendor threads
        for (Thread thread : customerThreads) {
            thread.interrupt();
        }
        // Clear all the customer Thread
        customerThreads.clear();

        System.out.println("Ticketing system stopped.");
        logService.addLog("Ticketing system stopped.");
    }

    // Get the current state of the system
    public static boolean isRunning() {
        return running;
    }
}
