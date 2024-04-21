package com.aerologix.app.server.pojo;

import java.util.List;

public class FlightData {
    
    protected int idFlight;
	protected String origin;
	protected String destination;
	protected long date;
    protected Integer aircraftId;
    protected List<Integer> bookingsIds;
    protected boolean overbooking;

    public FlightData() {
        // required by serialization
    }

    public int getIdFlight() {
		return idFlight;
	}


	public void setIdFlight(int idFlight) {
		this.idFlight = idFlight;
	}


	public String getOrigin() {
		return origin;
	}


	public void setOrigin(String origin) {
		this.origin = origin;
	}


	public String getDestination() {
		return destination;
	}


	public void setDestination(String destination) {
		this.destination = destination;
	}


	public long getDate() {
		return date;
	}


	public void setDate(long date) {
		this.date = date;
	}


	public Integer getAircraftId() {
		return aircraftId;
	}


	public void setAircraftId(Integer aircraftid) {
		this.aircraftId = aircraftid;
	}

	public boolean isOverbooked() {
        return overbooking;
	}

	public void setOverbooking() {
		this.overbooking = true;
	}

	public List<Integer> getBookingIds() {
		return this.bookingsIds;
	}

	public void setBookingIds(List<Integer> bookingIds) {
		this.bookingsIds = bookingIds;
	}
	
	public String toString() {
  
		return "Flight[id=" + this.getIdFlight() + ", Origin="+ this.getOrigin() + ", Destination=" + this.getDestination() + ", Date=" + this.getDate() + ", Aircraft=" + this.getAircraftId() + "]";
    }

    
}
