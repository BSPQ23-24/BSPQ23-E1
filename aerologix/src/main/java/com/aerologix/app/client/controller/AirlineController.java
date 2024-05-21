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
 * @brief Controller class for managing airline-related operations.
 * 
 * Controller class for managing airline-related operations.
 * <p>
 * This class provides methods to interact with the airline service.
 */
public class AirlineController {

    /** Logger for logging messages. */
    protected static final Logger logger = LogManager.getLogger();
    /** Singleton instance of AirlineController. */
    private static AirlineController instance;
    /** Client for connecting to the server. */
    private AeroLogixClient client;


    /**
     * Private constructor to initialize the client.
     *
     * @param client The client to be used for server communication.
     */
    private AirlineController(AeroLogixClient client) {
    	this.client = client;
    }

    /**
     * Returns the singleton instance of AirlineController.
     *
     * @param client The client to be used for server communication.
     * @return The singleton instance of AirlineController.
     */
    public static synchronized AirlineController getInstance(AeroLogixClient client) {
		if (instance == null) {
			instance = new AirlineController(client);
		}
		return instance;
	}

    /**
     * Creates a new airline.
     *
     * @param name The name of the airline to be created.
     * @return 0 if the airline was created successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Create a {@link AirlineData} object with the provided details.</li>
     *     <li>Send a POST request to the server to create the airline.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public int createAirline(String name) {
        WebTarget registerAirlineWebTarget = client.getWebTarget().path("/airline/create");
        Invocation.Builder invocationBuilder = registerAirlineWebTarget.request(MediaType.APPLICATION_JSON);

        AirlineData airlineData = new AirlineData();
        airlineData.setName(name);

        logger.info("Sending POST request to server to create a new airline...");
        Response response = invocationBuilder.post(Entity.entity(airlineData, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Cannot connect to the server. Error code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Airline correctly registered: {}", airlineData.getId());
            return 0;
        }
    }
    
    /**
     * Deletes an existing airline by ID.
     *
     * @param airlineId The ID of the airline to delete.
     * @return 0 if the airline is deleted successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a POST request to the server to delete the airline.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public int deleteAirline(int airlineId) {
        WebTarget deleteAirlineWebTarget = client.getWebTarget().path("/airline/delete");
        Invocation.Builder invocationBuilder = deleteAirlineWebTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Sending POST request to server to delete airline with id '{}'...", airlineId);
        Response response = invocationBuilder.post(Entity.entity(airlineId, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no airline with id {}. Code: {}", airlineId, response.getStatus());
            return -1;
        } else {
            logger.info("Airline '{}' deleted succesfully", airlineId);
            return 0;
        }
    }
    
    /**
     * Modifies an existing airline.
     *
     * @param id The ID of the airline to modify.
     * @param name The new name of the airline.
     * @return 0 if the airline is modified successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Create a {@link AirlineData} object with the provided details.</li>
     *     <li>Send a POST request to the server to modify the airline.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public int modifyAirline(int id, String name) {
        WebTarget modifyAirlineWebTarget = client.getWebTarget().path("/airline/modify");
        Invocation.Builder invocationBuilder = modifyAirlineWebTarget.request(MediaType.APPLICATION_JSON);

        AirlineData airlineData = new AirlineData();
        airlineData.setId(id);
        airlineData.setName(name);

        logger.info("Sending POST request to server to modify airline with id '{}'...", id);
        Response response = invocationBuilder.post(Entity.entity(airlineData, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no airline with that id. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Airline '{}' modified succesfully", airlineData.getId());
            return 0;
        }
    }
    
    /**
     * Retrieves an airline by ID.
     *
     * @param id The ID of the airline to retrieve.
     * @return A {@link AirlineData} object containing the airline details, or null if the airline is not found.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a GET request to the server to retrieve the airline.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public AirlineData getAirline(int id) {
        WebTarget getAirlineWebTarget = client.getWebTarget().path("/airline/get").queryParam("id", id);
        Invocation.Builder invocationBuilder = getAirlineWebTarget.request(MediaType.APPLICATION_JSON);
        
        logger.info("Sending GET request to server to retrieve airline with id '{}'...", id);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("Airline retrieved succesfully");
            return response.readEntity(AirlineData.class);
        } else {
            logger.error("Failed to get airline '{}'. Status code: {}", id, response.getStatus());
            return null;
        }
    }
    
    /**
     * Retrieves all airlines.
     *
     * @return A list of {@link AirlineData} objects containing the details of all airlines, or an empty list if no airlines are found.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a GET request to the server to retrieve all airlines.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public ArrayList<AirlineData> getAllAirlines(){
    	WebTarget getAllAirlinesWebTarget = client.getWebTarget().path("/airline/getAll");
        Invocation.Builder invocationBuilder = getAllAirlinesWebTarget.request(MediaType.APPLICATION_JSON);
        
        logger.info("Sending GET request to server to retrieve all airlines...");
        Response response = invocationBuilder.get();
    
        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("All airlines retrieved successfully");
            return response.readEntity(new GenericType<ArrayList<AirlineData>>() {});
        } else if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
            logger.error("No airlines registered");
            return new ArrayList<AirlineData>(); // Return an empty list if no airlines are registered
        } else {
            logger.error("Failed to get all airlines. Status code: {}", response.getStatus());
            return null;
        }
    }

    
}
