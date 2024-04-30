package com.aerologix.app.client.controller;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.*;

public class AirlineController {
    protected static final Logger logger = LogManager.getLogger();

    private static AirlineController instance;

    private AirlineController() {
    }

    public static synchronized AirlineController getInstance() {
		if (instance == null) {
			instance = new AirlineController();
		}
		return instance;
	}

    public int createAirline(String name, int airlineId) {
        WebTarget registerAirlineWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/airline/create");
        Invocation.Builder invocationBuilder = registerAirlineWebTarget.request(MediaType.APPLICATION_JSON);

        AirlineData airlineData = new AirlineData();
        airlineData.setId(airlineId);
        airlineData.setName(name);

        Response response = invocationBuilder.post(Entity.entity(airlineData, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Cannot create a airline with data that does not exist in the database. Error code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Airline correctly registered");
            return 0;
        }
    }
    
    public int deleteAirline(String airlineId) {
        WebTarget deleteAirlineWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/airline/delete");
        Invocation.Builder invocationBuilder = deleteAirlineWebTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(Entity.entity(airlineId, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no airline with id {}. Code: {}", airlineId, response.getStatus());
            return -1;
        } else {
            logger.info("Airline '{}' deleted succesfully", airlineId);
            return 0;
        }
    }
    
    public int modifyAirline(int id, String name) {
        WebTarget modifyAirlineWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/airline/modify");
        Invocation.Builder invocationBuilder = modifyAirlineWebTarget.request(MediaType.APPLICATION_JSON);

        AirlineData airlineData = new AirlineData();
        airlineData.setId(id);
        airlineData.setName(name);

        Response response = invocationBuilder.post(Entity.entity(airlineData, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no airline with that id. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Airline '{}' modified succesfully", airlineData.getId());
            return 0;
        }
    }
    
    public AirlineData getAirline(int id) {
        WebTarget getAirlineWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/airline/get").queryParam("id", id);
        Invocation.Builder invocationBuilder = getAirlineWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("Airline retrieved succesfully");
            return response.readEntity(AirlineData.class);
        } else {
            logger.error("Failed to get airline '{}'. Status code: {}", id, response.getStatus());
            return null;
        }
    }
    
    public ArrayList<AirlineData> getAllAirlines(){
    	WebTarget getAllAirlinesWebTarget = 
        
        
        
        AeroLogixClient.getInstance().getWebTarget().path("/airline/getAll");
        Invocation.Builder invocationBuilder = getAllAirlinesWebTarget.request(MediaType.APPLICATION_JSON);
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
