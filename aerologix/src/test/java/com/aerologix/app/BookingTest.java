package com.aerologix.app;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.aerologix.app.server.jdo.Airline;
import com.aerologix.app.server.jdo.Booking;
import com.aerologix.app.server.jdo.Flight;
import com.aerologix.app.server.jdo.Passenger;
import com.aerologix.app.server.jdo.User;
import com.aerologix.app.client.AeroLogixClient;


public class BookingTest {

    Booking booking;
    /*
    @Mock
    Passenger passenger;

    @Mock
    Flight flight;
    
    @Mock
    User user;

    @Mock
    Airline airline;
	*/
    @Before
    public void setUp() {
        /*booking = new Booking();
        booking.setId(1);
        booking.setUser(user);
        booking.setAirline(airline);
        booking.setFlight(flight);
        booking.setPassenger(passenger);*/
        int bookingDone=AeroLogixClient.createBooking("79050089D", 0, "anagonzalez02@opendeusto.es", 1);
    }

    @Test
    public void TestCreateBooking() {
    	assertEquals("79050089D",booking.getPassenger().getDNI());
    }


}