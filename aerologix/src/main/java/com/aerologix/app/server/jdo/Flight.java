package com.aerologix.app.server.jdo;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
/**
	 * @brief Represents a flight entity in the system.
	 * 
	 * <p>
	 * This class is annotated for persistence with JDO (Java Data Objects). It includes
	 * details about a flight such as origin, destination, date, associated aircraft, and bookings.
	 * The class provides getter and setter methods to access and modify the fields.
	 */
@PersistenceCapable
public class Flight {
	/** The unique identifier for the flight, generated incrementally. */
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    protected Integer idFlight = null;
    /** The origin location of the flight. */
    @Persistent
    protected String origin = null;
    /** The destination location of the flight. */
    @Persistent
    protected String destination = null;
    /** The date of the flight as a long value. */
    @Persistent
    protected long date;
    /** The aircraft associated with the flight. */
    @Persistent(defaultFetchGroup = "true")
    protected Aircraft aircraft = null;
    /** The set of bookings associated with the flight. */
    @Persistent(mappedBy="flight", dependentElement="true")
	@Join
    protected Set<Booking> bookings = new HashSet<>();
    /**
     * Default constructor initializing fields to default values.
     */
    public Flight() {
        super();
        this.origin = null;
        this.destination = null;
        this.date = -1;
        this.aircraft = null;
    }
    /**
     * Parameterized constructor to initialize a flight with the specified values.
     *
     * @param idFlight The unique identifier for the flight.
     * @param origin The origin location of the flight.
     * @param destination The destination location of the flight.
     * @param date The date of the flight.
     * @param aircraft The aircraft associated with the flight.
     * @param bookings The set of bookings associated with the flight.
     */
	public Flight(int idFlight, String origin, String destination, long date, Aircraft aircraft, Set<Booking> bookings) {
		super();
		this.idFlight = idFlight;
		this.origin = origin;
		this.destination = destination;
		this.date = date;
		this.aircraft = aircraft;
	}

    /**
     * Gets the unique identifier for the flight.
     *
     * @return The ID of the flight.
     */
	public int getIdFlight() {
		return idFlight;
	}

    /**
     * Sets the unique identifier for the flight.
     *
     * @param idFlight The new ID of the flight.
     */
	public void setIdFlight(int idFlight) {
		this.idFlight = idFlight;
	}

    /**
     * Gets the origin location of the flight.
     *
     * @return The origin location.
     */
	public String getOrigin() {
		return origin;
	}

    /**
     * Sets the origin location of the flight.
     *
     * @param origin The new origin location.
     */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

    /**
     * Gets the destination location of the flight.
     *
     * @return The destination location.
     */
	public String getDestination() {
		return destination;
	}

    /**
     * Sets the destination location of the flight.
     *
     * @param destination The new destination location.
     */
	public void setDestination(String destination) {
		this.destination = destination;
	}


    /**
     * Gets the date of the flight.
     *
     * @return The date of the flight.
     */
	public long getDate() {
		return date;
	}

    /**
     * Sets the date of the flight.
     *
     * @param date The new date of the flight.
     */
	public void setDate(long date) {
		this.date = date;
	}


    /**
     * Gets the aircraft associated with the flight.
     *
     * @return The aircraft.
     */
	public Aircraft getAircraft() {
		return aircraft;
	}

    /**
     * Sets the aircraft associated with the flight.
     *
     * @param aircraft The new aircraft.
     */
	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}
    /**
     * Gets the set of bookings associated with the flight.
     *
     * @return The set of bookings.
     */
	public Set<Booking> getBookings() {
		return this.bookings;
	}
    /**
     * Sets the set of bookings associated with the flight.
     *
     * @param bookings The new set of bookings.
     */
	public void setBookings(Set<Booking> bookings) {
		this.bookings = bookings;
	}
    /**
     * Returns a string representation of the flight.
     *
     * @return A string representing the flight.
     */
	public String toString() {
  
		return "Flight[id=" + this.getIdFlight() + ", Origin="+ this.getOrigin() + ", Destination=" + this.getDestination() + ", Date=" + this.getDate() + ", Aircraft=" + this.getAircraft().toString() + "]";
    }
	
}
