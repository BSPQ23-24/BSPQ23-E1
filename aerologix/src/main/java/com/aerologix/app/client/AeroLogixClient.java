package com.aerologix.app.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.client.*;

import com.aerologix.app.client.controller.PassengerController;
import com.aerologix.app.client.gui.*;
import com.aerologix.app.server.pojo.PassengerData;

public class AeroLogixClient {
    
    protected static final Logger logger = LogManager.getLogger();

    private static AeroLogixClient instance;

    private Client client;
    private WebTarget webTarget;

    private AeroLogixClient() {
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target(String.format("http://%s:%s/rest/aerologix", System.getProperty("aerologix.hostname"), System.getProperty("aerologix.port")));
    }

    public static AeroLogixClient getInstance() {
        if (instance == null) {
            instance = new AeroLogixClient();
        }
        return instance;
    }

    public WebTarget getWebTarget() {
        return this.webTarget;
    }
    
    // Main
    public static void main(String[] args) {
        if (args.length != 2) {
            logger.info("Use: java Client.Client [host] [port]");
            System.exit(0);
        }

        String hostname = args[0];
        String port = args[1];

        // DO NOT REMOVE THESE TWO LINES
        System.setProperty("aerologix.hostname", hostname);
        System.setProperty("aerologix.port", port);

        // Login Window
        //LoginWindow lw = LoginWindow.getInstanceLogin();
        //lw.setVisible(true);

        // Create passengers
        PassengerData pd1 = new PassengerData();
        pd1.setDNI("787967832K");
        pd1.setEmail("passenger@mail.com");
        pd1.setName("Pepe Rodriguez");
        pd1.setNationality("Spain");
        pd1.setBirthdate(515289600000L);
        pd1.setPhone(767857676);
        PassengerController.getInstance().createPassenger(pd1);

        PassengerData pd2 = new PassengerData();
        pd2.setDNI("787987762L");
        pd2.setEmail("passenger2@mail.com");
        pd2.setName("Marta Sanchez");
        pd2.setNationality("Spain");
        pd2.setBirthdate(839808000000L);
        pd2.setPhone(657856767);
        PassengerController.getInstance().createPassenger(pd2);

        // Get all passengers
        System.out.println(PassengerController.getInstance().getAllPassengers());

        // Get first passenger
        System.out.println(PassengerController.getInstance().getPassenger("787967832K"));

        // Modify passenger
        pd2.setEmail("martasanchez@gmail.com");
        PassengerController.getInstance().modifyPassenger(pd2);

        // Delete passenger
        PassengerController.getInstance().deletePassenger("787967832K");
    }
}
