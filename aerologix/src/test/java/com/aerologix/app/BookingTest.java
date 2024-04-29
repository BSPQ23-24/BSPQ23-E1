package com.aerologix.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.aerologix.app.server.jdo.Airline;
import com.aerologix.app.server.jdo.Booking;
import com.aerologix.app.server.jdo.Flight;
import com.aerologix.app.server.jdo.Passenger;
import com.aerologix.app.server.jdo.User;
import com.aerologix.app.client.AeroLogixClient;


public class BookingTest {

    Booking booking;
    
    @Mock
    Passenger passenger;

    @Mock
    Flight flight;
    
    @Mock
    User user;

    @Mock
    Airline airline;
	
    @BeforeEach
    public void setUp() {
        booking = new Booking();
        booking.setId(1);
        booking.setUser(user);
        booking.setAirline(airline);
        booking.setFlight(flight);
        booking.setPassenger(passenger);
        //int bookingDone=AeroLogixClient.createBooking("79050089D", 0, "anagonzalez02@opendeusto.es", 1);
    }

    @Test
    public void TestCreateBooking() {
    	assertEquals(1,booking.getId());
    }


}