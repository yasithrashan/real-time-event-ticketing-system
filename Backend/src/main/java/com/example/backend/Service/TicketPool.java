package com.example.backend.Service;

import com.example.backend.FrontendService.LogService;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j

public class TicketPool {

    private final LogService logService;

    private int currentPoolSize = 0;
    private final int maxTicketCapacity;
    private int ticketsAdded = 0;
    private final int totalTickets;
    private final List<String> ticketList =
            Collections.synchronizedList(new ArrayList<>()); // Holds ticket details
    private boolean isFinished = false;
    private int ticketsAddedThisSecond = 0;
    private final int ticketReleaseRate;
    private int ticketsRetrievedThisSecond = 0;
    private final int customerRetrievalRate;

    /**
     * Constructor to initialize from Configurations
     *
     * @param maxTicketCapacity
     * @param totalTickets
     * @param ticketReleaseRate
     * @param customerRetrievalRate
     * @param logService
     */
    public TicketPool(int maxTicketCapacity, int totalTickets,
                      int ticketReleaseRate, int customerRetrievalRate, LogService logService
    ) {

        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.logService = logService;

        // start a thread to for Rate
        startResetThread();
    }

    /**
     * Thread to reset the tickets added per second
     */
    private void startResetThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Reset every second
                    synchronized (this) {
                        ticketsAddedThisSecond = 0; // Reset the counter
                        ticketsRetrievedThisSecond = 0;
                        notifyAll(); // Notify waiting vendors
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    /**
     *  Add a ticket to the pool for Vendors
     * @param vendorID
     */
    public void addTicket(Long vendorID) {
        synchronized (this) {
            // Wait if the pool is full or all tickets are added ?
            while (currentPoolSize >= maxTicketCapacity ||
                    ticketsAdded >= totalTickets || ticketsAddedThisSecond >= ticketReleaseRate) {
                if(!TicketingService.isRunning()) return;
                if(isFinished)return; // Stop if the process is complete
                try {

                    if (ticketsAdded >= totalTickets) {
                        log.info("All tickets have been added. Vendor " + vendorID + " is waiting...");
                        logService.addLog("All tickets have been added. Vendor " + vendorID + " is waiting...");

                    } else if (ticketsAddedThisSecond >= ticketReleaseRate) {
                        log.info("Ticket release limit reached. Vendor " + vendorID + " is waiting...");
                        logService.addLog("Ticket release limit reached. Vendor " + vendorID + " is waiting...");


                    } else {
                        log.info("Ticket Pool is full. Vendor " + vendorID + " is waiting...");
                        logService.addLog("Ticket Pool is full. Vendor " + vendorID + " is waiting...");

                    }

                    wait(); // wait the Thread

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.info("Vendor " + vendorID + " thread interrupted.");
                    logService.addLog("Vendor " + vendorID + " thread interrupted.");
                }
            }

            // Increment counters and add ticket
            ticketsAdded++;
            ticketsAddedThisSecond++;
            String ticket = "Ticket-" + ticketsAdded;
            ticketList.add(ticket);
            currentPoolSize++;

            log.info("Vendor " + vendorID + " added " +ticket+ " tickets. Current Pool Size: " + currentPoolSize + "/" + maxTicketCapacity);
            logService.addLog("Vendor " + vendorID + " added " + ticket + " tickets. Current Pool Size: " + currentPoolSize + "/" + maxTicketCapacity);

            // Check Vendors added all the tickets
            if (ticketsAdded >= totalTickets) {
                isFinished = true; // marked system finished
            }

            notifyAll(); // Notify to the customers
        }
    }

    /**
     * Remove a ticket from the pool for Customers
     * @param customerID
     */
    public void removeTicket(Long customerID) {
        synchronized (this) {
            // Wait if the pool is empty
            while (currentPoolSize <= 0 || ticketsRetrievedThisSecond >= customerRetrievalRate) {
                if(!TicketingService.isRunning()) return;
                try {
                    if(isFinished)return;
                    if(currentPoolSize <=0) {
                        log.info("Ticket Pool is empty. Customer " + customerID + " is waiting...");
                        logService.addLog("Ticket Pool is empty. Customer " + customerID + " is waiting...");

                    } else if (ticketsRetrievedThisSecond >= ticketReleaseRate) {
                        log.info("Ticket release limit reached. Vendor " + customerID + " is waiting...");
                        logService.addLog("Ticket release limit reached. Vendor " + customerID + " is waiting...");
                    }

                    wait(); // wait the Thread

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.info("Customer " + customerID + " thread interrupted.");
                    logService.addLog("Customer " + customerID + " thread interrupted.");
                }
            }

            String ticket = ticketList.remove(0); // Remove first index from the ticket pool
            currentPoolSize--;
            ticketsRetrievedThisSecond++;

            log.info("Customer " + customerID + " bought " + ticket+ " tickets " +  ". Current Pool Size: " + currentPoolSize+ "/" + maxTicketCapacity);
            logService.addLog("Customer " + customerID + " bought " + ticket + " tickets. Current Pool Size: " + currentPoolSize + "/" + maxTicketCapacity);


            // Check  if the customers bought all the tickets
            if (ticketsAdded >= totalTickets && currentPoolSize <= 0) {
                isFinished = true; // Marked as Customer bought all the tickets
            }

            notifyAll(); // Notify to the vendors
        }
    }

    /**
     * Get current ticket size
     * @return
     */
    public int getCurrentPoolSize() {
        synchronized (this) {
            return currentPoolSize;
        }
    }
}