package com.aerologix.app.server.service;

import java.util.ArrayList;
import java.util.Iterator;

import javax.jdo.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.aerologix.app.server.pojo.*;
import com.aerologix.app.server.AeroLogixServer;
import com.aerologix.app.server.jdo.*;

/**
 * Service class for managing airline-related operations.
 * <p>
 * This class provides CRUD operations for airline.
 */
@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class AirlineService {

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
	public AirlineService() {
		this.pmf = AeroLogixServer.getInstance().getPersistenceManagerFactory();
		this.pm = pmf.getPersistenceManager();
		this.tx = pm.currentTransaction();
	}

	/**
     * Retrieves airline details for a given airline ID.
     *
     * @param id The ID of the airline to retrieve.
     * @return A Response containing the airline details or an error message.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the airline from the database using the provided ID.</li>
     *     <li>If all the information can be found:
     *     		<ul>
	 *             <li>We create a {@link Airline} using all the data.</li>
	 *             <li>We make the airline persistent in the database.</li>
	 *             <li>Commit the transaction and return the AirlineData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
	 *         	</ul>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@GET
	@Path("/airline/get")
	public Response getAirline(@QueryParam("id") int id) {
		try {
			tx.begin();
			logger.info("Checking if the airline '{}' already exists or not...", id);
			Airline airline = null;

			try {
				airline = pm.getObjectById(Airline.class, id);
			} catch (JDOObjectNotFoundException e) {
				logger.info("Airline with id '{}' does not exist in the database.", id);
			}

			tx.commit();

			// If airline exists
			if (airline != null) {
				AirlineData airlineData = new AirlineData();
				airlineData.setId(airline.getId());
				airlineData.setName(airline.getName());

				logger.info("Sending airlineData to client...");
				return Response.ok().entity(airlineData).build();
			} else {
				logger.info("Airline not found");
				return Response.status(Response.Status.NOT_FOUND).entity("Airline not found").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
    /**
     * Retrieves all airlines.
     *
     * @return A Response containing a list of all airlines or an error message.
     * <p>
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Retrieve all airlines from the database.</li>
     *     <li>For each airline, construct a AirlineData object containing the airline details.</li>
     *     <li>Commit the transaction and return the list of AirlineData objects.(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
     *     <li>If no airlines are found, return a 204 No Content response.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@GET
	@Path("/airline/getAll")
	public Response getAllAirlines() {
		try {
			tx.begin();

			ArrayList<AirlineData> airlineList = new ArrayList<AirlineData>();

			logger.info("Retrieving all airlines from database...");

			Extent<Airline> e = pm.getExtent(Airline.class, true);
			Iterator<Airline> iter = e.iterator();
			while (iter.hasNext()) {
				Airline airline = (Airline) iter.next();
				AirlineData airlineData = new AirlineData();
				airlineData.setId(airline.getId());
				airlineData.setName(airline.getName());

				airlineList.add(airlineData);
			}
			tx.commit();

			if (airlineList.size() > 0) {
				logger.info("Sending all airlines to client...");
				return Response.ok().entity(airlineList).build();
			} else {
				logger.error("No airlines registered");
				return Response.status(Response.Status.NO_CONTENT).entity("No airlines registered").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
	 /**
     * Creates a new airline.
     *
     * @param airlineData The data of the airline to create.
     * @return A Response indicating the result of the creation operation.
     *<p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve all the required information to create the airline using the provided {@link AirlineData}.</li>
     *     <li>If all the information can be found, we create a {@link Airline} using all the data.</li>
     *     <li> We make the airline persistent in the database.</li>
     *     <li>commit the transaction and return the AirlineData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
     *     <li>If something from the airline data does not exist, return a 401 UNAUTHORIZED.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@POST
	@Path("/airline/create")
	public Response createAirline(AirlineData airlineData) {
		try {
			tx.begin();
			logger.info("Checking if airline '{}' already exists or not", airlineData.getId());
			Airline airline = null;

			try {
				airline = pm.getObjectById(Airline.class, airlineData.getId());
			} catch (JDOObjectNotFoundException e) {
				logger.info("Airline with ID '{}' does not exist in the database.", airlineData.getId());
			}

			if (airline == null) {
				logger.info("Creating airline...");
				airline = new Airline(airlineData.getId(), airlineData.getName());
				pm.makePersistent(airline);
				logger.info("Airline created: {}", airline);
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("ID '{}' is already in use", airlineData.getId());
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("ID is already in use").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
    /**
     * Modifies an existing airline.
     *
     * @param airlineData The data of the airline to modify.
     * @return A Response indicating the result of the modification operation.
     *<p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the airline from the database using the provided ID.</li>
     *     <li>Attempt to retrieve all the required information to modify the airline using the provided {@link AirlineData}.</li>
     *     <li>If all the information can be found, we modify the data from the {@link Airline} using all the data.</li>
     *     <li>commit the transaction and return the AirlineData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
     *     <li>If something from the airline data does not exist, return a 401 Unauthorized.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@POST
	@Path("/airline/modify")
	public Response modifyAirline(AirlineData airlineData) {
		try {
			tx.begin();
			// Get current airline data
			logger.info("Get the current airline data for: {}", airlineData.getId());
			Airline airline = null;
			try {
				airline = pm.getObjectById(Airline.class, airlineData.getId());
			} catch (JDOObjectNotFoundException e) {
				logger.info("Airline with id '{}' does not exist in the database.", airlineData.getId());
			}

			if (airline != null) {
				// Modify all data except primary key (id)
				airline.setId(airlineData.getId());
				airline.setName(airlineData.getName());
				logger.info("Airline modified: {}", airline.getId());
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("There is no airline with id: {}", airlineData.getId());
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("No airline found").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
	 /**
     * Deletes an existing airline by ID.
     *
     * @param id The ID of the airline to delete.
     * @return A Response indicating the result of the deletion operation.
      <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the airline from the database using the provided ID.</li>
     *     <li>If the airline exists, we delete the airline from the database.</li>
     *     <li>Commit the transaction and return the AirlineData object(Finalizing all the changes made during the transaction and making them permanent in the database).</li>
     *     <li>If the airline does not exist, return a 401 Unauthorized.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@POST
	@Path("/airline/delete")
	public Response deleteAirline(int id) {
		try {
			tx.begin();
			logger.info("Checking if airline {} exists in the database...", id);
			Airline airline = null;
			try {
				airline = pm.getObjectById(Airline.class, id);
			} catch (JDOObjectNotFoundException e) {
				logger.info("Airline with id '{}' does not exist in the database.", id);
			}

			if (airline != null) {
				// Delete airline
				pm.deletePersistent(airline);
				logger.info("Airline deleted '{}'", id);
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("There is no airline with id: {}", id);
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("No airline found").build();
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
