package com.aerologix.app.server.jdo;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Flight {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    protected Integer idFlight = null;

    @Persistent
    protected String origin = null;

    @Persistent
    protected String destination = null;

    @Persistent
    protected long date;

    @Persistent(defaultFetchGroup = "true")
    protected Aircraft aircraft = null;

    @Persistent(mappedBy="flight", dependentElement="true")
	@Join
    protected Set<Booking> bookings = new HashSet<>();
	
    public Flight() {
        super();
        this.origin = null;
        this.destination = null;
        this.date = -1;
        this.aircraft = null;
    }

	public Flight(int idFlight, String origin, String destination, long date, Aircraft aircraft, Set<Booking> bookings) {
		super();
		this.idFlight = idFlight;
		this.origin = origin;
		this.destination = destination;
		this.date = date;
		this.aircraft = aircraft;
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

	public Set<Booking> getBookings() {
		return this.bookings;
	}

	public void setBookings(Set<Booking> bookings) {
		this.bookings = bookings;
	}
	
	public String toString() {
  
		return "Flight[id=" + this.getIdFlight() + ", Origin="+ this.getOrigin() + ", Destination=" + this.getDestination() + ", Date=" + this.getDate() + ", Aircraft=" + this.getAircraft().toString() + "]";
    }
	
}
