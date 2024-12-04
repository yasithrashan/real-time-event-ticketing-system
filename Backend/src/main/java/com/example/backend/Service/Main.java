package com.example.backend.Service;

import com.example.backend.TicketingConfiguration.TicketingSystemConfiguration;

public class Main {

    public static void main(String[] args) {

        TicketingSystemConfiguration config=TicketingSystemConfiguration.getInstance();

        // Initialize TicketPool
        TicketPool ticketPool=new TicketPool();

        // Create and start Vendor threads
        Thread vendorThread1 = new Thread(new Vendor(ticketPool, 1));
        Thread vendorThread2 = new Thread(new Vendor(ticketPool, 2));
        Thread vendorThread3 = new Thread(new Vendor(ticketPool, 3));
        Thread vendorThread4 = new Thread(new Vendor(ticketPool, 4));
        Thread vendorThread5 = new Thread(new Vendor(ticketPool, 5));

        vendorThread1.start();
        vendorThread2.start();
        vendorThread3.start();
        vendorThread4.start();
        vendorThread5.start();


        // Create and start Customer threads
        Thread customerThread1 = new Thread(new Customer(ticketPool, 1));
        Thread customerThread2 = new Thread(new Customer(ticketPool, 2));
        Thread customerThread3 = new Thread(new Customer(ticketPool, 3));
        Thread customerThread4 = new Thread(new Customer(ticketPool, 4));
        Thread customerThread5 = new Thread(new Customer(ticketPool, 5));

        customerThread1.start();
        customerThread2.start();
        customerThread3.start();
        customerThread4.start();
        customerThread5.start();

    }









}
