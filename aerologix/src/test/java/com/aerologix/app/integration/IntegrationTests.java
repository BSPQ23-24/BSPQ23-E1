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
    public void login() {
        logger.info("User login integration test");
        boolean loginSuccess = UserController.getInstance().login("testUser", "test");
        assertTrue(loginSuccess);    // Login is correct, then result should be true
    }

    @Test
    @Order(3)
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
    @Order(4)
    public void createAirline() {
        logger.info("Create airline integration test");
        int result = AirlineController.getInstance(AeroLogixClient.getInstance()).createAirline("Test Airline");
        assertEquals(0, result);
    }

    @Test
    @Order(5)
    public void createAircraft() {
        logger.info("Create aircraft integration test");
        int result = AircraftController.getInstance(AeroLogixClient.getInstance()).createAircraft("Test Manufacturer", "Test Type", -1);
        assertEquals(0, result);
    }

    @Test
    @Order(6)
    public void createFlight() {
        logger.info("Create flight integration test");
        ArrayList<AircraftData> aircrafts = AircraftController.getInstance(AeroLogixClient.getInstance()).getAllAircrafts();
        int result = FlightController.getInstance(AeroLogixClient.getInstance()).createFlight("Test origin", "Test destination", 0, aircrafts.get(0).getId());
        assertEquals(0, result);
    }

    @Test
    @Order(7)
    public void createBooking() {
        logger.info("Create booking integration test");
        ArrayList<FlightData> flights = FlightController.getInstance(AeroLogixClient.getInstance()).getAllFlights();
        int flightId = 0;
        for(FlightData fd : flights) {
            if(fd.getOrigin().equals("Test origin") && fd.getDestination().equals("Test destination")){
                flightId = fd.getIdFlight();
            }
        }
        ArrayList<AirlineData> airlines = AirlineController.getInstance(AeroLogixClient.getInstance()).getAllAirlines();
        int airlineId = 0;
        for(AirlineData ad : airlines) {
            if(ad.getName().equals("Test Airline")) {
               airlineId = ad.getId();
            }
        }

        int result = BookingController.getInstance(AeroLogixClient.getInstance()).createBooking("testPassenger", flightId, "testUser", airlineId);
        assertEquals(0, result);
    }

    @AfterAll
    public static void teardown() {
        // Delete all data created for the tests

        ArrayList<AirlineData> airlines = AirlineController.getInstance(AeroLogixClient.getInstance()).getAllAirlines();
        int airlineId = 0;
        for(AirlineData ad : airlines) {
            if(ad.getName().equals("Test Airline")) {
                airlineId = ad.getId();
            }
        }

        ArrayList<AircraftData> aircrafts =  AircraftController.getInstance(AeroLogixClient.getInstance()).getAllAircrafts();
        int aircraftId = 0;
        for (AircraftData acd : aircrafts) {
            if(acd.getManufacturer().equals("Test Manufacturer") && acd.getType().equals("Test Type") && acd.getMaxCapacity() == -1) {
                aircraftId = acd.getId();
            }
        }

        ArrayList<FlightData> flights = FlightController.getInstance(AeroLogixClient.getInstance()).getAllFlights();
        int flightId = 0;
        for(FlightData fd : flights) {
            if(fd.getOrigin().equals("Test origin") && fd.getDestination().equals("Test destination")){
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
