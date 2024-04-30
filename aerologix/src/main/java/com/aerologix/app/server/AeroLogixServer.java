package com.aerologix.app.server;

import java.util.ArrayList;
import java.util.Iterator;

import javax.jdo.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.aerologix.app.server.pojo.*;
import com.aerologix.app.server.jdo.*;

@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class AeroLogixServer {

	protected static final Logger logger = LogManager.getLogger();

	private static AeroLogixServer instance;

	private PersistenceManagerFactory pmf;

	private AeroLogixServer() {
		this.pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
	}

	public static AeroLogixServer getInstance() {
        if (instance == null) {
            instance = new AeroLogixServer();
        }
        return instance;
    }

	public PersistenceManagerFactory getPersistenceManagerFactory() {
		return this.pmf;
	}

	/*
	 * CRUD: Flight
	 */

	@POST
	@Path("/flight/get")
	public Response getFlight(int id) {
		return null;
	}

	@POST
	@Path("/flight/getAll")
	public Response getAllFlights() {
		return null;
	}

	@POST
	@Path("/flight/create")
	public Response createFlight(FlightData flightData) {
		return null;
	}

	@POST
	@Path("/flight/modify")
	public Response modifyBooking(FlightData flightData) {
		return null;
	}

	@POST
	@Path("/flight/delete")
	public Response deleteFlight(int id) {
		return null;
	}

	/*
	 * CRUD: Passenger
	 */

	@POST
	@Path("/passenger/get")
	public Response getPassenger(String dni) {
		return null;
	}

	@POST
	@Path("/passenger/getAll")
	public Response getAllPassengers() {
		return null;
	}

	@POST
	@Path("/passenger/create")
	public Response createPassenger(PassengerData passengerData) {
		return null;
	}

	@POST
	@Path("/passenger/modify")
	public Response modifyPassenger(PassengerData passengerData) {
		return null;
	}

	@POST
	@Path("/passenger/delete")
	public Response deletePassenger(String dni) {
		return null;
	}

	/*
	 * CRUD: Airline
	 */

/* 	@GET
	@Path("/airline/get")
	public Response getAirline(@QueryParam("id") String id) {
		try {
			tx.begin();
			logger.info("Checking if the airline '{}' already exists or not...", id);
			Airline airline = null;

			try {
				airline = pm.getObjectById(Airline.class, id);
			} catch (JDOObjectNotFoundException e) {
				logger.info("Booking with id '{}' does not exist in the database.", id);
			}

			tx.commit();

			// If booking exists
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
				logger.info("Sending all airline to client...");
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

			boolean notFound = false;

			// Retrieve all linked classes
			int airlineId = 0;
			String name = null;

			logger.info("Retrieving all data required for the airline");
			airlineId = airlineData.getId();
			name = airlineData.getName();

			// Create Airline instance and make it persistent
			Airline airline = new Airline(airlineId, name);

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
			logger.info("Get the current booking data for: {}", airlineData.getId());
			Airline airline = null;
			try {
				airline = pm.getObjectById(Airline.class, airlineData.getId());
			} catch (JDOObjectNotFoundException e) {
				logger.info("Airline with id '{}' does not exist in the database.", airlineData.getId());
			}

			if (airline != null) {
				int airlineId = 0;
				String name = null;
				
				logger.info("Retrieving all data required for the modified airline");
				name = airlineData.getName();
				airlineId = airlineData.getId();

				// Modify all data except primary key (id)
				airline.setId(airlineData.getId());
				airline.setName(airlineData.getName());
				tx.commit();
				logger.info("Airline modified: {}", airline.getId());
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
				logger.info("Airline deleted {}");
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
 */
	/*
	 * CRUD: Aircraft
	 */

	@POST
	@Path("/aircraft/get")
	public Response getAircraft(int id) {
		return null;
	}

	@POST
	@Path("/aircraft/getAll")
	public Response getAllAircrafts() {
		return null;
	}

	@POST
	@Path("/aircraft/create")
	public Response createAircraft(AircraftData aircraftData) {
		return null;
	}

	@POST
	@Path("/aircraft/modify")
	public Response modifyAircraft(AircraftData aircraftData) {
		return null;
	}

	@POST
	@Path("/aircraft/delete")
	public Response deleteAircraft(int id) {
		return null;
	}

}
