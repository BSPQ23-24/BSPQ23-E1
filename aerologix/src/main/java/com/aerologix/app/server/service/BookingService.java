package com.aerologix.app.server.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.jdo.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.aerologix.app.server.pojo.*;
import com.aerologix.app.server.AeroLogixServer;
import com.aerologix.app.server.jdo.*;
/**
 * Service class for managing Booking-related operations.
 * <p>
 * This class provides CRUD operations for bookings.
 */
@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class BookingService {
	/** Logger for logging messages. */
	protected static final Logger logger = LogManager.getLogger();
	/** PersistenceManagerFactory for creating PersistenceManager instances. */
	private PersistenceManagerFactory pmf;
	/** PersistenceManager for managing JDO operations. */
	private PersistenceManager pm;
	/** Transaction for managing database transactions. */
	private Transaction tx;

    /**
     * Default constructor initializing persistence manager and transaction.
     */
	public BookingService() {
		this.pmf = AeroLogixServer.getInstance().getPersistenceManagerFactory();
		this.pm = pmf.getPersistenceManager();
		this.tx = pm.currentTransaction();
	}

	/**
     * Retrieves Booking's details for a given flight ID.
     *
     * @param id The ID of the booking to retrieve.
     * @return A Response containing the booking details or an error message.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the booking from the database using the provided ID.</li>
     *     <li>If the booking exists, construct a BookingData object containing the booking details.</li>
     *     <li>Commit the transaction and return the BookingData object(Finalizing all the changes made during the transaction and making them permanent in the database).</li>
     *     <li>If the booking does not exist, return a 404 Not Found response.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */

	@GET
	@Path("/booking/get")
	public Response getBooking(@QueryParam("id") int id) {
		try {
			tx.begin();
			logger.info("Checking if the booking '{}' already exists or not...", id);
			Booking booking = null;

			try {
				booking = pm.getObjectById(Booking.class, id);
			} catch (JDOObjectNotFoundException e) {
				logger.info("Booking with id '{}' does not exist in the database.", id);
			}

			tx.commit();

			// If booking exists
			if (booking != null) {
				BookingData bookingData = new BookingData();
				bookingData.setId(booking.getId());				
				bookingData.setPassengerDNI(booking.getPassenger().getDNI());
				bookingData.setFlightId(booking.getFlight().getIdFlight());
				bookingData.setUserEmail(booking.getUser().getEmail());
				bookingData.setAirlineId(booking.getAirline().getId());

				logger.info("Sending bookingData to client...");
				return Response.ok().entity(bookingData).build();
			} else {
				logger.info("Booking not found");
				return Response.status(Response.Status.NOT_FOUND).entity("Booking not found").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
	/**
     * Retrieves all bookings.
     *
     * @return A Response containing a list of all bookings or an error message.
     * <p>
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Retrieve all bookings from the database.</li>
     *     <li>For each booking, construct a BookingData object containing the booking details and associated booking IDs.</li>
     *     <li>Commit the transaction and return the list of BookingData objects.(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
     *     <li>If no bookings are found, return a 204 No Content response.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@GET
	@Path("/booking/getAll")
	public Response getAllBookings() {
		try {
			tx.begin();

			ArrayList<BookingData> bookingList = new ArrayList<BookingData>();

			logger.info("Retrieving all bookings from database...");

			Extent<Booking> e = pm.getExtent(Booking.class, true);
			Iterator<Booking> iter = e.iterator();
			while (iter.hasNext()) {
				Booking booking = (Booking) iter.next();
				BookingData bookingData = new BookingData();
				bookingData.setId(booking.getId());
				bookingData.setPassengerDNI(booking.getPassenger().getDNI());
				bookingData.setFlightId(booking.getFlight().getIdFlight());
				bookingData.setUserEmail(booking.getUser().getEmail());
				bookingData.setAirlineId(booking.getAirline().getId());

				bookingList.add(bookingData);
			}
			tx.commit();

			if (bookingList.size() > 0) {
				logger.info("Sending all bookings to client...");
				return Response.ok().entity(bookingList).build();
			} else {
				logger.error("No bookings registered");
				return Response.status(Response.Status.NO_CONTENT).entity("No bookings registered").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
	/**
     * Creates a new booking.
     *
     * @param bookingData The data of the booking to create.
     * @return A Response indicating the result of the creation operation.
     *<p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve all the required information to create the booking using the provided {@link BookingData}.</li>
     *     <li>If all the information can be found, we create a {@link Booking} using all the data.</li>
     *     <li> We make the booking persistent in the database.</li>
     *     <li>commit the transaction and return the BookingData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
     *     <li>If something from the booking data does not exist, return a 401 UNAUTHORIZED.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@POST
	@Path("/booking/create")
	public Response createBooking(BookingData bookingData) {
		try {
			tx.begin();
			
			boolean notFound = false;

			// Retrieve all linked classes
			Passenger passenger = null;
			Flight flight = null;
			User user = null;
			Airline airline = null;

			try {
				logger.info("Retrieving all data required for the booking...");
				flight = pm.getObjectById(Flight.class, bookingData.getFlightId());
				logger.info("Flight retrieved succesfully for booking");
				user = pm.getObjectById(User.class, bookingData.getUserEmail());
				logger.info("User retrieved succesfully for booking");
				airline = pm.getObjectById(Airline.class, bookingData.getAirlineId());
				logger.info("Airline retrieved succesfully for booking");
				passenger = pm.getObjectById(Passenger.class, bookingData.getPassengerDNI());
				logger.info("Passenger retrieved succesfully for booking");
			} catch (JDOObjectNotFoundException e) {
				logger.info("At least one object of the Booking '{}' not found: {}", bookingData.getId(), e);
				notFound = true;
			}

			// Create Booking instance and make it persistent
			Booking booking = new Booking();
			booking.setPassenger(passenger);
			booking.setFlight(flight);
			booking.setUser(user);
			booking.setAirline(airline);
			
			// Add the booking to the list in flight
			Set<Booking> bookings = flight.getBookings();
			bookings.add(booking);
			flight.setBookings(bookings);

			if (notFound) {
				tx.commit();
				logger.error(
						"Error when creating a new booking. At least one of the objects related to the booking does not exist.");
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Cannot create a new booking with data that does not exist in the database").build();
			} else {
				pm.makePersistent(booking);
				tx.commit();
				logger.info("Booking created succesfully");
				return Response.ok().build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
	/**
     * Modifies an existing booking.
     *
     * @param flightData The data of the booking to modify.
     * @return A Response indicating the result of the modification operation.
     *<p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the booking from the database using the provided ID.</li>
     *     <li>Attempt to retrieve all the required information to modify the booking using the provided {@link BookingData}.</li>
     *     <li>If all the information can be found, we modify the data from the {@link Booking} using all the data.</li>
     *     <li>commit the transaction and return the BookingData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
     *     <li>If something from the booking data does not exist, return a 401 Unauthorized.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@POST
	@Path("/booking/modify")
	public Response modifyBooking(BookingData bookingData) {
		try {
			tx.begin();
			// Get current booking data
			logger.info("Get the current booking data for: {}", bookingData.getId());
			Booking booking = null;
			try {
				booking = pm.getObjectById(Booking.class, bookingData.getId());
			} catch (JDOObjectNotFoundException e) {
				logger.info("Booking with id '{}' does not exist in the database.", bookingData.getId());
			}

			if (booking != null) {
				// Retrieve all linked classes
				Passenger passenger = null;
				Flight flight = null;
				User user = null;
				Airline airline = null;
				try {
					logger.info("Retrieving all data required for the modified booking");
					passenger = pm.getObjectById(Passenger.class, bookingData.getPassengerDNI());
					flight = pm.getObjectById(Flight.class, bookingData.getFlightId());
					user = pm.getObjectById(User.class, bookingData.getUserEmail());
					airline = pm.getObjectById(Airline.class, bookingData.getAirlineId());
				} catch (JDOObjectNotFoundException e) {
					logger.info("Could not find all the data for the new values: ", e);
				}

				// Modify all data except primary key (id)
				booking.setId(bookingData.getId());
				booking.setPassenger(passenger);
				booking.setFlight(flight);
				booking.setUser(user);
				booking.setAirline(airline);
				tx.commit();
				logger.info("User modified: {}", user);
				return Response.ok().build();
			} else {
				logger.error("There is no booking with id: {}", bookingData.getId());
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("No booking found").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
	/**
     * Deletes an existing booking by ID.
     *
     * @param id The ID of the booking to delete.
     * @return A Response indicating the result of the deletion operation.
      <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the booking from the database using the provided ID.</li>
     *     <li>If the booking exists, we delete the booking from the database.</li>
     *     <li>Commit the transaction and return the BookingData object(Finalizing all the changes made during the transaction and making them permanent in the database).</li>
     *     <li>If the booking does not exist, return a 401 Unauthorized.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@POST
	@Path("/booking/delete")
	public Response deleteBooking(int id) {
		try {
			tx.begin();
			logger.info("Checking if booking {} exists in the database...", id);
			Booking booking = null;
			try {
				booking = pm.getObjectById(Booking.class, id);
			} catch (JDOObjectNotFoundException e) {
				logger.info("Booking with id '{}' does not exist in the database.", id);
			}

			if (booking != null) {
				// Delete user
				pm.deletePersistent(booking);
				logger.info("Booking deleted {}", id);
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("There is no booking with id: {}", id);
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("No booking found").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
	/**
     * Sets the persistence manager factory.
     * <p>
     * This method is primarily used for testing purposes.
     *
     * @param pmf The {@link PersistenceManagerFactory} to set.
     */
	void setPersistenceManagerFactory(PersistenceManagerFactory pmf) {
		this.pmf = pmf;
	}
	/**
     * Sets the persistence manager.
     * <p>
     * This method is primarily used for testing purposes.
     *
     * @param pm The {@link PersistenceManager} to set.
     */
	void setPersistenceManager(PersistenceManager pm) {
		this.pm = pm;
	}
	/**
     * Sets the transaction.
     * <p>
     * This method is primarily used for testing purposes.
     *
     * @param tx The {@link Transaction} to set.
     */
	void setTransaction(Transaction tx) {
		this.tx = tx;
	}

}
