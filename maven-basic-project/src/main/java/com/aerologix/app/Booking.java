public class Booking {

    // Attributes
    protected String id;
    protected Passenger passenger;
    protected Flight flight;
    protected User user;
    // protected Airline airline; should it be inside the flight as the aircraft?

    // Constructors
    public Booking() {
        this.id = "";
        this.passenger = null;
        this.flight = null;
        this.user = null;
    }

    public Booking(String id, Passenger passenger, Flight flight, User user) {
        this.setId(id);
        this.setPassenger(passenger);
        this.setFlight(flight);
        this.setUser(user);
    }

    // Getters and setters
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
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

    // Print the instance
    public String toString() {
        return "Booking[id=" + this.getId() + ", passenger="+ this.getPassenger().getName() + ", flight=" + this.getFlight().getId() + ", user=" + this.getUser().getDNI() + "]";
    }

}