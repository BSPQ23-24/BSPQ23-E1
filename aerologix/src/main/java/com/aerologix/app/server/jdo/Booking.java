package com.aerologix.app.server.jdo;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
/**
 * @brief Represents a booking entity in the system.
 * 
 * <p>
 * This class is annotated for persistence with JDO (Java Data Objects). It includes
 * details about a booking such as the associated passenger, flight, user, and airline.
 * The class provides getter and setter methods to access and modify the fields.
 */
@PersistenceCapable
public class Booking {
	/** The unique identifier for the booking, generated incrementally. */
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    protected Integer id = null;
    /** The passenger associated with the booking. */
    @Persistent(defaultFetchGroup = "true")
    protected Passenger passenger = null;
    /** The flight associated with the booking. */
    protected Flight flight = null;
    /** The user who made the booking. */
    @Persistent(defaultFetchGroup = "true")
    protected User user = null;
    /** The airline associated with the booking. */
    @Persistent(defaultFetchGroup = "true")
    protected Airline airline = null;
    /**
     * Default constructor initializing fields to default values.
     */
    public Booking() {
        super();
        this.passenger = null;
        this.flight = null;
        this.user = null;
        this.airline = null;
    }
    /**
     * Parameterized constructor to initialize a booking with the specified values.
     *
     * @param id The unique identifier for the booking.
     * @param passenger The passenger associated with the booking.
     * @param flight The flight associated with the booking.
     * @param user The user who made the booking.
     * @param airline The airline associated with the booking.
     */
    public Booking(int id, Passenger passenger, Flight flight, User user, Airline airline) {
        super();
        this.id = id;
        this.passenger = passenger;
        this.flight = flight;
        this.user = user;
        this.airline = airline;
    }

    /**
     * Gets the unique identifier for the booking.
     *
     * @return The ID of the booking.
     */
    public int getId() {
        return this.id;
    }
    /**
     * Sets the unique identifier for the booking.
     *
     * @param id The new ID of the booking.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Gets the passenger associated with the booking.
     *
     * @return The passenger.
     */
    public Passenger getPassenger() {
        return this.passenger;
    }
    /**
     * Sets the passenger associated with the booking.
     *
     * @param passenger The new passenger.
     */
    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
    /**
     * Gets the flight associated with the booking.
     *
     * @return The flight.
     */
    public Flight getFlight() {
        return this.flight;
    }
    /**
     * Sets the flight associated with the booking.
     *
     * @param flight The new flight.
     */
    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    /**
     * Gets the user who made the booking.
     *
     * @return The user.
     */
    public User getUser() {
        return this.user;
    }
    /**
     * Sets the user who made the booking.
     *
     * @param user The new user.
     */
    public void setUser(User user) {
        this.user = user;
    }
    /**
     * Gets the airline associated with the booking.
     *
     * @return The airline.
     */
    public Airline getAirline() {
        return this.airline;
    }
    /**
     * Sets the airline associated with the booking.
     *
     * @param airline The new airline.
     */
    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    /**
     * Returns a string representation of the booking.
     *
     * @return A string representing the booking.
     */
    public String toString() {
        return "Booking[id=" + this.getId() + ", passenger="+ this.getPassenger().getDNI() + ", flight=" + this.getFlight().getIdFlight() + ", user=" + this.getUser().getEmail() + "]";
    }

}