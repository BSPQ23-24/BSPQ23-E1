package com.aerologix.app.client.controller;


import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.AircraftData;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class AircraftController {
    
    protected static final Logger logger = LogManager.getLogger();
    
    private static AircraftController instance;
    
    private AeroLogixClient client;

    private AircraftController(AeroLogixClient client) {
    	this.client = client;
    }

    public static synchronized AircraftController getInstance(AeroLogixClient client) {
		if (instance == null) {
			instance = new AircraftController(client);
		}
		return instance;
	}

    private AircraftController() {
    }

    public int createAircraft(String manufacturer, String type, int maxCapacity) {
		WebTarget registerFlightWebTarget = client.getWebTarget().path("/aircraft/create");
        Invocation.Builder invocationBuilder = registerFlightWebTarget.request(MediaType.APPLICATION_JSON);
        
        AircraftData aircraftData = new AircraftData();
        aircraftData.setManufacturer(manufacturer);
        aircraftData.setType(type);
        aircraftData.setMaxCapacity(maxCapacity);
        
        logger.info("Sending POST request to server to create a new aircraft...");
        Response response = invocationBuilder.post(Entity.entity(aircraftData, MediaType.APPLICATION_JSON));
		
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Cannot connect with server. Error code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Aircraft correctly registered");
            return 0;
        }
	}
	public int deleteAircraft(int id) {
		WebTarget deleteUserWebTarget = client.getWebTarget().path("/aircraft/delete");
        Invocation.Builder invocationBuilder = deleteUserWebTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Sending POST request to server to delete aircraft with id '{}'...", id);
        Response response = invocationBuilder.post(Entity.entity(id, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no aircraft with id {}. Code: {}", id, response.getStatus());
            return -1;
        } else {
            logger.info("Aircraft '{}' deleted succesfully", id);
            return 0;
        }
	}
	public int modifyAircraft(int id, String manufacturer, String type, int maxCapacity) {
		WebTarget modifyUserWebTarget = client.getWebTarget().path("/aircraft/modify");
        Invocation.Builder invocationBuilder = modifyUserWebTarget.request(MediaType.APPLICATION_JSON);
		
        AircraftData aircraftData = new AircraftData();
        aircraftData.setId(id);
        aircraftData.setManufacturer(manufacturer);
        aircraftData.setType(type);
        aircraftData.setMaxCapacity(maxCapacity);
        
        logger.info("Sending POST request to server to modify aircraft with id '{}'...", id);
        Response response = invocationBuilder.post(Entity.entity(aircraftData, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no aircraft with that id. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Aircraft '{}' modified succesfully", id);
            return 0;
        }
	}
	
	public AircraftData getAircraft(int id) {
        WebTarget getUserWebTarget = client.getWebTarget().path("/aircraft/get").queryParam("id", id);
        Invocation.Builder invocationBuilder = getUserWebTarget.request(MediaType.APPLICATION_JSON);
        
        logger.info("Sending GET request to server to retrieve aircraft with id '{}'...", id);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("Aircraft retrieved succesfully");
            return response.readEntity(AircraftData.class);
        } else {
            logger.error("Failed to get Aircraft with id '{}'. Status code: {}", id, response.getStatus());
            return null;
        }
    }
	public ArrayList<AircraftData> getAllAircrafts() {
        WebTarget getAllUsersWebTarget = client.getWebTarget().path("/aircraft/getAll");
        Invocation.Builder invocationBuilder = getAllUsersWebTarget.request(MediaType.APPLICATION_JSON);
        
        logger.info("Sending GET request to server to retrieve all aircrafts...");
        Response response = invocationBuilder.get();
    
        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("All Aircrafts retrieved successfully");
            return response.readEntity(new GenericType<ArrayList<AircraftData>>() {});
        } else if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
            logger.error("No aircrafts registered");
            return new ArrayList<AircraftData>(); // Return an empty list if no aircrafts are registered
        } else {
            logger.error("Failed to get all aircrafts. Status code: {}", response.getStatus());
            return null;
        }
    }
}
