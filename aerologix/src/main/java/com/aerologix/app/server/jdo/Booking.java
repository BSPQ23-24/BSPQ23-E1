package com.aerologix.app.server.jdo;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Booking {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    protected Integer id = null;

    @Persistent(defaultFetchGroup = "true")
    protected Passenger passenger = null;

    protected Flight flight = null;

    @Persistent(defaultFetchGroup = "true")
    protected User user = null;

    @Persistent(defaultFetchGroup = "true")
    protected Airline airline = null;

    public Booking() {
        super();
        this.passenger = null;
        this.flight = null;
        this.user = null;
        this.airline = null;
    }

    public Booking(int id, Passenger passenger, Flight flight, User user, Airline airline) {
        super();
        this.id = id;
        this.passenger = passenger;
        this.flight = flight;
        this.user = user;
        this.airline = airline;
    }

    // Getters and setters
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Passenger getPassenger() {
        return this.passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Flight getFlight() {
        return this.flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Airline getAirline() {
        return this.airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    // Print the instance
    public String toString() {
        return "Booking[id=" + this.getId() + ", passenger="+ this.getPassenger().getDNI() + ", flight=" + this.getFlight().getIdFlight() + ", user=" + this.getUser().getEmail() + "]";
    }

}