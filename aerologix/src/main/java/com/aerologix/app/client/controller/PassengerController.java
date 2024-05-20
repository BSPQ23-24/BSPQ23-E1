package com.aerologix.app.client.controller;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.PassengerData;

/**
 * Controller class for managing passenger-related operations.
 * <p>
 * This class provides methods to interact with the passenger service.
 */
public class PassengerController {

    /** Logger for logging messages. */
    protected static final Logger logger = LogManager.getLogger();
    /** Singleton instance of PassengerController. */
    private static PassengerController instance;
    /** Client for connecting to the server. */
    private AeroLogixClient client;

    /**
     * Private constructor to initialize the client.
     *
     * @param client The client to be used for server communication.
     */
    private PassengerController(AeroLogixClient client) {
        this.client = client;
    }

    /**
     * Returns the singleton instance of PassengerController.
     *
     * @param client The client to be used for server communication.
     * @return The singleton instance of PassengerController.
     */
    public static synchronized PassengerController getInstance(AeroLogixClient client) {
        if (instance == null) {
            instance = new PassengerController(client);
        }
        return instance;
    }

    /**
     * Creates a new passenger.
     *
     * @param passengerData The data of the passenger to be created.
     * @return 0 if the passenger was created successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Create a {@link PassengerData} object with the provided details.</li>
     *     <li>Send a POST request to the server to create the passenger.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public int createPassenger(PassengerData passengerData) {
        WebTarget registerPassengerWebTarget = client.getWebTarget().path("/passenger/create");
        Invocation.Builder invocationBuilder = registerPassengerWebTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Sending POST request to server to register a new passenger...");
        Response response = invocationBuilder.post(Entity.entity(passengerData, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Passenger registration failed. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Passenger correctly registered: {}", passengerData);
            return 0;
        }
    }

    /**
     * Modifies an existing passenger.
     *
     * @param passengerData The data of the passenger to modify.
     * @return 0 if the passenger is modified successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Create a {@link PassengerData} object with the provided details.</li>
     *     <li>Send a POST request to the server to modify the passenger.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public int modifyPassenger(PassengerData passengerData) {
        WebTarget modifyPassengerWebTarget = client.getWebTarget().path("/passenger/modify");
        Invocation.Builder invocationBuilder = modifyPassengerWebTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Sending POST request to server to modify passenger with DNI '{}'...", passengerData.getDNI());
        Response response = invocationBuilder.post(Entity.entity(passengerData, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Passenger modification failed. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Passenger '{}' modified successfully", passengerData.getDNI());
            return 0;
        }
    }


    /**
     * Deletes an existing passenger by DNI.
     *
     * @param dni The DNI of the passenger to delete.
     * @return 0 if the passenger is deleted successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a POST request to the server to delete the passenger.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public int deletePassenger(String dni) {
        WebTarget deletePassengerWebTarget = client.getWebTarget().path("/passenger/delete");
        Invocation.Builder invocationBuilder = deletePassengerWebTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Sending POST request to server to delete passenger with DNI '{}'...", dni);
        Response response = invocationBuilder.post(Entity.entity(dni, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Passenger deletion failed. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Passenger '{}' deleted successfully", dni);
            return 0;
        }
    }

    /**
     * Retrieves a passenger by DNI.
     *
     * @param dni The DNI of the passenger to retrieve.
     * @return A {@link PassengerData} object containing the passenger details, or null if the passenger is not found.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a GET request to the server to retrieve the passenger.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public PassengerData getPassenger(String dni) {
        WebTarget getPassengerWebTarget = client.getWebTarget().path("/passenger/get").queryParam("dni", dni);
        Invocation.Builder invocationBuilder = getPassengerWebTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Sending GET request to server to retrieve passenger with DNI '{}'...", dni);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("Passenger retrieved successfully");
            return response.readEntity(PassengerData.class);
        } else {
            logger.error("Failed to get passenger '{}'. Status code: {}", dni, response.getStatus());
            return null;
        }
    }

    /**
     * Retrieves all passengers.
     *
     * @return A list of {@link PassengerData} objects containing the details of all passengers, or an empty list if no passengers are found.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a GET request to the server to retrieve all passengers.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public ArrayList<PassengerData> getAllPassengers() {
        WebTarget getAllPassengersWebTarget = client.getWebTarget().path("/passenger/getAll");
        Invocation.Builder invocationBuilder = getAllPassengersWebTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Sending GET request to server to retrieve all passengers...");
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("All passengers retrieved successfully");
            return response.readEntity(new GenericType<ArrayList<PassengerData>>() {});
        } else if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
            logger.error("No passengers registered");
            return new ArrayList<PassengerData>(); // Return an empty list if no passengers are registered
        } else {
            logger.error("Failed to get all passengers. Status code: {}", response.getStatus());
            return null;
        }
    }
}
