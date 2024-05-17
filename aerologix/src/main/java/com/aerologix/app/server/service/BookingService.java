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

@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class BookingService {

	protected static final Logger logger = LogManager.getLogger();

	private PersistenceManagerFactory pmf;
	private PersistenceManager pm;
	private Transaction tx;

	public BookingService() {
		this.pmf = AeroLogixServer.getInstance().getPersistenceManagerFactory();
		this.pm = pmf.getPersistenceManager();
		this.tx = pm.currentTransaction();
	}

	// CRUD methods

	@GET
	@Path("/booking/get")
	public Response getBooking(@QueryParam("id") String id) {
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

	@POST
	@Path("/booking/create")
	public Response createBooking(BookingData bookingData) {
		try {
			tx.begin();

			System.out.println("\n\n" + bookingData + "\n\n");
			
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

	void setPersistenceManagerFactory(PersistenceManagerFactory pmf) {
		this.pmf = pmf;
	}

	void setPersistenceManager(PersistenceManager pm) {
		this.pm = pm;
	}

	void setTransaction(Transaction tx) {
		this.tx = tx;
	}

}
