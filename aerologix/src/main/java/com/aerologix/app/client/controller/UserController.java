package com.aerologix.app.client.controller;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.*;

/**
 * Controller class for managing user-related operations.
 * <p>
 * This class provides methods to interact with the user service.
 */
public class UserController {

    /** Logger for logging messages. */
    protected static final Logger logger = LogManager.getLogger();

    /** Singleton instance of UserController. */
    private static UserController instance;

    /** Private constructor to prevent instantiation. */
    private UserController() {
    }

    /**
     * Returns the singleton instance of UserController.
     *
     * @return The singleton instance of UserController.
     */
    public static synchronized UserController getInstance() {
		if (instance == null) {
			instance = new UserController();
		}
		return instance;
	}

    /**
     * Registers a new user.
     *
     * @param email The email of the user to be registered.
     * @param password The password of the user to be registered.
     * @param userType The type of the user to be registered.
     * @param name The name of the user to be registered.
     * @return 0 if the user was registered successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Create a {@link UserData} object with the provided details.</li>
     *     <li>Send a POST request to the server to register the user.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
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

    /**
     * Logs in a user.
     *
     * @param email The email of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     * @return true if the login was successful, false otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Create a {@link LoginData} object with the provided credentials.</li>
     *     <li>Send a POST request to the server to validate the credentials.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
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

    /**
     * Modifies an existing user.
     *
     * @param email The email of the user to be modified.
     * @param password The password of the user to be modified.
     * @param userType The type of the user to be modified.
     * @param name The name of the user to be modified.
     * @return 0 if the user is modified successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Create a {@link UserData} object with the provided details.</li>
     *     <li>Send a POST request to the server to modify the user.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
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

    /**
     * Deletes an existing user by email.
     *
     * @param email The email of the user to delete.
     * @return 0 if the user is deleted successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a POST request to the server to delete the user.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
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

    /**
     * Retrieves a user by email.
     *
     * @param email The email of the user to retrieve.
     * @return A {@link UserData} object containing the user details, or null if the user is not found.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a GET request to the server to retrieve the user.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
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

    /**
     * Retrieves all users.
     *
     * @return A list of {@link UserData} objects containing the details of all users, or an empty list if no users are found.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a GET request to the server to retrieve all users.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
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