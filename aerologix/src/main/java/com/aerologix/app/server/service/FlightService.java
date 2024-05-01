package com.aerologix.app.server.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
public class FlightService {

    protected static final Logger logger = LogManager.getLogger();

    private PersistenceManagerFactory pmf;
    private PersistenceManager pm;
    private Transaction tx;

    public FlightService() {
        this.pmf = AeroLogixServer.getInstance().getPersistenceManagerFactory();
        this.pm = pmf.getPersistenceManager();
        this.tx = pm.currentTransaction();
    }

    /*
     * CRUD: Flight
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

            tx.commit();

            if (flight != null) {
                FlightData flightData = new FlightData();
                flightData.setIdFlight(flight.getIdFlight());
                flightData.setOrigin(flight.getOrigin());
                flightData.setDestination(flight.getDestination());
                flightData.setAircraftId(flight.getAircraft().getId());
                flightData.setDate(flight.getDate());

                // Setting the booking id list
                List<Booking> bookings = flight.getBookings();

                ArrayList<Integer> bookingIdList = new ArrayList<Integer>();
                Iterator<Booking> iter = bookings.iterator();
                logger.info("Retrieving booking list...");
                while (iter.hasNext()) {
                    bookingIdList.add((Integer) iter.next().getId());
                }

                flightData.setBookingIds(bookingIdList);

                logger.info("Sending flightData to client...");
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
                List<Booking> bookings = flight.getBookings();

                ArrayList<Integer> bookingIdList = new ArrayList<Integer>();
                Iterator<Booking> iter2 = bookings.iterator();
                logger.info("Retrieving booking list...");
                while (iter2.hasNext()) {
                    bookingIdList.add((Integer) iter2.next().getId());
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

    @POST
    @Path("/flight/create")
    public Response createFlight(FlightData flightData) {
        try {
            tx.begin();

            boolean notFound = false;

            Aircraft aircraft = null;

            List<Booking> bookings = new ArrayList<Booking>();

            try {
                logger.info("Retrieving all data required for the flight");
                aircraft = pm.getObjectById(Aircraft.class, flightData.getAircraftId());
                logger.info("Retrieving all bookings...");
                for (int id : flightData.getBookingIds()) {
                    bookings.add(pm.getObjectById(Booking.class, id));
                }
            } catch (JDOObjectNotFoundException e) {
                logger.info("At least one object of the Flight '{}' not found: {}", flightData.getIdFlight(), e);
                notFound = true;
            }
            Flight flight = new Flight();
            flight.setOrigin(flightData.getOrigin());
            flight.setDestination(flightData.getDestination());
            flight.setDate(flightData.getDate());
            flight.setAircraft(aircraft);
            flight.setBookings(bookings);

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

                List<Booking> bookings = new ArrayList<Booking>();
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

}
