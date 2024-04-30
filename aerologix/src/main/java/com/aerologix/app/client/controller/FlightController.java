package com.aerologix.app.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aerologix.app.server.jdo.Aircraft;
import com.aerologix.app.server.jdo.Booking;
import com.aerologix.app.server.pojo.BookingData;
import com.aerologix.app.server.pojo.FlightData;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class FlightController {

	protected static final Logger logger = LogManager.getLogger();

    private Client client;
    private WebTarget webTarget;

    public FlightController(Client client, WebTarget webTarget) {
        this.client = client;
        this.webTarget = webTarget;
    }

	public int createFlight(String origin, String destination, long date, int aircraft, List<Integer> bookings) {
		WebTarget registerUserWebTarget = webTarget.path("/flight/create");
        Invocation.Builder invocationBuilder = registerUserWebTarget.request(MediaType.APPLICATION_JSON);
        
        FlightData flightData = new FlightData();
        flightData.setAircraftId(aircraft);
        flightData.setOrigin(origin);
        flightData.setDestination(destination);
        flightData.setDate(date);
        flightData.setBookingIds(bookings);
        
        Response response = invocationBuilder.post(Entity.entity(flightData, MediaType.APPLICATION_JSON));
		
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Cannot create a flight with data that does not exist in the database. Error code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Flight correctly registered");
            return 0;
        }
	}
	public int deleteFlight(String flightId) {
		WebTarget deleteUserWebTarget = webTarget.path("/flight/delete");
        Invocation.Builder invocationBuilder = deleteUserWebTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(Entity.entity(flightId, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no flight with id {}. Code: {}", flightId, response.getStatus());
            return -1;
        } else {
            logger.info("Flight '{}' deleted succesfully", flightId);
            return 0;
        }
	}
	public int modifyFlight(int idFlight, String origin, String destination, long date, int aircraft, List<Integer> bookings) {
		WebTarget modifyUserWebTarget = webTarget.path("/flight/modify");
        Invocation.Builder invocationBuilder = modifyUserWebTarget.request(MediaType.APPLICATION_JSON);
		
        FlightData flightData = new FlightData();
        flightData.setAircraftId(aircraft);
        flightData.setOrigin(origin);
        flightData.setDestination(destination);
        flightData.setDate(date);
        flightData.setBookingIds(bookings);
        
        Response response = invocationBuilder.post(Entity.entity(flightData, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no flight with that id. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Flight '{}' modified succesfully", flightData.getIdFlight());
            return 0;
        }
	}
	
	public FlightData getFlight(int id) {
        WebTarget getUserWebTarget = webTarget.path("/flight/get").queryParam("id", id);
        Invocation.Builder invocationBuilder = getUserWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("Flight retrieved succesfully");
            return response.readEntity(FlightData.class);
        } else {
            logger.error("Failed to get Flight '{}'. Status code: {}", id, response.getStatus());
            return null;
        }
    }
	public ArrayList<FlightData> getAllFlights() {
        WebTarget getAllUsersWebTarget = webTarget.path("/flight/getAll");
        Invocation.Builder invocationBuilder = getAllUsersWebTarget.request(MediaType.APPLICATION_JSON);
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
