package com.aerologix.server;

import java.util.HashMap;
import com.aerologix.serialization.*;

public class AeroLogixManager {
    
    protected HashMap<String, Booking> bookings;

    // Retrieve booking
    public Booking getBooking(String id) {
        return bookings.get(id);
    }

    // Create booking
    public void addBooking(Booking booking) {
        this.bookings.put(booking.getId(), booking);
    }

    // Modify booking
    public void modifyBooking(String id, Passenger passenger, Flight flight, User user) {
        Booking booking = getBooking(id);
        booking.setPassenger(passenger);
        booking.setFlight(flight);
        booking.setUser(user);
        bookings.replace(id, booking);
    }

    // Delete booking
    public void deleteBooking(String id) {
        bookings.remove(id);
    }
    
    
    protected HashMap<String, Airline> airlines;

    // Retrieve booking
    public Airline getAirline(String id) {
        return airlines.get(id);
    }

    // Create booking
    public void addAirline(Airline airline) {
        this.airlines.put(airline.getId(), airline);
    }

    // Modify booking
    public void modifyAirline(String id, String name) {
        Airline airline = getAirline(id);
        airline.setName(name);
        airlines.replace(id, airline);
    }

    // Delete booking
    public void deleteAirline(String id) {
        airlines.remove(id);
    }

}
