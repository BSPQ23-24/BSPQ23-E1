package com.aerologix.app.client.controller;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.*;

public class UserController {
    
    protected static final Logger logger = LogManager.getLogger();

    private static UserController instance;

    private UserController() {
    }

    public static synchronized UserController getInstance() {
		if (instance == null) {
			instance = new UserController();
		}
		return instance;
	}

    /*
     * CRUD: user
     */

    public int registerUser(String email, String password, String userType, String name) {
        WebTarget registerUserWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/user/register");
        Invocation.Builder invocationBuilder = registerUserWebTarget.request(MediaType.APPLICATION_JSON);

        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);
        userData.setUserType(userType);
        userData.setName(name);

        logger.info("Sending POST request to server to register a new user...");
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
        WebTarget loginWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/user/login");
        Invocation.Builder invocationBuilder = loginWebTarget.request(MediaType.APPLICATION_JSON);

        LoginData loginData = new LoginData();
        loginData.setEmail(email);
        loginData.setPassword(password);

        logger.info("Sending POST request to server to validate login credentials for user with email '{}'...", email);
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
        WebTarget modifyUserWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/user/modify");
        Invocation.Builder invocationBuilder = modifyUserWebTarget.request(MediaType.APPLICATION_JSON);

        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);
        userData.setUserType(userType);
        userData.setName(name);

        logger.info("Sending POST request to server to modify user with email '{}'...", email);
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
        WebTarget deleteUserWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/user/delete");
        Invocation.Builder invocationBuilder = deleteUserWebTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Sending POST request to server to delete user with email '{}'...", email);
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
        WebTarget getUserWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/user/get").queryParam("email", email);
        Invocation.Builder invocationBuilder = getUserWebTarget.request(MediaType.APPLICATION_JSON);
        
        logger.info("Sending GET request to server to retrieve user with email '{}'...", email);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("User retrieved succesfully");
            return response.readEntity(UserData.class);
        } else {
            logger.error("Failed to get user '{}'. Status code: {}", email, response.getStatus());
            return null;
        }
    }

    public ArrayList<UserData> getAllUsers() {
        WebTarget getAllUsersWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/user/getAll");
        Invocation.Builder invocationBuilder = getAllUsersWebTarget.request(MediaType.APPLICATION_JSON);
        
        logger.info("Sending GET request to server to retrieve all users...");
        Response response = invocationBuilder.get();
    
        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("All users retrieved successfully");
            return response.readEntity(new GenericType<ArrayList<UserData>>() {});
        } else if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
            logger.error("No users registered");
            return new ArrayList<UserData>(); // Return an empty list if no users are registered
        } else {
            logger.error("Failed to get all users. Status code: {}", response.getStatus());
            return null;
        }
    }
}