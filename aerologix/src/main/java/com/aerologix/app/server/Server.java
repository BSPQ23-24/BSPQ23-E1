package com.aerologix.app.server;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.aerologix.app.server.pojo.*;
import com.aerologix.app.server.jdo.*;

@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class Server {
    
    protected static final Logger logger = LogManager.getLogger();

    private PersistenceManager pm;
    private Transaction tx;

    public Server() {
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
        this.pm = pmf.getPersistenceManager();
        this.tx = pm.currentTransaction();
    }

    /*
     * CRUD: User
     */

    @POST
    @Path("/user/register")
    public Response registerUser(UserData userData) {
        try{
            tx.begin();
            logger.info("Checking if user '{}' already exists or not", userData.getEmail());
            User user = null;
            
            try { 
                user = pm.getObjectById(User.class, userData.getEmail());
            } catch(JDOObjectNotFoundException e) {
                logger.info("User with email '{}' does not exist in the database.", userData.getEmail());
            }

            if (user == null) {
                logger.info("Creating user...");
                user = new User(userData.getEmail(), userData.getPassword(), User.UserType.valueOf(userData.getUserType()), userData.getName());
                pm.makePersistent(user);
                logger.info("User created: {}", user);
                tx.commit();
                return Response.ok().build();
            } else {
                logger.error("Email '{}' is already in use", userData.getEmail());
                tx.commit();
                return Response.status(Response.Status.UNAUTHORIZED).entity("Email is already in use").build();
            }
            
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
        }
    }

    @POST
    @Path("/user/login")
    public Response login(LoginData loginData) {
        try{
            tx.begin();
            logger.info("Checking if the user '{}' already exists or not...", loginData.getEmail());
            User user = null;
            
            try { 
                user = pm.getObjectById(User.class, loginData.getEmail());
            } catch(JDOObjectNotFoundException e) {
                logger.info("User with email '{}' does not exist in the database.", loginData.getEmail());
            }

            tx.commit();

            // If user exists
            if (user != null) {
                logger.info("Checking credentials...");
                // If client provided password matches: return OK status
                if(user.getPassword().equals(loginData.getPassword())){
                    logger.info("Login succesful for user '{}'", user.getEmail());
                    return Response.ok().build();
                } else {    // If password does not match: return UNAUTHORIZED status
                    return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
            }
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
        }
    }

    @POST
    @Path("/user/modify")
    public Response modifyUser(UserData userData) {
        return null;
    }

    @POST
    @Path("/user/delete")
    public Response deleteUser(String email) {
        return null;
    }

    @POST
    @Path("/user/get")
    public Response getUser(int id) {
        return null;
    }

    @POST
    @Path("/user/getAll")
    public Response getAllUsers() {
        return null;
    }

    /*
     * CRUD: Booking
     */

    @POST
    @Path("/booking/get")
    public Response getBooking(int id) {
        return null;
    }

    @POST
    @Path("/booking/getAll")
    public Response getAllBookings() {
        return null;
    }

    @POST
    @Path("/booking/create")
    public Response createBooking(BookingData bookingData) {
        return null;
    }

    @POST
    @Path("/booking/modify")
    public Response modifyBooking(BookingData bookingData) {
        return null;
    }

    @POST
    @Path("/booking/delete")
    public Response deleteBooking(int id) {
        return null;
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

    @POST
    @Path("/airline/get")
    public Response getAirline(int id) {
        return null;
    }

    @POST
    @Path("/airline/getAll")
    public Response getAllAirlines() {
        return null;
    }

    @POST
    @Path("/airline/create")
    public Response createAirline(AirlineData airlineData) {
        return null;
    }

    @POST
    @Path("/airline/modify")
    public Response modifyAirline(AirlineData airlineData) {
        return null;
    }

    @POST
    @Path("/airline/delete")
    public Response deleteAirline(int id) {
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
