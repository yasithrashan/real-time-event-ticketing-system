package com.example.backend.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {

    private int currentPoolSize = 0; // Tracks the current size of the ticket pool
    private final int maxTicketCapacity; // Maximum tickets allowed in the pool
    private int ticketsAdded = 0; // Tracks how many tickets have been added to the system
    private final int totalTickets; // Total tickets allowed to be added to the system
    private final List<String> ticketList = Collections.synchronizedList(new ArrayList<>()); // Holds ticket details
    private boolean isFinished = false;
    private int ticketsAddedThisSecond = 0; // Tracks tickets added in the current second
    private final int ticketReleaseRate; // Maximum tickets all vendors can add per second
    private int ticketsRetrievedThisSecond = 0; // Tracks tickets retrieved in the current second
    private final int customerRetrievalRate; // Maximum tickets all customers can retrieve per second


    // Constructor initializes from configuration
    public TicketPool(int maxTicketCapacity, int totalTickets,int ticketReleaseRate,int customerRetrievalRate) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        // start a thread to reset

        startResetThread();
    }



    // Thread to reset the tickets added per second
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


    // Add a ticket to the pool (Vendor action)
    public void addTicket(Long vendorID) {
        synchronized (this) {
            // Wait if the pool is full or all tickets are added
            while (currentPoolSize >= maxTicketCapacity ||
                    ticketsAdded >= totalTickets || ticketsAddedThisSecond >= ticketReleaseRate) {
                if(isFinished)return; // Stop if the process is complete
                try {
                    if (ticketsAdded >= totalTickets) {
                        System.out.println("All tickets have been added. Vendor " + vendorID + " is waiting...");
                    } else if (ticketsAddedThisSecond >= ticketReleaseRate) {
                        System.out.println("Ticket release limit reached. Vendor " + vendorID + " is waiting...");
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
                ticketsAddedThisSecond++;
                String ticket = "Ticket-" + ticketsAdded;
                ticketList.add(ticket);
                currentPoolSize++;



            System.out.println("Vendor " + vendorID + " added " +ticket+ " tickets. Current Pool Size: " + currentPoolSize + "/" + maxTicketCapacity);

            if (ticketsAdded >= totalTickets) {
                isFinished = true;
            }

            notifyAll(); // Notify consumers
        }
    }

    // Remove a ticket from the pool (Customer action)
    public void removeTicket(Long customerID) {
        synchronized (this) {
            // Wait if the pool is empty
            while (currentPoolSize <= 0 || ticketsRetrievedThisSecond >= customerRetrievalRate) {
                try {
                    if(isFinished)return;
                    if(currentPoolSize <=0) {
                        System.out.println("Ticket Pool is empty. Customer " + customerID + " is waiting...");
                    } else if (ticketsRetrievedThisSecond >= ticketReleaseRate) {
                        System.out.println("Ticket release limit reached. Vendor " + customerID + " is waiting...");
                    }

                    wait();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Customer " + customerID + " thread interrupted.");
                }
            }

                String ticket = ticketList.remove(0);
                currentPoolSize--;
                ticketsRetrievedThisSecond++;

            System.out.println("Customer " + customerID + " bought " + ticket+ " tickets " +  ". Current Pool Size: " + currentPoolSize+ "/" + maxTicketCapacity);

            if (ticketsAdded >= totalTickets && currentPoolSize <= 0) {
                isFinished = true;
            }

            notifyAll(); // Notify vendors
        }
    }

    // Get the current size of the ticket pool
    public int getCurrentPoolSize() {
        synchronized (this) {
            return currentPoolSize;
        }
    }
}
