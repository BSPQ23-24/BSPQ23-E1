package com.aerologix.app.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.FlightData;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;



/**
 * @brief Controller class for managing flight-related operations.
 * 
 * Controller class for managing flight-related operations.
 * <p>
 * This class provides methods to interact with the flight service.
 */
public class FlightController {

    /** Logger for logging messages. */
	protected static final Logger logger = LogManager.getLogger();
    /** Singleton instance of FlightController. */
    private static FlightController instance;
    /** Client for connecting to the server. */
    private AeroLogixClient client;
    
    /**
     * Private constructor to initialize the client.
     *
     * @param client The client to be used for server communication.
     */
    private FlightController(AeroLogixClient client) {
    	this.client = client;
    }

    /**
     * Returns the singleton instance of FlightController.
     *
     * @param client The client to be used for server communication.
     * @return The singleton instance of FlightController.
     */
    public static synchronized FlightController getInstance(AeroLogixClient client) {
		if (instance == null) {
			instance = new FlightController(client);
		}
		return instance;
	}


    /**
     * Creates a new flight.
     *
     * @param origin The origin of the flight.
     * @param destination The destination of the flight.
     * @param date The date of the flight.
     * @param aircraft The ID of the aircraft.
     * @return 0 if the flight is created successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Create a {@link FlightData} object with the provided details.</li>
     *     <li>Send a POST request to the server to create the flight.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
	public int createFlight(String origin, String destination, long date, int aircraft) {
		WebTarget registerFlightWebTarget = client.getWebTarget().path("/flight/create");
        Invocation.Builder invocationBuilder = registerFlightWebTarget.request(MediaType.APPLICATION_JSON);
        
        FlightData flightData = new FlightData();
        flightData.setAircraftId(aircraft);
        flightData.setOrigin(origin);
        flightData.setDestination(destination);
        flightData.setDate(date);
        flightData.setBookingIds(new ArrayList<Integer>());
        
        logger.info("Sending POST request to server to create a new flight...");
        Response response = invocationBuilder.post(Entity.entity(flightData, MediaType.APPLICATION_JSON));
		
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Cannot create a flight with data that does not exist in the database. Error code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Flight correctly registered");
            return 0;
        }
	}

    /**
     * Deletes an existing flight by ID.
     *
     * @param flightId The ID of the flight to delete.
     * @return 0 if the flight is deleted successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a POST request to the server to delete the flight.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
	public int deleteFlight(int flightId) {
		WebTarget deleteUserWebTarget = client.getWebTarget().path("/flight/delete");
        Invocation.Builder invocationBuilder = deleteUserWebTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Sending POST request to server to delete flight with id '{}'...", flightId);
        Response response = invocationBuilder.post(Entity.entity(flightId, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no flight with id {}. Code: {}", flightId, response.getStatus());
            return -1;
        } else {
            logger.info("Flight '{}' deleted succesfully", flightId);
            return 0;
        }
	}

    /**
     * Modifies an existing flight.
     *
     * @param idFlight The ID of the flight to modify.
     * @param origin The new origin of the flight.
     * @param destination The new destination of the flight.
     * @param date The new date of the flight.
     * @param aircraft The new ID of the aircraft.
     * @param bookings The new list of booking IDs.
     * @return 0 if the flight is modified successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Create a {@link FlightData} object with the provided details.</li>
     *     <li>Send a POST request to the server to modify the flight.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
	public int modifyFlight(int idFlight, String origin, String destination, long date, int aircraft, List<Integer> bookings) {
		WebTarget modifyUserWebTarget = client.getWebTarget().path("/flight/modify");
        Invocation.Builder invocationBuilder = modifyUserWebTarget.request(MediaType.APPLICATION_JSON);
		
        FlightData flightData = new FlightData();
        flightData.setIdFlight(idFlight);
        flightData.setAircraftId(aircraft);
        flightData.setOrigin(origin);
        flightData.setDestination(destination);
        flightData.setDate(date);
        flightData.setBookingIds(bookings);
        
        logger.info("Sending POST request to server to modify flight with id '{}'...", idFlight);
        Response response = invocationBuilder.post(Entity.entity(flightData, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no flight with that id. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Flight '{}' modified succesfully", flightData.getIdFlight());
            return 0;
        }
	}
	
    /**
     * Retrieves flight details for a given flight ID.
     *
     * @param id The ID of the flight to retrieve.
     * @return A {@link FlightData} object containing the flight details, or null if the flight is not found.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a GET request to the server to retrieve the flight details.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
	public FlightData getFlight(int id) {
        WebTarget getUserWebTarget = client.getWebTarget().path("/flight/get").queryParam("id", id);
        Invocation.Builder invocationBuilder = getUserWebTarget.request(MediaType.APPLICATION_JSON);
        
        logger.info("Sending GET request to server to retrieve flight with id '{}'...", id);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("Flight retrieved succesfully");
            return response.readEntity(FlightData.class);
        } else {
            logger.error("Failed to get Flight '{}'. Status code: {}", id, response.getStatus());
            return null;
        }
    }

    /**
     * Retrieves all flights.
     *
     * @return A list of {@link FlightData} objects containing the details of all flights, or an empty list if no flights are found.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a GET request to the server to retrieve all flights.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
	public ArrayList<FlightData> getAllFlights() {
        WebTarget getAllUsersWebTarget = client.getWebTarget().path("/flight/getAll");
        Invocation.Builder invocationBuilder = getAllUsersWebTarget.request(MediaType.APPLICATION_JSON);
        
        logger.info("Sending GET request to server to retrieve all flights...");
        Response response = invocationBuilder.get();
    
        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("All Flights retrieved successfully");
            return response.readEntity(new GenericType<ArrayList<FlightData>>() {});
        } else if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
            logger.error("No flights registered");
            return new ArrayList<FlightData>(); // Return an empty list if no flights are registered
        } else {
            logger.error("Failed to get all flights. Status code: {}", response.getStatus());
            return null;
        }
    }

	
}
