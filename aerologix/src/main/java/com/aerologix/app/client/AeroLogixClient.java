package com.aerologix.app.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.client.*;

import com.aerologix.app.client.gui.*;

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
        LoginWindow lw = LoginWindow.getInstanceLogin();
        lw.setVisible(true);
    }
}
