import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        mainMenu(); // Call the main method
    }

    /**
     * Start the Ticketing system
     */
    public static void startSystem() {
        try {
            System.out.println("Starting the system");
            TicketingService ticketingService = new TicketingService();
            ticketingService.startSystem(); // start the system
        } catch (Exception e) {
            System.out.println("An error occurred while starting the system: " + e.getMessage());
        }
    }

    public static void stopSystem() {
        try {
            System.out.println("Stopping the system");
            TicketingService ticketingService = new TicketingService();
            ticketingService.stopSystem(); // stop the system
        } catch (Exception e) {
            System.out.println("An error occurred while stopping the system: " + e.getMessage());
        }
    }

    /**
     * Update configuration
     */
    public static void updateConfiguration() {
        Configuration configuration = Configuration.getInstance();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Updating Configuration:");

            // Get user positive inputs
            int totalTickets = getPositiveInput(scanner, "Enter Total Number of Tickets: ");
            int ticketReleaseRate = getPositiveInput(scanner, "Enter Ticket Release Rate: ");
            int customerRetrievalRate = getPositiveInput(scanner, "Enter Customer Retrieval Rate: ");
            int maxTicketCapacity = getPositiveInput(scanner, "Enter Maximum Ticket Capacity: ");

            configuration.setTotalTickets(totalTickets);
            configuration.setTicketReleaseRate(ticketReleaseRate);
            configuration.setCustomerRetrievalRate(customerRetrievalRate);
            configuration.setMaxTicketCapacity(maxTicketCapacity);

            // Configuration saved to the txt file
            saveConfigurationToFile(configuration);
            System.out.println("Configuration saved to file.");

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numeric values.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            System.out.println("An error occurred while updating configuration: " + e.getMessage());
        }
    }

    /**
     * Retrieve ticketing parameters
     * using singleton instance
     */
    public static void retrieveConfiguration() {
        try {
            System.out.println("Retrieving Configuration:");
            Configuration configuration = Configuration.getInstance();
            System.out.println("Total Tickets : " + configuration.getTotalTickets());
            System.out.println("Ticket Release Rate : " + configuration.getTicketReleaseRate());
            System.out.println("Customer Retrieval Rate : " + configuration.getCustomerRetrievalRate());
            System.out.println("Maximum Ticket Capacity : " + configuration.getMaxTicketCapacity());
        } catch (Exception e) {
            System.out.println("An error occurred while retrieving configuration: " + e.getMessage());
        }
    }

    /**
     * Create a method to save configuration to txt file
     * Buffered writer
     * @param configuration
     */
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

    /**
     * Main Menu method
     */
    public static void mainMenu() {
        boolean isRunning = true;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Ticketing System Application!");

        while (isRunning) {
            try {
                System.out.println("\n....Main Menu...");
                System.out.println("1. Start the System");
                System.out.println("2. Stop the System");
                System.out.println("3. Update Configuration");
                System.out.println("4. Retrieve Configuration");
                System.out.println("5. Exit\n");
                System.out.print("Please choose one of the following options: \n");

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
                        isRunning = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        scanner.close(); // close the scanner object
    }

    /**
     * Create a method for get positive values
     * using scanner
     * @param scanner
     * @param prompt
     * @return input
     */
    private static int getPositiveInput(Scanner scanner, String prompt) {
        int input = -1;

        while (input <= 0) {
            try {
                System.out.print(prompt);
                input = scanner.nextInt();

                if (input <= 0) {
                    System.out.println("Invalid input. Please enter a positive number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.nextLine(); // Clear invalid input
            }
        }
        return input;
    }
}
