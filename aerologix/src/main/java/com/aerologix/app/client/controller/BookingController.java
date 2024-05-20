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
 * Controller class for managing booking-related operations.
 * <p>
 * This class provides methods to interact with the booking service.
 */
public class BookingController {
    
    /** Logger for logging messages. */
    protected static final Logger logger = LogManager.getLogger();

    /** Singleton instance of BookingController. */
    private static BookingController instance;

    /** Client for connecting to the server. */
    private AeroLogixClient client;

    /**
     * Private constructor to initialize the client.
     *
     * @param client The client to be used for server communication.
     */
    private BookingController(AeroLogixClient client) {
    	this.client = client;
    }

    /**
     * Returns the singleton instance of BookingController.
     *
     * @param client The client to be used for server communication.
     * @return The singleton instance of BookingController.
     */
    public static synchronized BookingController getInstance(AeroLogixClient client) {
		if (instance == null) {
			instance = new BookingController(client);
		}
		return instance;
	}

    /** Private constructor to prevent instantiation. */
    private BookingController() {
    }

    /**
     * Creates a new booking.
     *
     * @param passengerDNI The DNI of the passenger for the booking.
     * @param flightId The ID of the flight for the booking.
     * @param userEmail The email of the user making the booking.
     * @param airlineId The ID of the airline for the booking.
     * @return 0 if the booking was created successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Create a {@link BookingData} object with the provided details.</li>
     *     <li>Send a POST request to the server to create the booking.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public int createBooking(String passengerDNI, int flightId, String userEmail, int airlineId) {
        WebTarget registerBookingWebTarget = client.getWebTarget().path("/booking/create");
        Invocation.Builder invocationBuilder = registerBookingWebTarget.request(MediaType.APPLICATION_JSON);

        BookingData bookingData = new BookingData();
        bookingData.setId(-1);
        bookingData.setPassengerDNI(passengerDNI);
        bookingData.setFlightId(flightId);
        bookingData.setUserEmail(userEmail);
        bookingData.setAirlineId(airlineId);
        
        System.out.println(bookingData);

        logger.info("Sending POST request to server to create a new booking...");
        Response response = invocationBuilder.post(Entity.entity(bookingData, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Cannot create a booking with data that does not exist in the database. Error code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Booking correctly registered");
            return 0;
        }
    }

    /**
     * Deletes an existing booking by ID.
     *
     * @param bookingId The ID of the booking to delete.
     * @return 0 if the booking is deleted successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a POST request to the server to delete the booking.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public int deleteBooking(String bookingId) {
        WebTarget deleteBookingWebTarget = client.getWebTarget().path("/booking/delete");
        Invocation.Builder invocationBuilder = deleteBookingWebTarget.request(MediaType.APPLICATION_JSON);

        logger.info("Sending POST request to server to delete booking with id '{}'...", bookingId);
        Response response = invocationBuilder.post(Entity.entity(bookingId, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no booking with id {}. Code: {}", bookingId, response.getStatus());
            return -1;
        } else {
            logger.info("Booking '{}' deleted succesfully", bookingId);
            return 0;
        }
    }

    /**
     * Modifies an existing booking.
     *
     * @param id The ID of the booking to modify.
     * @param passengerDNI The DNI of the passenger for the booking.
     * @param flightId The ID of the flight for the booking.
     * @param userEmail The email of the user making the booking.
     * @param airlineId The ID of the airline for the booking.
     * @return 0 if the booking is modified successfully, -1 otherwise.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Create a {@link BookingData} object with the provided details.</li>
     *     <li>Send a POST request to the server to modify the booking.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public int modifyBooking(int id, String passengerDNI, int flightId, String userEmail, int airlineId) {
        WebTarget modifyBookingWebTarget = client.getWebTarget().path("/booking/modify");
        Invocation.Builder invocationBuilder = modifyBookingWebTarget.request(MediaType.APPLICATION_JSON);

        BookingData bookingData = new BookingData();
        bookingData.setId(id);
        bookingData.setPassengerDNI(passengerDNI);
        bookingData.setFlightId(flightId);
        bookingData.setUserEmail(userEmail);
        bookingData.setAirlineId(airlineId);

        logger.info("Sending POST request to server to modify booking with id '{}'...", id);
        Response response = invocationBuilder.post(Entity.entity(bookingData, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no booking with that id. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Booking '{}' modified succesfully", bookingData.getId());
            return 0;
        }
    }

    /**
     * Retrieves a booking by ID.
     *
     * @param id The ID of the booking to retrieve.
     * @return A {@link BookingData} object containing the booking details, or null if the booking is not found.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a GET request to the server to retrieve the booking.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public BookingData getBooking(int id) {
        WebTarget getBookingWebTarget = client.getWebTarget().path("/booking/get").queryParam("id", id);
        Invocation.Builder invocationBuilder = getBookingWebTarget.request(MediaType.APPLICATION_JSON);
        
        logger.info("Sending GET request to server to retrieve booking with id '{}'...", id);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("Booking retrieved succesfully");
            return response.readEntity(BookingData.class);
        } else {
            logger.error("Failed to get booking '{}'. Status code: {}", id, response.getStatus());
            return null;
        }
    }

    /**
     * Retrieves all bookings.
     *
     * @return A list of {@link BookingData} objects containing the details of all bookings, or an empty list if no bookings are found.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Send a GET request to the server to retrieve all bookings.</li>
     *     <li>Log the result of the operation.</li>
     * </ul>
     */
    public ArrayList<BookingData> getAllBookings() {
        WebTarget getAllBookingsWebTarget = client.getWebTarget().path("/booking/getAll");
        Invocation.Builder invocationBuilder = getAllBookingsWebTarget.request(MediaType.APPLICATION_JSON);
        
        logger.info("Sending GET request to server to retrieve all bookings...");
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
