package com.aerologix.app.server.jdo;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Flight {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
	protected int idFlight;
	protected String origin;
	protected String destination;
	protected long date;

	@Persistent(defaultFetchGroup="true")
	protected Aircraft aircraft;
	
	@Persistent(mappedBy = "flight")
	protected List<Booking> bookings;
	
	public Flight() {
		super();
		this.idFlight = -1;
		this.origin = null;
		this.destination = null;
		this.date = -1;
		this.aircraft = null;
		this.bookings = null;
	}

	public Flight(int idFlight, String origin, String destination, long date, Aircraft aircraft, List<Booking> bookings) {
		super();
		this.idFlight = idFlight;
		this.origin = origin;
		this.destination = destination;
		this.date = date;
		this.aircraft = aircraft;
		this.bookings = bookings;
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


	public Aircraft getAircraft() {
		return aircraft;
	}


	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	public List<Booking> getBookings() {
		return this.bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	public String toString() {
  
		return "Flight[id=" + this.getIdFlight() + ", Origin="+ this.getOrigin() + ", Destination=" + this.getDestination() + ", Date=" + this.getDate() + ", Aircraft=" + this.getAircraft().toString() + "]";
    }
	
}
