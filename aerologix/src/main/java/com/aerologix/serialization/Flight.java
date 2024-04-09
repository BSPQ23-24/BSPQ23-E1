package com.aerologix.serialization;

import java.time.LocalDate;

public class Flight {

	protected String idFlight;
	protected String origin;
	protected String destination;
	protected LocalDate date;
	protected Aircraft aircraft;
	
	
	public Flight(String idFlight, String origin, String destination, LocalDate date, Aircraft aircraft) {
		super();
		this.idFlight = idFlight;
		this.origin = origin;
		this.destination = destination;
		this.date = date;
		this.aircraft = aircraft;
	}


	public String getIdFlight() {
		return idFlight;
	}


	public void setIdFlight(String idFlight) {
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


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public Aircraft getAircraft() {
		return aircraft;
	}


	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}
	
	public String toString() {
  
		return "Flight[id=" + this.getIdFlight() + ", Origin="+ this.getOrigin() + ", Destination=" + this.getDestination() + ", Date=" + this.getDate() + ", Aircraft=" + this.getAircraft().toString() + "]";
    }
	
}
