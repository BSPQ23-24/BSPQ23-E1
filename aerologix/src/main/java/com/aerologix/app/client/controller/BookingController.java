package com.aerologix.app.client.controller;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.*;

public class BookingController {
    
    protected static final Logger logger = LogManager.getLogger();

    private static BookingController instance;

    private BookingController() {
    }

    public static synchronized BookingController getInstance() {
		if (instance == null) {
			instance = new BookingController();
		}
		return instance;
	}

    /*
     * CRUD: booking
     */

    public int createBooking(String passengerDNI, int flightId, String userEmail, int airlineId) {
        WebTarget registerBookingWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/booking/create");
        Invocation.Builder invocationBuilder = registerBookingWebTarget.request(MediaType.APPLICATION_JSON);

        BookingData bookingData = new BookingData();
        bookingData.setPassengerDNI(passengerDNI);
        bookingData.setFlightId(flightId);
        bookingData.setUserEmail(userEmail);
        bookingData.setAirlineId(airlineId);

        Response response = invocationBuilder.post(Entity.entity(bookingData, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Cannot create a booking with data that does not exist in the database. Error code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Booking correctly registered");
            return 0;
        }
    }

    public int deleteBooking(String bookingId) {
        WebTarget deleteBookingWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/booking/delete");
        Invocation.Builder invocationBuilder = deleteBookingWebTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(Entity.entity(bookingId, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no booking with id {}. Code: {}", bookingId, response.getStatus());
            return -1;
        } else {
            logger.info("Booking '{}' deleted succesfully", bookingId);
            return 0;
        }
    }

    public int modifyBooking(int id, String passengerDNI, int flightId, String userEmail, int airlineId) {
        WebTarget modifyBookingWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/booking/modify");
        Invocation.Builder invocationBuilder = modifyBookingWebTarget.request(MediaType.APPLICATION_JSON);

        BookingData bookingData = new BookingData();
        bookingData.setId(id);
        bookingData.setPassengerDNI(passengerDNI);
        bookingData.setFlightId(flightId);
        bookingData.setUserEmail(userEmail);
        bookingData.setAirlineId(airlineId);

        Response response = invocationBuilder.post(Entity.entity(bookingData, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no booking with that id. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Booking '{}' modified succesfully", bookingData.getId());
            return 0;
        }
    }

    public BookingData getBooking(int id) {
        WebTarget getBookingWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/booking/get").queryParam("id", id);
        Invocation.Builder invocationBuilder = getBookingWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("Booking retrieved succesfully");
            return response.readEntity(BookingData.class);
        } else {
            logger.error("Failed to get booking '{}'. Status code: {}", id, response.getStatus());
            return null;
        }
    }

    public ArrayList<BookingData> getAllBookings() {
        WebTarget getAllBookingsWebTarget = AeroLogixClient.getInstance().getWebTarget().path("/booking/getAll");
        Invocation.Builder invocationBuilder = getAllBookingsWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
    
        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("All bookings retrieved successfully");
            return response.readEntity(new GenericType<ArrayList<BookingData>>() {});
        } else if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
            logger.error("No bookings registered");
            return new ArrayList<BookingData>(); // Return an empty list if no bookings are registered
        } else {
            logger.error("Failed to get all bookings. Status code: {}", response.getStatus());
            return null;
        }
    }
    
}
