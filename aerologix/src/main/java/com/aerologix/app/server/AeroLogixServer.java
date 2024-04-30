package com.aerologix.app.server;

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
