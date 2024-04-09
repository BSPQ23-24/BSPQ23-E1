package com.aerologix.server;

import java.util.HashMap;
import com.aerologix.serialization.*;
import java.time.LocalDate;

public class AeroLogixManager {
    
    protected HashMap<String, Booking> bookings;

    protected HashMap<String, Passenger> passenger;

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

    // Retrieve Passenger
    public Passenger getPassenger(String dni) {
        return passenger.get(dni);
    }

    // Create Passenger
    public void addPassenger(Passenger passenger) {
        this.passenger.put(passenger.getDNI(), passenger);
    }

    // Modify Passenger
    public void modifyPassenger(String dni, int phone, String name, String email, String nationality, LocalDate birthdate) {
        Passenger passenger = getPassenger(dni);
        passenger.setPhone(phone);
        passenger.setName(name);
        passenger.setEmail(email);
        passenger.setNationality(nationality);
        passenger.setBirthdate(birthdate);
        this.passenger.replace(dni, passenger);
    }

    // Delete Passenger
    public void deletePassenger(String dni) {
        passenger.remove(dni);
    }




}
