package com.aerologix.app.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;

import com.aerologix.app.client.gui.LoginWindow;
import com.aerologix.app.server.pojo.*;

public class AeroLogixClient {
    
    protected static final Logger logger = LogManager.getLogger();

    private Client client;
    private WebTarget webTarget;

    public AeroLogixClient(String hostname, String port) {
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target(String.format("http://%s:%s/rest/aerologix", hostname, port));
    }

    public int registerUser(String email, String password, String userType, String name) {
        WebTarget registerUserWebTarget = webTarget.path("/user/register");
        Invocation.Builder invocationBuilder = registerUserWebTarget.request(MediaType.APPLICATION_JSON);

        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);
        userData.setUserType(userType);
        userData.setName(name);

        Response response = invocationBuilder.post(Entity.entity(userData, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Email '{}' is already in use. Code: {}", userData.getEmail() ,response.getStatus());
            return -1;
        } else {
            logger.info("User correctly registered: {}", userData);
            return 0;
        }
    }

    public boolean login(String email, String password) {
        WebTarget loginWebTarget = webTarget.path("/user/login");
        Invocation.Builder invocationBuilder = loginWebTarget.request(MediaType.APPLICATION_JSON);

        LoginData loginData = new LoginData();
        loginData.setEmail(email);
        loginData.setPassword(password);

        Response response = invocationBuilder.post(Entity.entity(loginData, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Login credentials invalid. Code: {}", response.getStatus());
            return false;
        } else {
            logger.info("Login successful for user: {}", loginData.getEmail());
            return true;
        }
    }
    
    // Main
    public static void main(String[] args) {
        if (args.length != 2) {
            logger.info("Use: java Client.Client [host] [port]");
            System.exit(0);
        }

        String hostname = args[0];
        String port = args[1];

        AeroLogixClient aerologixClient = new AeroLogixClient(hostname, port);
        
        // Sample data
        aerologixClient.registerUser("juan.orts@opendeusto.es", "juan1234", "COUNTER_CLERK", "Juan Orts");
        aerologixClient.registerUser("admin", "admin", "ADMIN", "admin");

        // Login Window
        LoginWindow lw = LoginWindow.getInstanceLogin(aerologixClient);
        lw.setVisible(true);
    }
}
