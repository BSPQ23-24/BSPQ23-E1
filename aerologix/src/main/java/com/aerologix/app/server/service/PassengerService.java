package com.aerologix.app.server.service;

import java.util.ArrayList;
import java.util.Iterator;

import javax.jdo.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.aerologix.app.server.pojo.PassengerData;
import com.aerologix.app.server.pojo.PassengerData;
import com.aerologix.app.server.AeroLogixServer;
import com.aerologix.app.server.jdo.Passenger;
import com.aerologix.app.server.jdo.Passenger;
import com.aerologix.app.server.jdo.Passenger;

/**
 * Service class for managing Passenger-related operations.
 * <p>
 * This class provides CRUD operations for passenger.
 */
@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class PassengerService {
    
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
    public PassengerService() {
		this.pmf = AeroLogixServer.getInstance().getPersistenceManagerFactory();
		this.pm = pmf.getPersistenceManager();
		this.tx = pm.currentTransaction();
    }

	/**
     * Retrieves passenger details for a given passenger ID.
     *
     * @param id The ID of the passenger to retrieve.
     * @return A Response containing the passenger details or an error message.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the passenger from the database using the provided ID.</li>
     *     <li>If all the information can be found:
     *     		<ul>
	 *             <li>We create a {@link Passenger} using all the data.</li>
	 *             <li>We make the passenger persistent in the database.</li>
	 *             <li>Commit the transaction and return the PassengerData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
	 *         	</ul>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@POST
	@Path("/passenger/create")
	public Response createPassenger(PassengerData passengerData) {
		try {
			tx.begin();
			logger.info("Checking if passenger '{}' already exists or not", passengerData.getDNI());
			Passenger passenger = null;

			try {
				passenger = pm.getObjectById(Passenger.class, passengerData.getDNI());
			} catch (JDOObjectNotFoundException e) {
				logger.info("Passenger with DNI '{}' does not exist in the database.", passengerData.getDNI());
			}

			if (passenger == null) {
				logger.info("Creating passenger...");
				passenger = new Passenger(passengerData.getDNI(), passengerData.getPhone(), passengerData.getName(),
                        passengerData.getEmail(), passengerData.getNationality(), passengerData.getBirthdate());
				pm.makePersistent(passenger);
				logger.info("Passenger created: {}", passenger);
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("DNI '{}' is already in use", passengerData.getDNI());
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("DNI is already in use").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
    /**
     * Modifies an existing Passenger.
     *
     * @param PassengerData The data of the Passenger to modify.
     * @return A Response indicating the result of the modification operation.
     *<p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the Passenger from the database using the provided ID.</li>
     *     <li>Attempt to retrieve all the required information to modify the Passenger using the provided {@link PassengerData}.</li>
     *     <li>If all the information can be found, we modify the data from the {@link Passenger} using all the data.</li>
     *     <li>commit the transaction and return the PassengerData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
     *     <li>If something from the Passenger data does not exist, return a 401 Unauthorized.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@POST
	@Path("/passenger/modify")
	public Response modifyPassenger(PassengerData passengerData) {
		try {
			tx.begin();
			logger.info("Get the current passenger data for: {}", passengerData.getDNI());
			Passenger passenger = null;
			try {
				passenger = pm.getObjectById(Passenger.class, passengerData.getDNI());
			} catch (JDOObjectNotFoundException e) {
				logger.info("Passenger with DNI '{}' does not exist in the database.", passengerData.getDNI());
			}

			if (passenger != null) {
				passenger.setPhone(passengerData.getPhone());
				passenger.setName(passengerData.getName());
				passenger.setEmail(passengerData.getEmail());
				passenger.setNationality(passengerData.getNationality());
				passenger.setBirthdate(passengerData.getBirthdate());
				logger.info("Passenger modified: {}", passenger);
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("There is no passenger registered with DNI: {}", passengerData.getDNI());
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("No passenger found").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
	 /**
     * Deletes an existing Passenger by ID.
     *
     * @param id The ID of the Passenger to delete.
     * @return A Response indicating the result of the deletion operation.
      <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the Passenger from the database using the provided ID.</li>
     *     <li>If the Passenger exists, we delete the Passenger from the database.</li>
     *     <li>Commit the transaction and return the PassengerData object(Finalizing all the changes made during the transaction and making them permanent in the database).</li>
     *     <li>If the Passenger does not exist, return a 401 Unauthorized.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@POST
	@Path("/passenger/delete")
	public Response deletePassenger(String dni) {
		try {
			tx.begin();
			logger.info("Checking if passenger {} exists in the database...", dni);
			Passenger passenger = null;
			try {
				passenger = pm.getObjectById(Passenger.class, dni);
			} catch (JDOObjectNotFoundException e) {
				logger.info("Passenger with DNI '{}' does not exist in the database.", dni);
			}

			if (passenger != null) {
				pm.deletePersistent(passenger);
				logger.info("Passenger deleted: {}", dni);
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("There is no passenger registered with DNI: {}", dni);
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("No passenger found").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	/**
     * Retrieves Passenger details for a given Passenger ID.
     *
     * @param id The ID of the Passenger to retrieve.
     * @return A Response containing the Passenger details or an error message.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the Passenger from the database using the provided ID.</li>
     *     <li>If all the information can be found:
     *     		<ul>
	 *             <li>We create a {@link Passenger} using all the data.</li>
	 *             <li>We make the Passenger persistent in the database.</li>
	 *             <li>Commit the transaction and return the PassengerData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
	 *         	</ul>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@GET
	@Path("/passenger/get")
	public Response getPassenger(@QueryParam("dni") String dni) {
		try {
			tx.begin();
			logger.info("Checking if the passenger '{}' already exists or not...", dni);
			Passenger passenger = null;

			try {
				passenger = pm.getObjectById(Passenger.class, dni);
			} catch (JDOObjectNotFoundException e) {
				logger.info("Passenger with DNI '{}' does not exist in the database.", dni);
			}

			tx.commit();

			if (passenger != null) {
				PassengerData passengerData = new PassengerData();
				passengerData.setDNI(passenger.getDNI());
				passengerData.setPhone(passenger.getPhone());
				passengerData.setName(passenger.getName());
				passengerData.setEmail(passenger.getEmail());
				passengerData.setNationality(passenger.getNationality());
				passengerData.setBirthdate(passenger.getBirthdate());

				logger.info("Sending passengerData to client...");
				return Response.ok().entity(passengerData).build();
			} else {
				logger.info("Passenger not found");
				return Response.status(Response.Status.NOT_FOUND).entity("Passenger not found").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
    /**
     * Retrieves all Passengers.
     *
     * @return A Response containing a list of all Passengers or an error message.
     * <p>
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Retrieve all Passengers from the database.</li>
     *     <li>For each Passenger, construct a PassengerData object containing the Passenger details.</li>
     *     <li>Commit the transaction and return the list of PassengerData objects.(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
     *     <li>If no Passengers are found, return a 204 No Content response.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
	@GET
	@Path("/passenger/getAll")
	public Response getAllPassengers() {
		try {
			tx.begin();

			ArrayList<PassengerData> passengerList = new ArrayList<>();

			logger.info("Retrieving all passengers from database...");

			Extent<Passenger> e = pm.getExtent(Passenger.class, true);
			Iterator<Passenger> iter = e.iterator();
			while (iter.hasNext()) {
				Passenger passenger = iter.next();
				PassengerData passengerData = new PassengerData();
				passengerData.setDNI(passenger.getDNI());
				passengerData.setPhone(passenger.getPhone());
				passengerData.setName(passenger.getName());
				passengerData.setEmail(passenger.getEmail());
				passengerData.setNationality(passenger.getNationality());
				passengerData.setBirthdate(passenger.getBirthdate());
				passengerList.add(passengerData);
			}
			tx.commit();

			if (passengerList.size() > 0) {
				logger.info("Sending all passengers to client...");
				return Response.ok().entity(passengerList).build();
			} else {
				logger.error("No passengers registered");
				return Response.status(Response.Status.NO_CONTENT).entity("No passengers registered").build();
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
