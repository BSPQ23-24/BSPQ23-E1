package com.aerologix.app.server.pojo;

import java.util.List;
/**
 * @brief A data transfer object (DTO) representing a flight.
 * 
 * <p>
 * This class is used to encapsulate flight information and is utilized for communication between different layers of the application.
 */
public class FlightData {
    /** The unique identifier of the flight */
    protected int idFlight;
    /** The origin of the flight*/
	protected String origin;
	/** The destination of the flight */
	protected String destination;
	/** The date when the flight will take off. */
	protected long date;
	/**  The unique identifier of the aircraft related to this flight */
    protected Integer aircraftId;
    /** The list of unique identifiers of the bookings associated with this flight. */
    protected List<Integer> bookingsIds;
    /**
     * Default constructor.
     * <p>
     * This constructor is required for serialization of the object.
     */
    public FlightData() {
       
    }
    /**
     * Gets the unique identifier of the flight.
     *
     * @return the flight ID.
     */
    public int getIdFlight() {
		return idFlight;
	}

    /**
     * Sets the unique identifier of the flight.
     *
     * @param idFlight the flight ID.
     */
	public void setIdFlight(int idFlight) {
		this.idFlight = idFlight;
	}

    /**
     * Gets the origin of the flight.
     *
     * @return the origin.
     */
	public String getOrigin() {
		return origin;
	}

    /**
     * Sets the origin of the flight.
     *
     * @param origin the origin.
     */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

    /**
     * Gets the destination of the flight.
     *
     * @return the destination.
     */
	public String getDestination() {
		return destination;
	}

    /**
     * Sets the destination of the flight.
     *
     * @param destination the destination.
     */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	  /**
     * Gets the date when the flight will take off, represented as a Unix timestamp.
     *
     * @return the date.
     */
	public long getDate() {
		return date;
	}

    /**
     * Sets the date when the flight will take off, represented as a Unix timestamp.
     *
     * @param date the date.
     */
	public void setDate(long date) {
		this.date = date;
	}

	  /**
     * Gets the unique identifier of the aircraft related to this flight.
     *
     * @return the aircraft ID.
     */
	public Integer getAircraftId() {
		return aircraftId;
	}

    /**
     * Sets the unique identifier of the aircraft related to this flight.
     *
     * @param aircraftId the aircraft ID.
     */
	public void setAircraftId(Integer aircraftid) {
		this.aircraftId = aircraftid;
	}
	   /**
     * Gets the list of unique identifiers of the bookings associated with this flight.
     *
     * @return the list of booking IDs.
     */
	public List<Integer> getBookingIds() {
		return this.bookingsIds;
	}
    /**
     * Sets the list of unique identifiers of the bookings associated with this flight.
     *
     * @param bookingIds the list of booking IDs.
     */
	public void setBookingIds(List<Integer> bookingIds) {
		this.bookingsIds = bookingIds;
	}
	  /**
     * Returns a string representation of the flight data.
     *
     * @return a string representing the flight.
     */
	
	public String toString() {
  
		return "Flight[id=" + this.getIdFlight() + ", Origin="+ this.getOrigin() + ", Destination=" + this.getDestination() + ", Date=" + this.getDate() + ", Aircraft=" + this.getAircraftId() + "]";
    }
}
