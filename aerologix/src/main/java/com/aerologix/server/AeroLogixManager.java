package com.aerologix.server;

import java.time.LocalDate;
import java.util.HashMap;
import com.aerologix.serialization.*;

public class AeroLogixManager {

	protected HashMap<String, Booking> bookings;
	protected HashMap<String, Flight> flights;
	protected HashMap<String, Airline> airlines;

///////BOOKINGS////////	

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

///////////////////// 

///////FLIGHTS////////   

	// Retrieve flight
	public Flight getFlight(String idFlight) {
		return flights.get(idFlight);
	}

	// Create Flight
	public void addFlight(Flight flight) {
		this.flights.put(flight.getIdFlight(), flight);
	}

	// Modify Flight
	public void modifyFlight(String idFlight, String origin, String destination, LocalDate date, Aircraft aircraft) {
		Flight flight = getFlight(idFlight);
		flight.setIdFlight(idFlight);
		flight.setOrigin(origin);
		flight.setDestination(destination);
		flight.setDate(date);
		flight.setAircraft(aircraft);
	}

	// Delete Flight
	public void deleteFlight(String idFlight) {
		flights.remove(idFlight);
	}
/////////////////////    

//////AIRLINES/////// 

	// Retrieve airline
	public Airline getAirline(String id) {
		return airlines.get(id);
	}

	// Create airline
	public void addAirline(Airline airline) {
		this.airlines.put(airline.getId(), airline);
	}

	// Modify airline
	public void modifyAirline(String id, String name) {
		Airline airline = getAirline(id);
		airline.setName(name);
		airlines.replace(id, airline);
	}

	// Delete airline
	public void deleteAirline(String id) {
		airlines.remove(id);
	}

}
