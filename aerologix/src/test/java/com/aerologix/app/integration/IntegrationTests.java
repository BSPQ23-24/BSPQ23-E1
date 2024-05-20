package com.aerologix.app.integration;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.client.controller.AircraftController;
import com.aerologix.app.client.controller.AirlineController;
import com.aerologix.app.client.controller.BookingController;
import com.aerologix.app.client.controller.FlightController;
import com.aerologix.app.client.controller.PassengerController;
import com.aerologix.app.client.controller.UserController;
import com.aerologix.app.server.pojo.*;


@Tag("IntegrationTest")
@TestMethodOrder(OrderAnnotation.class)
public class IntegrationTests {

    protected static final Logger logger = LogManager.getLogger();
    
    @BeforeAll
    public static void setup() {
        logger.info("Setting up client connection with server for integration tests");
        AeroLogixClient.getInstance().setWebTarget("127.0.0.1", "8080");
    }

    @Test
    @Order(1)
    public void registerUser() {
        logger.info("User register integration test");
        int result = UserController.getInstance().registerUser("testUser", "test", "COUNTER_CLERK", "Test User");
        assertEquals(0, result);
    }

    @Test
    @Order(2)
    public void getUser() {
        logger.info("Get user integration test");
        UserData ud = UserController.getInstance().getUser("testUser");
        assertEquals("testUser", ud.getEmail());
    }

    @Test
    @Order(3)
    public void getAllUsers() {
        logger.info("Get all users integration test");
        ArrayList<UserData> users = UserController.getInstance().getAllUsers();
        assertNotEquals(0, users.size());
    }

    @Test
    @Order(4)
    public void modifyUser() {
        logger.info("Modify user integration test");
        int result = UserController.getInstance().modifyUser("testUser", "test", "ADMIN", "Test User");
        assertEquals(0, result);
    }

    @Test
    @Order(5)
    public void login() {
        logger.info("User login integration test");
        boolean loginSuccess = UserController.getInstance().login("testUser", "test");
        assertTrue(loginSuccess);    // Login is correct, then result should be true
    }

    @Test
    @Order(6)
    public void createPassenger() {
        logger.info("Create passenger integration test");
        PassengerData pd = new PassengerData();
        pd.setDNI("testPassenger");
        pd.setEmail("testPassenger@gmail.com");
        pd.setPhone(123456789);
        pd.setName("Test Passenger");
        pd.setNationality("Spain");
        int result = PassengerController.getInstance(AeroLogixClient.getInstance()).createPassenger(pd);
        assertEquals(0, result);
    }

    @Test
    @Order(7)
    public void getPassenger() {
        logger.info("Get passenger integration test");
        PassengerData pd = PassengerController.getInstance(AeroLogixClient.getInstance()).getPassenger("testPassenger");
        assertEquals("testPassenger", pd.getDNI());
    }

    @Test
    @Order(8)
    public void getAllPassengers() {
        logger.info("Get all passengers integration test");
        ArrayList<PassengerData> passengers = PassengerController.getInstance(AeroLogixClient.getInstance()).getAllPassengers();
        assertNotEquals(0, passengers.size());
    }

    @Test
    @Order(9)
    public void modifyPassenger() {
        logger.info("Modify passenger integration test");
        PassengerData pd = new PassengerData();
        pd.setDNI("testPassenger");
        pd.setEmail("testPassenger@gmail.com");
        pd.setPhone(123456789);
        pd.setName("Test Passenger");
        pd.setNationality("United States");
        int result = PassengerController.getInstance(AeroLogixClient.getInstance()).modifyPassenger(pd);
        assertEquals(0, result);
    }

    @Test
    @Order(10)
    public void createAirline() {
        logger.info("Create airline integration test");
        int result = AirlineController.getInstance(AeroLogixClient.getInstance()).createAirline("Test Airline");
        assertEquals(0, result);
    }

    @Test
    @Order(11)
    public void getAllAirlines() {
        logger.info("Get all airlines integration test");
        ArrayList<AirlineData> airlines = AirlineController.getInstance(AeroLogixClient.getInstance()).getAllAirlines();
        assertNotEquals(0, airlines.size());
    }

    @Test
    @Order(12)
    public void modifyAirline() {
        logger.info("Modify airline");
        ArrayList<AirlineData> airlines = AirlineController.getInstance(AeroLogixClient.getInstance()).getAllAirlines();
        int result = -1;
        for (AirlineData ad : airlines) {
            if(ad.getName().equals("Test Airline")){
                result = AirlineController.getInstance(AeroLogixClient.getInstance()).modifyAirline(ad.getId(), "Test Airline 2");
            }
        }
        assertEquals(0, result);
    }

    @Test
    @Order(13)
    public void createAircraft() {
        logger.info("Create aircraft integration test");
        int result = AircraftController.getInstance(AeroLogixClient.getInstance()).createAircraft("Test Manufacturer", "Test Type", -1);
        assertEquals(0, result);
    }

    @Test
    @Order(14)
    public void getAllAircrafts() {
        logger.info("Get all aircrafts integration test");
        ArrayList<AircraftData> aircrafts = AircraftController.getInstance(AeroLogixClient.getInstance()).getAllAircrafts();
        assertNotEquals(0, aircrafts.size());
    }

    @Test
    @Order(15)
    public void modifyAircraft() {
        logger.info("Modify aircraft integration test");
        ArrayList<AircraftData> aircrafts = AircraftController.getInstance(AeroLogixClient.getInstance()).getAllAircrafts();
        int result = -1;
        for (AircraftData ad : aircrafts) {
            if(ad.getManufacturer().equals("Test Manufacturer")){
                result = AircraftController.getInstance(AeroLogixClient.getInstance()).modifyAircraft(ad.getId(), "Test Manufacturer", "Test Type", 0);
            }
        }
        assertEquals(0, result);
    }

    @Test
    @Order(16)
    public void createFlight() {
        logger.info("Create flight integration test");
        ArrayList<AircraftData> aircrafts = AircraftController.getInstance(AeroLogixClient.getInstance()).getAllAircrafts();
        int result = FlightController.getInstance(AeroLogixClient.getInstance()).createFlight("Test origin", "Test destination", 0, aircrafts.get(0).getId());
        assertEquals(0, result);
    }

    @Test
    @Order(17)
    public void getAllFlights() {
        logger.info("Get all flights integration test");
        ArrayList<FlightData> flights = FlightController.getInstance(AeroLogixClient.getInstance()).getAllFlights();
        assertNotEquals(0, flights.size());
    }

    @Test
    @Order(18)
    public void modifyFlight() {
        logger.info("Modify flight integration test");
        ArrayList<AircraftData> aircrafts = AircraftController.getInstance(AeroLogixClient.getInstance()).getAllAircrafts();
        ArrayList<FlightData> flights = FlightController.getInstance(AeroLogixClient.getInstance()).getAllFlights();
        int result = -1;
        for(FlightData f : flights) {
            if(f.getOrigin().equals("Test origin") && f.getDestination().equals("Test destination")) {
                result = FlightController.getInstance(AeroLogixClient.getInstance()).modifyFlight(f.getIdFlight(), "Origin", "Destination", 0, aircrafts.get(0).getId(),  f.getBookingIds());
            }
        }
        assertEquals(0, result);
    }

    @Test
    @Order(19)
    public void createBooking() {
        logger.info("Create booking integration test");
        ArrayList<FlightData> flights = FlightController.getInstance(AeroLogixClient.getInstance()).getAllFlights();
        int flightId = 0;
        for(FlightData fd : flights) {
            if(fd.getOrigin().equals("Origin") && fd.getDestination().equals("Destination")){
                flightId = fd.getIdFlight();
            }
        }
        ArrayList<AirlineData> airlines = AirlineController.getInstance(AeroLogixClient.getInstance()).getAllAirlines();
        int airlineId = 0;
        for(AirlineData ad : airlines) {
            if(ad.getName().equals("Test Airline 2")) {
               airlineId = ad.getId();
            }
        }

        int result = BookingController.getInstance(AeroLogixClient.getInstance()).createBooking("testPassenger", flightId, "testUser", airlineId);
        assertEquals(0, result);
    }

    @Test
    @Order(20)
    public void getAllBookings() {
        logger.info("Get all bookings integration test");
        ArrayList<BookingData> bd = BookingController.getInstance(AeroLogixClient.getInstance()).getAllBookings();
        assertNotEquals(0, bd.size());
    }

    @AfterAll
    public static void teardown() {
        // Delete all data created for the tests

        ArrayList<AirlineData> airlines = AirlineController.getInstance(AeroLogixClient.getInstance()).getAllAirlines();
        int airlineId = 0;
        for(AirlineData ad : airlines) {
            if(ad.getName().equals("Test Airline 2")) {
                airlineId = ad.getId();
            }
        }

        ArrayList<AircraftData> aircrafts =  AircraftController.getInstance(AeroLogixClient.getInstance()).getAllAircrafts();
        int aircraftId = 0;
        for (AircraftData acd : aircrafts) {
            if(acd.getManufacturer().equals("Test Manufacturer") && acd.getType().equals("Test Type") && acd.getMaxCapacity() == 0) {
                aircraftId = acd.getId();
            }
        }

        ArrayList<FlightData> flights = FlightController.getInstance(AeroLogixClient.getInstance()).getAllFlights();
        int flightId = 0;
        for(FlightData fd : flights) {
            if(fd.getOrigin().equals("Origin") && fd.getDestination().equals("Destination")){
                flightId = fd.getIdFlight();
            }
        }

        FlightController.getInstance(AeroLogixClient.getInstance()).deleteFlight(flightId);
        PassengerController.getInstance(AeroLogixClient.getInstance()).deletePassenger("testPassenger");
        UserController.getInstance().deleteUser("testUser");
        AirlineController.getInstance(AeroLogixClient.getInstance()).deleteAirline(airlineId);
        AircraftController.getInstance(AeroLogixClient.getInstance()).deleteAircraft(aircraftId);
        
        logger.info("Integration tests ended");
    }
}
