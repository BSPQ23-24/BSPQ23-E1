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
public class AirlineService {
    protected static final Logger logger = LogManager.getLogger();

	private PersistenceManagerFactory pmf;
	private PersistenceManager pm;
	private Transaction tx;

    public AirlineService() {
        this.pmf = AeroLogixServer.getInstance().getPersistenceManagerFactory();
		this.pm = pmf.getPersistenceManager();
		this.tx = pm.currentTransaction();
    }

    //  CRUD methods

 	@GET
	@Path("/airline/get")
	public Response getAirline(@QueryParam("id") String id) {
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

	@POST
	@Path("/airline/create")
	public Response createAirline(AirlineData airlineData) {
		try {
			tx.begin();

			// Create Airline instance and make it persistent
			Airline airline = new Airline();
			airline.setName(airlineData.getName());

			pm.makePersistent(airline);

			tx.commit();
			logger.info("Airline created succesfully");
			return Response.ok().build();
			
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

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
}
