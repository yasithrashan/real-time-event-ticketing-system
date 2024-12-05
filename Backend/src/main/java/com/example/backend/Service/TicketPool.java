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

    // Constructor initializes from configuration
    public TicketPool(int maxTicketCapacity, int totalTickets) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
    }

    // Add a ticket to the pool (Vendor action)
    public void addTicket(Long vendorID) {
        synchronized (this) {
            // Wait if the pool is full or all tickets are added
            while (currentPoolSize >= maxTicketCapacity || ticketsAdded >= totalTickets) {
                try {
                    if (ticketsAdded >= totalTickets) {
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
            System.out.println("Vendor " + vendorID + " added " + ticket + ". Current Pool Size: " + currentPoolSize);

            notifyAll(); // Notify consumers
        }
    }

    // Remove a ticket from the pool (Customer action)
    public void removeTicket(Long customerID) {
        synchronized (this) {
            // Wait if the pool is empty
            while (currentPoolSize <= 0) {
                try {
                    System.out.println("Ticket Pool is empty. Customer " + customerID + " is waiting...");
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Customer " + customerID + " thread interrupted.");
                }
            }

            // Decrement pool size and retrieve a ticket
            String ticket = ticketList.remove(0);
            currentPoolSize--;
            System.out.println("Customer " + customerID + " bought " + ticket + ". Current Pool Size: " + currentPoolSize);

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
