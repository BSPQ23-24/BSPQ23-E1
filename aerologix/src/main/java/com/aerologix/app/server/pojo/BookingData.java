package com.aerologix.app.server.pojo;

public class BookingData {
    protected Integer id = null;
    protected String passengerDNI;
    protected Integer flightId;
    protected String userEmail;
    protected Integer airlineId;
    
    public BookingData() {
        // required by serialization
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassengerDNI() {
        return this.passengerDNI;
    }

    public void setPassengerDNI(String passengerDNI) {
        this.passengerDNI = passengerDNI;
    }

    public Integer getFlightId() {
        return this.flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getAirlineId() {
        return this.airlineId;
    }

    public void setAirlineId(Integer airlineId) {
        this.airlineId = airlineId;
    }

    // Print the instance
    public String toString() {
    	if(this.id != null) {
    		return "Booking[id=" + this.getId() + ", passenger="+ this.getPassengerDNI() + ", flight=" + this.getFlightId() + ", user=" + this.getUserEmail() + "]";
    	} else {
    		return "Booking[id=NOT_DEFINED_YET" + ", passenger="+ this.getPassengerDNI() + ", flight=" + this.getFlightId() + ", user=" + this.getUserEmail() + "]";
    	}
        
    }

}
