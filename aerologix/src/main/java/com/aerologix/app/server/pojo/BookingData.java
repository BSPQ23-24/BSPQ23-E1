package com.aerologix.app.server.pojo;
/**
 * A data transfer object (DTO) representing a booking.
 * <p>
 * This class is used to encapsulate booking information and is utilized for communication between different layers of the application.
 */
public class BookingData {
	/** The unique identifier of the booking */
    protected Integer id = null;
    /** The unique identifier of the passenger*/
    protected String passengerDNI;
    /** The unique identifier of the flight*/
    protected Integer flightId;
    /** The unique identifier of the user, that represents the email*/
    protected String userEmail;
    /** The unique identifier of the airline*/
    protected Integer airlineId;
    /**
     * Default constructor.
     * <p>
     * This constructor is required for serialization of the object.
     */
    public BookingData() {
        
    }
    /**
     * Gets the unique identifier of the booking.
     *
     * @return the booking ID.
     */
    public int getId() {
        return this.id;
    }
    /**
     * Sets the unique identifier of the booking.
     *
     * @param id the booking ID.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Gets the unique identifier of the passenger.
     *
     * @return the passenger ID.
     */
    public String getPassengerDNI() {
        return this.passengerDNI;
    }
    /**
     * Sets the unique identifier of the passenger.
     *
     * @param passengerDNI the passenger ID.
     */
    public void setPassengerDNI(String passengerDNI) {
        this.passengerDNI = passengerDNI;
    }
    /**
     * Gets the unique identifier of the flight.
     *
     * @return the flight ID.
     */
    public Integer getFlightId() {
        return this.flightId;
    }
    /**
     * Sets the unique identifier of the flight.
     *
     * @param flightID the flight ID.
     */
    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }
    /**
     * Gets the unique identifier of the user(email).
     *
     * @return the user email.
     */
    public String getUserEmail() {
        return this.userEmail;
    }
    /**
     * Sets the unique identifier of the user.
     *
     * @param userEmail the user ID(email).
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    /**
     * Gets the unique identifier of the airline.
     *
     * @return the airline ID.
     */
    public Integer getAirlineId() {
        return this.airlineId;
    }
    /**
     * Sets the unique identifier of the airline.
     *
     * @param airlineID the airline ID.
     */
    public void setAirlineId(Integer airlineId) {
        this.airlineId = airlineId;
    }

	  /**
     * Returns a string representation of the booking data.
     *
     * @return a string representing the booking.
     */
    public String toString() {
    	if(this.id != null) {
    		return "Booking[id=" + this.getId() + ", passenger="+ this.getPassengerDNI() + ", flight=" + this.getFlightId() + ", user=" + this.getUserEmail() + "]";
    	} else {
    		return "Booking[id=NOT_DEFINED_YET" + ", passenger="+ this.getPassengerDNI() + ", flight=" + this.getFlightId() + ", user=" + this.getUserEmail() + "]";
    	}
        
    }

}
