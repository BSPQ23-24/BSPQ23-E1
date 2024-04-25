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

    /*
     * CRUD: user
     */

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

    public int modifyUser(String email, String password, String userType, String name) {
        WebTarget modifyUserWebTarget = webTarget.path("/user/modify");
        Invocation.Builder invocationBuilder = modifyUserWebTarget.request(MediaType.APPLICATION_JSON);

        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);
        userData.setUserType(userType);
        userData.setName(name);

        Response response = invocationBuilder.post(Entity.entity(userData, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no user registered with that email. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("User '{}' modified succesfully", userData.getEmail());
            return 0;
        }
    }

    public int deleteUser(String email) {
        WebTarget deleteUserWebTarget = webTarget.path("/user/delete");
        Invocation.Builder invocationBuilder = deleteUserWebTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(Entity.entity(email, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no user registered with that email. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("User '{}' deleted succesfully", email);
            return 0;
        }
    }

    public UserData getUser(String email) {
        WebTarget getUserWebTarget = webTarget.path("/user/get").queryParam("email", email);
        Invocation.Builder invocationBuilder = getUserWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("User retrieved succesfully");
            return response.readEntity(UserData.class);
        } else {
            logger.error("Failed to get user '{}'. Status code: {}", email, response.getStatus());
            return null;
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
        //LoginWindow lw = LoginWindow.getInstanceLogin(aerologixClient);
        //lw.setVisible(true);

        // Modify user
        aerologixClient.modifyUser("juan.orts@opendeusto.es", "juan2002", "COUNTER_CLERK", "Juan Orts Madina");
        aerologixClient.deleteUser("admin");

        System.out.println(aerologixClient.getUser("juan.orts@opendeusto.es"));
    }
}
