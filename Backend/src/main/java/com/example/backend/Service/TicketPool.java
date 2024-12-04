package com.example.backend.Service;

import com.example.backend.TicketingConfiguration.TicketingSystemConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class TicketPool {

    private final List<Long> tickets= Collections.synchronizedList(new ArrayList<>());

    TicketingSystemConfiguration config =TicketingSystemConfiguration.getInstance();
    private final int maxTicketCapacity = config.getMaxTicketCapacity();
    private long ticketCounter = 0;



    public synchronized void addTicket(int vendorId) throws InterruptedException {

        // Wait Statement
        while (tickets.size() >= maxTicketCapacity) {
            System.out.println("Ticket pool is full. Vendor : " + vendorId + " is waiting...");
            wait();
        }

        // Add ticket to the Ticket Pool
        ticketCounter++;
        tickets.add(ticketCounter);
        System.out.println("Vendor "+vendorId+" has been added "+
                ticketCounter + " to the pool");

        notifyAll();

    }

    public synchronized void removeTicket(int customerID) throws InterruptedException {

        // Wait Statement
        while (tickets.isEmpty()) {
            System.out.println("No tickets available. Customer " + customerID + " is waiting...");
            wait();
        }

        //Remove ticket from ticket pool

        Long TicketId = tickets.remove(0);
        System.out.println("Customer "+customerID+" has been purchased ticket " + TicketId + " from the pool");
        notifyAll();

        }
    }


