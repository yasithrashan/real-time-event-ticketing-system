package com.example.backend.TicketingConfiguration;


import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;

public class ConfigurationFileHandler {


    private static final String CONFIGURATION_FILE = "configuration.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Save Configuration files to JSON file
    public static void saveConfiguration(TicketingSystemConfiguration config) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(CONFIGURATION_FILE),config);
            System.out.println("Configuration saved to " + CONFIGURATION_FILE);

        }catch (Exception e){
            System.out.println("Error saving configuration");
        }

    }

    // Load Configuration files from JSON file
    public static TicketingSystemConfiguration loadConfiguration() {
        try{
            return objectMapper.readValue(new File(CONFIGURATION_FILE),TicketingSystemConfiguration.class);

        }catch (Exception e){
            System.out.println("Configuration could not be loaded");
            return null;
        }
    }




}
