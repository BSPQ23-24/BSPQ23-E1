import java.util.ArrayList;
import java.util.HashMap;

public class AeroLogixManager {
    
    protected HashMap<String, Booking> bookings;

    // Retrieve booking
    public Booking getBooking(String id) {
        return bookings.get(id);
    }

    // Create booking
    public void addBooking(Booking booking) {
        this.bookings.put(booking.getId(), booking);
    }

    // Modify booking
    public void modifyBooking(String id, Passenger passenger, Flight flight, User user) {
        Booking booking = getBooking(id);
        booking.setPassenger(passenger);
        booking.setFlight(flight);
        booking.setUser(user);
        bookings.replace(id, booking);
    }

    // Delete booking
    public void deleteBooking(String id) {
        bookings.remove(id);
    }

}
