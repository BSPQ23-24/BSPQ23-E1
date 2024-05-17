package com.aerologix.app.server;

import java.util.ArrayList;

import javax.jdo.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.aerologix.app.server.jdo.*;
import com.aerologix.app.server.pojo.*;
import com.aerologix.app.server.service.*;

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
	
//	public static void main(String[] args) {
//		UserService us = new UserService();
//		PassengerService ps = new PassengerService();
//		FlightService fs = new FlightService();
//		AirlineService as = new AirlineService();
//		AircraftService acs = new AircraftService();
//		BookingService bs = new BookingService();
//		
//		// Create a user
//		UserData ud = new UserData();
//		ud.setEmail("user1");
//		ud.setPassword("user1");
//		ud.setName("User1");
//		ud.setUserType("COUNTER_CLERK");
//		
//		us.registerUser(ud);
//		
//		// Create a passenger
//		PassengerData pd = new PassengerData();
//		pd.setDNI("dni1");
//		pd.setPhone(123);
//		pd.setName("Passenger1");
//		pd.setEmail("passenger1");
//		pd.setNationality("Spain");
//		pd.setBirthdate(0);
//		
//		ps.createPassenger(pd);
//		
//		// Create an airline
//		AirlineData ad = new AirlineData();
//		ad.setName("Iberia");
//		
//		as.createAirline(ad);
//		
//		// Create an aircraft
//		AircraftData acd = new AircraftData();
//		acd.setManufacturer("Manufacturer");
//		acd.setMaxCapacity(300);
//		acd.setType("Type");
//		
//		acs.createAircraft(acd);
//		
//		// Create a flight
//		FlightData fd = new FlightData();
//		fd.setOrigin("Origin");
//		fd.setDestination("Destination");
//		fd.setDate(System.currentTimeMillis());
//		fd.setAircraftId(1);
//		fd.setBookingIds(new ArrayList<Integer>());
//		
//		fs.createFlight(fd);
//		
//		// Create a booking
//		BookingData bd = new BookingData();
//		bd.setPassengerDNI("dni1");
//		bd.setFlightId(1);
//		bd.setUserEmail("user1");
//		bd.setAirlineId(1);
//		
//		bs.createBooking(bd);
//	}
	
}


