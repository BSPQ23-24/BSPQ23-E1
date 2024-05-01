package com.aerologix.app.server.service;

import java.util.ArrayList;
import java.util.Iterator;

import javax.jdo.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.aerologix.app.server.pojo.PassengerData;
import com.aerologix.app.server.AeroLogixServer;
import com.aerologix.app.server.jdo.Passenger;

@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class PassengerService {
    
    protected static final Logger logger = LogManager.getLogger();

	private PersistenceManagerFactory pmf;
	private PersistenceManager pm;
	private Transaction tx;

    public PassengerService() {
		this.pmf = AeroLogixServer.getInstance().getPersistenceManagerFactory();
		this.pm = pmf.getPersistenceManager();
		this.tx = pm.currentTransaction();
    }

    // CRUD methods

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
}
