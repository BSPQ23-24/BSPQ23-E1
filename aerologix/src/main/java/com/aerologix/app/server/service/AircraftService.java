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
 * Service class for managing aircraft-related operations.
 * <p>
 * This class provides CRUD operations for aircraft.
 */
@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class AircraftService {

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
    public AircraftService() {
        this.pmf = AeroLogixServer.getInstance().getPersistenceManagerFactory();
        this.pm = pmf.getPersistenceManager();
        this.tx = pm.currentTransaction();
    }

    // CRUD methods

    /**
     * Retrieves aircraft details for a given aircraft ID.
     *
     * @param id The ID of the aircraft to retrieve.
     * @return A Response containing the aircraft details or an error message.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction.</li>
     *     <li>Attempt to retrieve the aircraft from the database using the provided ID.</li>
     *    <li>If all the information can be found:
     *     		<ul>
	 *             <li>We create a {@link Aircraft} using all the data.</li>
	 *             <li>We make the aircraft persistent in the database.</li>
	 *             <li>Commit the transaction and return the AircraftData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
	 *         	</ul>
     *     <li>RollBack the transaction if an exception occurs.</li>
     * </ul>
     */
    @GET
    @Path("/aircraft/get")
    public Response getAircraft(@QueryParam("id") int id) {
        try {
            tx.begin();
            logger.info("Checking if aircraft {} exists in the database...", id);
            Aircraft aircraft = null;
            try {
                aircraft = pm.getObjectById(Aircraft.class, id);
            } catch (JDOObjectNotFoundException e) {
                logger.info("Aircraft with id '{}' does not exist in the database.", id);
            }

            tx.commit();

            if (aircraft != null) {
                AircraftData aircraftData = new AircraftData();
                aircraftData.setId(aircraft.getId());
                aircraftData.setManufacturer(aircraft.getManufacturer());
                aircraftData.setType(aircraft.getType());
                aircraftData.setMaxCapacity(aircraft.getMaxCapacity());

                logger.info("Sending aircraftData to client...");
                return Response.ok().entity(aircraftData).build();
            } else {
                logger.info("Aircraft not found");
                return Response.status(Response.Status.NOT_FOUND).entity("Aircraft not found").build();
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Retrieves all aircrafts.
     *
     * @return A Response containing a list of all aircrafts or an error message.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction.</li>
     *     <li>Retrieve all aircrafts from the database.</li>
     *     <li>For each aircraft, construct an AircraftData object containing the aircraft details.</li>
     *     <li>Commit the transaction and return the list of AircraftData objects.</li>
     *     <li>If no aircrafts are found, return a 204 No Content response.</li>
     *     <li>RollBack the transaction if an exception occurs.</li>
     * </ul>
     */
    @GET
    @Path("/aircraft/getAll")
    public Response getAllAircrafts() {
        try {
            tx.begin();

            ArrayList<AircraftData> aircraftDataList = new ArrayList<AircraftData>();

            logger.info("Retrieving all aircrafts from database...");

            Extent<Aircraft> e = pm.getExtent(Aircraft.class, true);
            Iterator<Aircraft> iter = e.iterator();
            while (iter.hasNext()) {
                Aircraft aircraft = (Aircraft) iter.next();
                AircraftData aircraftData = new AircraftData();
                aircraftData.setId(aircraft.getId());
                aircraftData.setManufacturer(aircraft.getManufacturer());
                aircraftData.setType(aircraft.getType());
                aircraftData.setMaxCapacity(aircraft.getMaxCapacity());

                aircraftDataList.add(aircraftData);
            }
            tx.commit();

            if (aircraftDataList.size() > 0) {
                logger.info("Sending all aircrafts to client...");
                return Response.ok().entity(aircraftDataList).build();
            } else {
                logger.error("No aircrafts registered");
                return Response.status(Response.Status.NO_CONTENT).entity("No aircrafts registered").build();
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Creates a new aircraft.
     *
     * @param aircraftData The data of the aircraft to create.
     * @return A Response indicating the result of the creation operation.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction.</li>
     *     <li>Create a new Aircraft object using the provided data.</li>
     *     <li>Make the aircraft persistent in the database.</li>
     *     <li>Commit the transaction.</li>
     * </ul>
     */
    @POST
    @Path("/aircraft/create")
    public Response createAircraft(AircraftData aircraftData) {
        try {
            tx.begin();

            Aircraft aircraft = new Aircraft();
            aircraft.setManufacturer(aircraftData.getManufacturer());
            aircraft.setType(aircraftData.getType());
            aircraft.setMaxCapacity(aircraftData.getMaxCapacity());

            pm.makePersistent(aircraft);
            tx.commit();
            logger.info("Aircraft created successfully");
            return Response.ok().build();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Modifies an existing aircraft.
     *
     * @param aircraftData The data of the aircraft to modify.
     * @return A Response indicating the result of the modification operation.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction.</li>
     *     <li>Attempt to retrieve the aircraft from the database using the provided ID.</li>
     *     <li>If the aircraft exists, modify its data with the provided data.</li>
     *     <li>Commit the transaction.</li>
     * </ul>
     */
    @POST
    @Path("/aircraft/modify")
    public Response modifyAircraft(AircraftData aircraftData) {
        try {        tx.begin();
        // Get current aircraft data
        logger.info("Get the current aircraft data for: {}", aircraftData.getId());
        Aircraft aircraft = null;
        try {
            aircraft = pm.getObjectById(Aircraft.class, aircraftData.getId());
        } catch (JDOObjectNotFoundException e) {
            logger.info("Aircraft with id '{}' does not exist in the database.", aircraftData.getId());
        }

        if (aircraft != null) {

            // Modify all data except primary key (id)
            aircraft.setManufacturer(aircraftData.getManufacturer());
            aircraft.setType(aircraftData.getType());
            aircraft.setMaxCapacity(aircraftData.getMaxCapacity());

            tx.commit();
            logger.info("Aircraft modified: {}", aircraftData.getId());
            return Response.ok().build();
        } else {
            logger.error("There is no aircraft with id: {}", aircraftData.getId());
            tx.commit();
            return Response.status(Response.Status.UNAUTHORIZED).entity("No aircraft found").build();
        }

    } finally {
        if (tx.isActive()) {
            tx.rollback();
        }
    }
}

/**
 * Deletes an existing aircraft by ID.
 *
 * @param id The ID of the aircraft to delete.
 * @return A Response indicating the result of the deletion operation.
 * <p>
 * This method performs the following steps:
 * <ul>
 *     <li>Begin a new transaction.</li>
 *     <li>Attempt to retrieve the aircraft from the database using the provided ID.</li>
 *     <li>If the aircraft exists, delete it from the database.</li>
 *     <li>Commit the transaction.</li>
 * </ul>
 */
@POST
@Path("/aircraft/delete")
public Response deleteAircraft(int id) {
    try {
        tx.begin();
        logger.info("Checking if aircraft {} exists in the database...", id);
        Aircraft aircraft = null;
        try {
            aircraft = pm.getObjectById(Aircraft.class, id);
        } catch (JDOObjectNotFoundException e) {
            logger.info("Aircraft with id '{}' does not exist in the database.", id);
        }

        if (aircraft != null) {
            // Delete aircraft
            pm.deletePersistent(aircraft);
            logger.info("Aircraft deleted '{}'", id);
            tx.commit();
            return Response.ok().build();
        } else {
            logger.error("There is no aircraft with id: {}", id);
            tx.commit();
            return Response.status(Response.Status.UNAUTHORIZED).entity("No aircraft found").build();
        }

    } finally {
        if (tx.isActive()) {
            tx.rollback();
        }
    }
}

// Other methods

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

           
