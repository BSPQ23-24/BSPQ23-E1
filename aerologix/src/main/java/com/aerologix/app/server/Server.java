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
import com.aerologix.app.server.jdo.User.UserType;

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
		try {
			tx.begin();
			logger.info("Checking if user '{}' already exists or not", userData.getEmail());
			User user = null;

			try {
				user = pm.getObjectById(User.class, userData.getEmail());
			} catch (JDOObjectNotFoundException e) {
				logger.info("User with email '{}' does not exist in the database.", userData.getEmail());
			}

			if (user == null) {
				logger.info("Creating user...");
				user = new User(userData.getEmail(), userData.getPassword(),
						User.UserType.valueOf(userData.getUserType()), userData.getName());
				pm.makePersistent(user);
				logger.info("User created: {}", user);
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("Email '{}' is already in use", userData.getEmail());
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("Email is already in use").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/user/login")
	public Response login(LoginData loginData) {
		try {
			tx.begin();
			logger.info("Checking if the user '{}' already exists or not...", loginData.getEmail());
			User user = null;

			try {
				user = pm.getObjectById(User.class, loginData.getEmail());
			} catch (JDOObjectNotFoundException e) {
				logger.info("User with email '{}' does not exist in the database.", loginData.getEmail());
			}

			tx.commit();

			// If user exists
			if (user != null) {
				logger.info("Checking credentials...");
				// If client provided password matches: return OK status
				if (user.getPassword().equals(loginData.getPassword())) {
					logger.info("Login succesful for user '{}'", user.getEmail());
					return Response.ok().build();
				} else { // If password does not match: return UNAUTHORIZED status
					return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
				}
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/user/modify")
	public Response modifyUser(UserData userData) {
		try {
			tx.begin();
			// Get current user data
			logger.info("Get the current user data for: {}", userData.getEmail());
			User user = null;
			try {
				user = pm.getObjectById(User.class, userData.getEmail());
			} catch (JDOObjectNotFoundException e) {
				logger.info("User with email '{}' does not exist in the database.", userData.getEmail());
			}

			if (user != null) {
				// Modify all data except primary key (email)
				user.setName(userData.getName());
				user.setPassword(userData.getPassword());
				user.setUserType(UserType.valueOf(userData.getUserType()));
				logger.info("User modified: {}", user);
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("There is no user registered with email: {}", userData.getEmail());
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("No user found").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/user/delete")
	public Response deleteUser(String email) {
		try {
			tx.begin();
			logger.info("Checking if user {} exists in the database...", email);
			User user = null;
			try {
				user = pm.getObjectById(User.class, email);
			} catch (JDOObjectNotFoundException e) {
				logger.info("User with email '{}' does not exist in the database.", email);
			}

			if (user != null) {
				// Delete user
				pm.deletePersistent(user);
				logger.info("User deleted: {}", email);
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("There is no user registered with email: {}", email);
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("No user found").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@GET
	@Path("/user/get")
	public Response getUser(@QueryParam("email") String email) {
		try {
			tx.begin();
			logger.info("Checking if the user '{}' already exists or not...", email);
			User user = null;

			try {
				user = pm.getObjectById(User.class, email);
			} catch (JDOObjectNotFoundException e) {
				logger.info("User with email '{}' does not exist in the database.", email);
			}

			tx.commit();

			// If user exists
			if (user != null) {
				UserData userData = new UserData();
				userData.setEmail(user.getEmail());
				userData.setPassword(user.getPassword());
				userData.setUserType(user.getUserType().name());
				userData.setName(user.getName());

				logger.info("Sending userData to client...");
				return Response.ok().entity(userData).build();
			} else {
				logger.info("User not found");
				return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@GET
	@Path("/user/getAll")
	public Response getAllUsers() {
		try {
			tx.begin();

			ArrayList<UserData> userList = new ArrayList<UserData>();

			logger.info("Retrieving all users from database...");

			Extent<User> e = pm.getExtent(User.class, true);
			Iterator<User> iter = e.iterator();
			while (iter.hasNext()) {
				User user = (User) iter.next();
				if (!user.getUserType().name().equals("ADMIN")) {
					UserData userData = new UserData();
					userData.setEmail(user.getEmail());
					userData.setPassword(user.getPassword());
					userData.setUserType(user.getUserType().name());
					userData.setName(user.getName());
					userList.add(userData);
				}
			}
			tx.commit();

			if (userList.size() > 0) {
				logger.info("Sending all users to client...");
				return Response.ok().entity(userList).build();
			} else {
				logger.error("No users registered");
				return Response.status(Response.Status.NO_CONTENT).entity("No users registered").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	/*
	 * CRUD: Booking
	 */

	@GET
	@Path("/booking/get")
	public Response getBooking(@QueryParam("id") String id) {
		try {
			tx.begin();
			logger.info("Checking if the booking '{}' already exists or not...", id);
			Booking booking = null;

			try {
				booking = pm.getObjectById(Booking.class, id);
			} catch (JDOObjectNotFoundException e) {
				logger.info("Booking with id '{}' does not exist in the database.", id);
			}

			tx.commit();

			// If booking exists
			if (booking != null) {
				BookingData bookingData = new BookingData();
				bookingData.setId(booking.getId());
				bookingData.setPassengerDNI(booking.getPassenger().getDNI());
				bookingData.setFlightId(booking.getFlight().getIdFlight());
				bookingData.setUserEmail(booking.getUser().getEmail());
				bookingData.setAirlineId(booking.getAirline().getId());

				logger.info("Sending bookingData to client...");
				return Response.ok().entity(bookingData).build();
			} else {
				logger.info("Booking not found");
				return Response.status(Response.Status.NOT_FOUND).entity("Booking not found").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@GET
	@Path("/booking/getAll")
	public Response getAllBooking() {
		try {
			tx.begin();

			ArrayList<BookingData> bookingList = new ArrayList<BookingData>();

			logger.info("Retrieving all bookings from database...");

			Extent<Booking> e = pm.getExtent(Booking.class, true);
			Iterator<Booking> iter = e.iterator();
			while (iter.hasNext()) {
				Booking booking = (Booking) iter.next();
				BookingData bookingData = new BookingData();
				bookingData.setId(booking.getId());
				bookingData.setPassengerDNI(booking.getPassenger().getDNI());
				bookingData.setFlightId(booking.getFlight().getIdFlight());
				bookingData.setUserEmail(booking.getUser().getEmail());
				bookingData.setAirlineId(booking.getAirline().getId());

				bookingList.add(bookingData);
			}
			tx.commit();

			if (bookingList.size() > 0) {
				logger.info("Sending all bookings to client...");
				return Response.ok().entity(bookingList).build();
			} else {
				logger.error("No bookings registered");
				return Response.status(Response.Status.NO_CONTENT).entity("No bookings registered").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/booking/create")
	public Response createBooking(BookingData bookingData) {
		try {
			tx.begin();

			boolean notFound = false;

			// Retrieve all linked classes
			Passenger passenger = null;
			Flight flight = null;
			User user = null;
			Airline airline = null;

			try {
				logger.info("Retrieving all data required for the booking");
				passenger = pm.getObjectById(Passenger.class, bookingData.getPassengerDNI());
				flight = pm.getObjectById(Flight.class, bookingData.getFlightId());
				user = pm.getObjectById(User.class, bookingData.getUserEmail());
				airline = pm.getObjectById(Airline.class, bookingData.getAirlineId());
			} catch (JDOObjectNotFoundException e) {
				logger.info("At least one object of the Booking '{}' not found: {}", bookingData.getId(), e);
				notFound = true;
			}

			// Create Booking instance and make it persistent
			Booking booking = new Booking();
			booking.setPassenger(passenger);
			booking.setFlight(flight);
			booking.setUser(user);
			booking.setAirline(airline);

			if (notFound) {
				tx.commit();
				logger.error(
						"Error when creating a new booking. At least one of the objects related to the booking does not exist.");
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Cannot create a new booking with data that does not exist in the database").build();
			} else {
				pm.makePersistent(booking);
				tx.commit();
				logger.info("Booking created succesfully");
				return Response.ok().build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/booking/modify")
	public Response modifyBooking(BookingData bookingData) {
		try {
			tx.begin();
			// Get current booking data
			logger.info("Get the current booking data for: {}", bookingData.getId());
			Booking booking = null;
			try {
				booking = pm.getObjectById(Booking.class, bookingData.getId());
			} catch (JDOObjectNotFoundException e) {
				logger.info("Booking with id '{}' does not exist in the database.", bookingData.getId());
			}

			if (booking != null) {
				// Retrieve all linked classes
				Passenger passenger = null;
				Flight flight = null;
				User user = null;
				Airline airline = null;
				try {
					logger.info("Retrieving all data required for the modified booking");
					passenger = pm.getObjectById(Passenger.class, bookingData.getPassengerDNI());
					flight = pm.getObjectById(Flight.class, bookingData.getFlightId());
					user = pm.getObjectById(User.class, bookingData.getUserEmail());
					airline = pm.getObjectById(Airline.class, bookingData.getAirlineId());
				} catch (JDOObjectNotFoundException e) {
					logger.info("Could not find all the data for the new values: ", e);
				}

				// Modify all data except primary key (id)
				booking.setId(bookingData.getId());
				booking.setPassenger(passenger);
				booking.setFlight(flight);
				booking.setUser(user);
				booking.setAirline(airline);
				tx.commit();
				logger.info("User modified: {}", user);
				return Response.ok().build();
			} else {
				logger.error("There is no booking with id: {}", bookingData.getId());
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("No booking found").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/booking/delete")
	public Response deleteBooking(int id) {
		try {
			tx.begin();
			logger.info("Checking if booking {} exists in the database...", id);
			Booking booking = null;
			try {
				booking = pm.getObjectById(Booking.class, id);
			} catch (JDOObjectNotFoundException e) {
				logger.info("Booking with id '{}' does not exist in the database.", id);
			}

			if (booking != null) {
				// Delete user
				pm.deletePersistent(booking);
				logger.info("Booking deleted {}");
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("There is no booking with id: {}", id);
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("No booking found").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
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

	@POST
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

			// Create Booking instance and make it persistent
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
			// Get current booking data
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
				return Response.status(Response.Status.UNAUTHORIZED).entity("No booking found").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
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
