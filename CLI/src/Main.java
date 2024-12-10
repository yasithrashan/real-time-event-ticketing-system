import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        mainMenu();
    }

    public static void startSystem() {
        System.out.println("Starting the system");
        TicketingService ticketingService = new TicketingService();
        ticketingService.startSystem();
    }

    public static void stopSystem() {
        System.out.println("Stopping the system");
        TicketingService ticketingService = new TicketingService();
        ticketingService.stopSystem();
    }

    public static void updateConfiguration() {
        Configuration configuration = Configuration.getInstance();
        System.out.println("Updating Configuration:");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Total Number of Tickets: ");

        int totalTickets;
        for(totalTickets = scanner.nextInt(); totalTickets <= 0; totalTickets = scanner.nextInt()) {
            System.out.println("Invalid input. Please enter a positive number.");
            System.out.print("Enter Total Number of Tickets (positive number): ");
        }

        System.out.print("Enter Ticket Release Rate: ");

        int ticketReleaseRate;
        for(ticketReleaseRate = scanner.nextInt(); ticketReleaseRate <= 0; ticketReleaseRate = scanner.nextInt()) {
            System.out.println("Invalid input. Please enter a positive number.");
            System.out.print("Enter Ticket Release Rate (positive number): ");
        }

        System.out.print("Enter Customer Retrieval Rate: ");

        int customerRetrievalRate;
        for(customerRetrievalRate = scanner.nextInt(); customerRetrievalRate <= 0; customerRetrievalRate = scanner.nextInt()) {
            System.out.println("Invalid input. Please enter a positive number.");
            System.out.print("Enter Customer Retrieval Rate (positive number): ");
        }

        System.out.print("Enter Maximum Ticket Capacity: ");

        int maxTicketCapacity;
        for(maxTicketCapacity = scanner.nextInt(); maxTicketCapacity <= 0; maxTicketCapacity = scanner.nextInt()) {
            System.out.println("Invalid input. Please enter a positive number.");
            System.out.print("Enter Maximum Ticket Capacity (positive number): ");
        }

        configuration.setTotalTickets(totalTickets);
        configuration.setTicketReleaseRate(ticketReleaseRate);
        configuration.setCustomerRetrievalRate(customerRetrievalRate);
        configuration.setMaxTicketCapacity(maxTicketCapacity);
        saveConfigurationToFile(configuration);
        System.out.println("Configuration saved to file.");
    }

    public static void retrieveConfiguration() {
        System.out.println("Retrieving Configuration:");
        System.out.println("Total Tickets : " + Configuration.getInstance().getTotalTickets());
        System.out.println("Ticket Release Rate : " + Configuration.getInstance().getTicketReleaseRate());
        System.out.println("Customer Retrieval Rate : " + Configuration.getInstance().getCustomerRetrievalRate());
        System.out.println("Maximum Ticket Capacity : " + Configuration.getInstance().getMaxTicketCapacity());
    }

    public static void exit() {
    }

    public static void saveConfigurationToFile(Configuration configuration) {
        String filePath = "Configuration.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Total Tickets: " + configuration.getTotalTickets() + "\n");
            writer.write("Ticket Release Rate: " + configuration.getTicketReleaseRate() + "\n");
            writer.write("Customer Retrieval Rate: " + configuration.getCustomerRetrievalRate() + "\n");
            writer.write("Maximum Ticket Capacity: " + configuration.getMaxTicketCapacity() + "\n");
            System.out.println("Configuration saved successfully to " + filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the configuration: " + e.getMessage());
        }

    }

    public static void mainMenu(){
        boolean isStarting = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Ticketing System Application!");

        while(!isStarting) {
            System.out.println("....Main Menu....");
            System.out.println("Press 1 to Start the System");
            System.out.println("Press 2 to Stop the System");
            System.out.println("Press 3 to Update Configuration");
            System.out.println("Press 4 to Retrieve Configuration");
            System.out.println("Press 5 to Exit\n");
            System.out.println("Please choose one of the following options:");
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    startSystem();
                    break;
                case 2:
                    stopSystem();
                    break;
                case 3:
                    updateConfiguration();
                    break;
                case 4:
                    retrieveConfiguration();
                    break;
                case 5:
                    System.out.println("Exiting the system...");
                    isStarting = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

    }
}