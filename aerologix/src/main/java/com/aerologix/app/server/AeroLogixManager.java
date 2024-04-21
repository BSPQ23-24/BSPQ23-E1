package com.aerologix.app.server;

import java.util.HashMap;
import com.aerologix.app.server.jdo.*;


public class AeroLogixManager {
  
    protected HashMap<Integer, Booking> bookings;
	protected HashMap<Integer, Flight> flights;
	protected HashMap<Integer, Airline> airlines;
    protected HashMap<String, Passenger> passengers;
	protected HashMap<Integer, Aircraft> aircrafts;
	protected HashMap<String, User> users;

	public AeroLogixManager() {
        this.bookings = new HashMap<Integer, Booking>();
        this.flights = new HashMap<Integer, Flight>();
        this.airlines = new HashMap<Integer, Airline>();
        this.passengers = new HashMap<String, Passenger>();
        this.aircrafts = new HashMap<Integer, Aircraft>();
		this.users = new HashMap<String, User>();
    }

	public AeroLogixManager(HashMap<Integer, Booking> bookings, HashMap<Integer, Flight> flights, HashMap<Integer, Airline> airlines,HashMap<String, Passenger> passengers, HashMap<Integer, Aircraft> aircrafts, HashMap<String, User> users) {
        this.bookings = bookings;
        this.flights = flights;
        this.airlines = airlines;
        this.passengers = passengers;
        this.aircrafts = aircrafts;
		this.users = users;
    }

///////BOOKINGS////////	

	// Retrieve booking
	public Booking getBooking(Integer id) {
		return bookings.get(id);
	}

	// Create booking
	public void addBooking(Booking booking) {
		this.bookings.put(booking.getId(), booking);
	}

	// Modify booking
	public void modifyBooking(Integer id, Passenger passenger, Flight flight, User user) {
		Booking booking = getBooking(id);
		booking.setPassenger(passenger);
		booking.setFlight(flight);
		booking.setUser(user);
		bookings.replace(id, booking);
	}

	// Delete booking
	public void deleteBooking(Integer id) {
		bookings.remove(id);
	}

///////////////////// 

///////FLIGHTS////////   

	// Retrieve flight
	public Flight getFlight(Integer idFlight) {
		return flights.get(idFlight);
	}

	// Create Flight
	public void addFlight(Flight flight) {
		this.flights.put(flight.getIdFlight(), flight);
	}

	// Modify Flight
	public void modifyFlight(Integer idFlight, String origin, String destination, long date, Aircraft aircraft) {
		Flight flight = getFlight(idFlight);
		flight.setIdFlight(idFlight);
		flight.setOrigin(origin);
		flight.setDestination(destination);
		flight.setDate(date);
		flight.setAircraft(aircraft);
	}

	// Delete Flight
	public void deleteFlight(Integer idFlight) {
		flights.remove(idFlight);
	}
/////////////////////    

//////AIRLINES/////// 

	// Retrieve airline
	public Airline getAirline(Integer id) {
		return airlines.get(id);
	}

	// Create airline
	public void addAirline(Airline airline) {
		this.airlines.put(airline.getId(), airline);
	}

	// Modify airline
	public void modifyAirline(Integer id, String name) {
		Airline airline = getAirline(id);
		airline.setName(name);
		airlines.replace(id, airline);
	}

	// Delete airline
	public void deleteAirline(Integer id) {
		airlines.remove(id);
	}
  
/////////////////////    

//////PASSENGERS/////// 

    // Retrieve Passenger
    public Passenger getPassenger(String dni) {
        return passengers.get(dni);
    }

    // Create Passenger
    public void addPassenger(Passenger passenger) {
        this.passengers.put(passenger.getDNI(), passenger);
    }

    // Modify Passenger
    public void modifyPassenger(String dni, int phone, String name, String email, String nationality, long birthdate) {
        Passenger passenger = getPassenger(dni);
        passenger.setPhone(phone);
        passenger.setName(name);
        passenger.setEmail(email);
        passenger.setNationality(nationality);
        passenger.setBirthdate(birthdate);
        this.passengers.replace(dni, passenger);
    }

    // Delete Passenger
    public void deletePassenger(String dni) {
        passengers.remove(dni);
    }

/////////////////////    


//////AIRCRAFT/////// 

    public Aircraft getAircraft(Integer id) {
        return aircrafts.get(id);
    }

    public void addAircraft(Aircraft aircraft) {
       
            aircrafts.put(aircraft.getId(), aircraft);
        
    }

    public void deleteAircraft(Integer id) {
        aircrafts.remove(id);
    }

 	public void modifyAircraft(Integer id, String manufacturer, String type, int maxCapacity) {
        Aircraft aircraft = getAircraft(id);
        aircraft.setManufacturer(manufacturer);
        aircraft.setType(type);
        aircraft.setMaxCapacity(maxCapacity);
            
        
    }

/////////////////////

//////USERS/////// 

	// Retrieve User
	public User getUser(String id) {
		return users.get(id);
	}

	// Create User
	public void addUser(User user) {
		this.users.put(user.getEmail(), user);
	}



	// Modify User	
	public void modifyUser(String id, String password, User.UserType userType, String name) {
		User user = getUser(id);
		user.setPassword(password);
		user.setUserType(userType);
		user.setName(name);
		users.replace(id, user);
	}

	// Delete User
	public void deleteUser(String id) {
		users.remove(id);
	}


}
