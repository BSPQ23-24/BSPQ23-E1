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

@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class AircraftService {

	protected static final Logger logger = LogManager.getLogger();

	private PersistenceManagerFactory pmf;
	private PersistenceManager pm;
	private Transaction tx;

	public AircraftService() {
		this.pmf = AeroLogixServer.getInstance().getPersistenceManagerFactory();
		this.pm = pmf.getPersistenceManager();
		this.tx = pm.currentTransaction();
	}

	// CRUD methods

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
			logger.info("Aircraft created succesfully");
			return Response.ok().build();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/aircraft/modify")
	public Response modifyAircraft(AircraftData aircraftData) {
		try {
			tx.begin();
			// Get current booking data
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
				// Delete flight
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
