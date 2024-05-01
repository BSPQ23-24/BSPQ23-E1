package com.aerologix.app.client.controller;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.PassengerData;

public class PassengerController {
    
    protected static final Logger logger = LogManager.getLogger();

    private static PassengerController instance;

    private PassengerController() {
    }

    public static synchronized PassengerController getInstance() {
		if (instance == null) {
			instance = new PassengerController();
		}
		return instance;
	}


    public int createPassenger(PassengerData passengerData) {
        WebTarget registerPassengerWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/passenger/create");
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

    public int modifyPassenger(PassengerData passengerData) {
        WebTarget modifyPassengerWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/passenger/modify");
        Invocation.Builder invocationBuilder = modifyPassengerWebTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Sending POST request to server to modify passenger with DNI '{}'...", passengerData.getDNI());
        Response response = invocationBuilder.post(Entity.entity(passengerData, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Passenger modification failed. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Passenger '{}' modified successfully", passengerData.getDNI());
            return 0;
        }
    }

    public int deletePassenger(String dni) {
        WebTarget deletePassengerWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/passenger/delete");
        Invocation.Builder invocationBuilder = deletePassengerWebTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Sending POST request to server to delete passenger with DNI '{}'...", dni);
        Response response = invocationBuilder.post(Entity.entity(dni, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Passenger deletion failed. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Passenger '{}' deleted successfully", dni);
            return 0;
        }
    }

    public PassengerData getPassenger(String dni) {
        WebTarget getPassengerWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/passenger/get").queryParam("dni", dni);
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

    public ArrayList<PassengerData> getAllPassengers() {
        WebTarget getAllPassengersWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/passenger/getAll");
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
