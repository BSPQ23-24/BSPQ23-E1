package com.aerologix.app.server.service;

import java.util.ArrayList;
import java.util.HashSet;
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
 * @brief Service class for managing flight-related operations.
 * 
 * <p>
 * This class provides CRUD operations for flights.
 */
@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class FlightService {
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
    public FlightService() {
        this.pmf = AeroLogixServer.getInstance().getPersistenceManagerFactory();
        this.pm = pmf.getPersistenceManager();
        this.tx = pm.currentTransaction();
    }

    /**
     * Retrieves flight details for a given flight ID.
     *
     * @param id The ID of the flight to retrieve.
     * @return A Response containing the flight details or an error message.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the flight from the database using the provided ID.</li>
     *     <li>If the flight exists, construct a FlightData object containing the flight details and associated booking IDs.</li>
     *     <li>Commit the transaction and return the FlightData object(Finalizing all the changes made during the transaction and making them permanent in the database).</li>
     *     <li>If the flight does not exist, return a 404 Not Found response.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
    @GET
    @Path("/flight/get")
    public Response getFlight(@QueryParam("id") int id) {
        try {
            tx.begin();
            logger.info("Checking if flight {} exists in the database...", id);
            Flight flight = null;
            try {
                flight = pm.getObjectById(Flight.class, id);
            } catch (JDOObjectNotFoundException e) {
                logger.info("Flight with id '{}' does not exist in the database.", id);
            }

            if (flight != null) {
                FlightData flightData = new FlightData();
                flightData.setIdFlight(flight.getIdFlight());
                flightData.setOrigin(flight.getOrigin());
                flightData.setDestination(flight.getDestination());
                flightData.setAircraftId(flight.getAircraft().getId());
                flightData.setDate(flight.getDate());

                // Setting the booking id list
                ArrayList<Integer> bookingIds = new ArrayList<Integer>();
                
                for(Booking b : flight.getBookings()) {
                	bookingIds.add(b.getId());
                }

                flightData.setBookingIds(bookingIds);

                logger.info("Sending flightData to client...");
                
                tx.commit();
                return Response.ok().entity(flightData).build();
            } else {
                logger.info("Flight not found");
                return Response.status(Response.Status.NOT_FOUND).entity("Flight not found").build();

            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }

        }
    }
    /**
     * Retrieves all flights.
     *
     * @return A Response containing a list of all flights or an error message.
     * <p>
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Retrieve all flights from the database.</li>
     *     <li>For each flight, construct a FlightData object containing the flight details and associated booking IDs.</li>
     *     <li>Commit the transaction and return the list of FlightData objects.(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
     *     <li>If no flights are found, return a 204 No Content response.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
    @GET
    @Path("/flight/getAll")
    public Response getAllFlights() {
        try {
            tx.begin();

            ArrayList<FlightData> flightDataList = new ArrayList<FlightData>();

            logger.info("Retrieving all flights from database...");

            Extent<Flight> e = pm.getExtent(Flight.class, true);
            Iterator<Flight> iter = e.iterator();
            while (iter.hasNext()) {
                Flight flight = (Flight) iter.next();
                FlightData flightData = new FlightData();
                flightData.setIdFlight(flight.getIdFlight());
                flightData.setOrigin(flight.getOrigin());
                flightData.setDestination(flight.getDestination());
                flightData.setAircraftId(flight.getAircraft().getId());
                flightData.setDate(flight.getDate());

                // Setting the booking id list
                Set<Booking> bookings = flight.getBookings();

                ArrayList<Integer> bookingIdList = new ArrayList<Integer>();
                
                for(Booking b : bookings) {
                	bookingIdList.add(b.getId());
                }

                flightData.setBookingIds(bookingIdList);

                flightDataList.add(flightData);
            }
            tx.commit();

            if (flightDataList.size() > 0) {
                logger.info("Sending all flights to client...");
                return Response.ok().entity(flightDataList).build();
            } else {
                logger.error("No flights registered");
                return Response.status(Response.Status.NO_CONTENT).entity("No users registered").build();
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /**
     * Creates a new flight.
     *
     * @param flightData The data of the flight to create.
     * @return A Response indicating the result of the creation operation.
     *<p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve all the required information to create the flight using the provided {@link FlightData}.</li>
     *     <li>If all the information can be found:
     *     		<ul>
	 *             <li>We create a {@link Flight} using all the data.</li>
	 *             <li>We make the flight persistent in the database.</li>
	 *             <li>Commit the transaction and return the FlightData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
	 *         	</ul>
     *     <li>If something from the flight data does not exist, return a 401 UNAUTHORIZED.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
    @POST
    @Path("/flight/create")
    public Response createFlight(FlightData flightData) {
        try {
            tx.begin();

            boolean notFound = false;

            Aircraft aircraft = null;

            try {
                logger.info("Retrieving all data required for the flight");
                aircraft = pm.getObjectById(Aircraft.class, flightData.getAircraftId());
            } catch (JDOObjectNotFoundException e) {
                logger.info("At least one object of the Flight '{}' not found: {}", flightData.getIdFlight(), e);
                notFound = true;
            }
            Flight flight = new Flight();
            flight.setOrigin(flightData.getOrigin());
            flight.setDestination(flightData.getDestination());
            flight.setDate(flightData.getDate());
            flight.setAircraft(aircraft);
            flight.setBookings(new HashSet<Booking>());

            if (notFound) {
                tx.commit();
                logger.error(
                        "Error when creating a new flight. At least one of the objects related to the flight does not exist.");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Cannot create a new flight with data that does not exist in the database").build();
            } else {
                pm.makePersistent(flight);
                tx.commit();
                logger.info("Flight created succesfully");
                return Response.ok().build();
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /**
     * Modifies an existing flight.
     *
     * @param flightData The data of the flight to modify.
     * @return A Response indicating the result of the modification operation.
     *<p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the flight from the database using the provided ID.</li>
     *     <li>Attempt to retrieve all the required information to modify the flight using the provided {@link FlightData}.</li>
     *     <li>If all the information can be found, we modify the data from the {@link Flight} using all the data.</li>
     *     <li>commit the transaction and return the FlightData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
     *     <li>If something from the flight data does not exist, return a 401 Unauthorized.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
    @POST
    @Path("/flight/modify")
    public Response modifyFlight(FlightData flightData) {
        try {
            tx.begin();
            // Get current flight data
            logger.info("Get the current flight data for: {}", flightData.getIdFlight());
            Flight flight = null;
            try {
                flight = pm.getObjectById(Flight.class, flightData.getIdFlight());
            } catch (JDOObjectNotFoundException e) {
                logger.info("Flight with id '{}' does not exist in the database.", flightData.getIdFlight());
            }

            if (flight != null) {
                Aircraft aircraft = null;

                Set<Booking> bookings = new HashSet<Booking>();
                try {
                    logger.info("Retrieving all data required for the flight");
                    aircraft = pm.getObjectById(Aircraft.class, flightData.getAircraftId());
                    logger.info("Retrieving all bookings...");
                    for (int id : flightData.getBookingIds()) {

                        bookings.add(pm.getObjectById(Booking.class, id));
                    }
                } catch (JDOObjectNotFoundException e) {
                    logger.info("At least one object of the Flight '{}' not found: {}", flightData.getIdFlight(), e);

                }
                
                // Modify all data except primary key (id)
                flight.setOrigin(flightData.getOrigin());
                flight.setDestination(flightData.getDestination());
                flight.setDate(flightData.getDate());
                flight.setAircraft(aircraft);
                flight.setBookings(bookings);

                tx.commit();
                logger.info("Flight modified: {}", flight);
                return Response.ok().build();
            } else {
                logger.error("There is no flight with id: {}", flightData.getIdFlight());
                tx.commit();
                return Response.status(Response.Status.UNAUTHORIZED).entity("No flight found").build();
            }

        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    /**
     * Deletes an existing flight by ID.
     *
     * @param id The ID of the flight to delete.
     * @return A Response indicating the result of the deletion operation.
      <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the flight from the database using the provided ID.</li>
     *     <li>If the flight exists, we delete the flight from the database.</li>
     *     <li>Commit the transaction and return the FlightData object(Finalizing all the changes made during the transaction and making them permanent in the database).</li>
     *     <li>If the flight does not exist, return a 401 Unauthorized.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
    @POST
    @Path("/flight/delete")
    public Response deleteFlight(int id) {
        try {
            tx.begin();
            logger.info("Checking if flight {} exists in the database...", id);
            Flight flight = null;
            try {
                flight = pm.getObjectById(Flight.class, id);
            } catch (JDOObjectNotFoundException e) {
                logger.info("Flight with id '{}' does not exist in the database.", id);
            }

            if (flight != null) {
                // First delete all bookings in a flight
                for(Booking b : flight.getBookings()) {
                    logger.info("Booking deleted '{}'", b.getId());
                    pm.deletePersistent(b);
                }
                // Delete flight
                pm.deletePersistent(flight);
                logger.info("Flight deleted '{}'", id);
                tx.commit();
                return Response.ok().build();
            } else {
                logger.error("There is no flight with id: {}", id);
                tx.commit();
                return Response.status(Response.Status.UNAUTHORIZED).entity("No flight found").build();
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
