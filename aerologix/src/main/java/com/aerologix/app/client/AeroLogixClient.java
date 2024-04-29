package com.aerologix.app.client;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;

import com.aerologix.app.client.gui.LoginWindow;
import com.aerologix.app.server.pojo.*;

public class AeroLogixClient {
    
    protected static final Logger logger = LogManager.getLogger();

    private Client client;
    private WebTarget webTarget;

    public AeroLogixClient(String hostname, String port) {
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target(String.format("http://%s:%s/rest/aerologix", hostname, port));
    }

    /*
     * CRUD: user
     */

    public int registerUser(String email, String password, String userType, String name) {
        WebTarget registerUserWebTarget = webTarget.path("/user/register");
        Invocation.Builder invocationBuilder = registerUserWebTarget.request(MediaType.APPLICATION_JSON);

        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);
        userData.setUserType(userType);
        userData.setName(name);

        Response response = invocationBuilder.post(Entity.entity(userData, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Email '{}' is already in use. Code: {}", userData.getEmail() ,response.getStatus());
            return -1;
        } else {
            logger.info("User correctly registered: {}", userData);
            return 0;
        }
    }

    public boolean login(String email, String password) {
        WebTarget loginWebTarget = webTarget.path("/user/login");
        Invocation.Builder invocationBuilder = loginWebTarget.request(MediaType.APPLICATION_JSON);

        LoginData loginData = new LoginData();
        loginData.setEmail(email);
        loginData.setPassword(password);

        Response response = invocationBuilder.post(Entity.entity(loginData, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Login credentials invalid. Code: {}", response.getStatus());
            return false;
        } else {
            logger.info("Login successful for user: {}", loginData.getEmail());
            return true;
        }
    }

    public int modifyUser(String email, String password, String userType, String name) {
        WebTarget modifyUserWebTarget = webTarget.path("/user/modify");
        Invocation.Builder invocationBuilder = modifyUserWebTarget.request(MediaType.APPLICATION_JSON);

        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);
        userData.setUserType(userType);
        userData.setName(name);

        Response response = invocationBuilder.post(Entity.entity(userData, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no user registered with that email. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("User '{}' modified succesfully", userData.getEmail());
            return 0;
        }
    }

    public int deleteUser(String email) {
        WebTarget deleteUserWebTarget = webTarget.path("/user/delete");
        Invocation.Builder invocationBuilder = deleteUserWebTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(Entity.entity(email, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no user registered with that email. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("User '{}' deleted succesfully", email);
            return 0;
        }
    }

    public UserData getUser(String email) {
        WebTarget getUserWebTarget = webTarget.path("/user/get").queryParam("email", email);
        Invocation.Builder invocationBuilder = getUserWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("User retrieved succesfully");
            return response.readEntity(UserData.class);
        } else {
            logger.error("Failed to get user '{}'. Status code: {}", email, response.getStatus());
            return null;
        }
    }

    public ArrayList<UserData> getAllUsers() {
        WebTarget getAllUsersWebTarget = webTarget.path("/user/getAll");
        Invocation.Builder invocationBuilder = getAllUsersWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
    
        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("All users retrieved successfully");
            return response.readEntity(new GenericType<ArrayList<UserData>>() {});
        } else if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
            logger.error("No users registered");
            return new ArrayList<UserData>(); // Return an empty list if no users are registered
        } else {
            logger.error("Failed to get all users. Status code: {}", response.getStatus());
            return null;
        }
    }

    /*
     * CRUD: booking
     */

    public int createBooking(String passengerDNI, int flightId, String userEmail, int airlineId) {
        WebTarget registerBookingWebTarget = webTarget.path("/booking/create");
        Invocation.Builder invocationBuilder = registerBookingWebTarget.request(MediaType.APPLICATION_JSON);

        BookingData bookingData = new BookingData();
        bookingData.setPassengerDNI(passengerDNI);
        bookingData.setFlightId(flightId);
        bookingData.setUserEmail(userEmail);
        bookingData.setAirlineId(airlineId);

        Response response = invocationBuilder.post(Entity.entity(bookingData, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Cannot create a booking with data that does not exist in the database. Error code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Booking correctly registered");
            return 0;
        }
    }

    public int deleteBooking(String bookingId) {
        WebTarget deleteBookingWebTarget = webTarget.path("/booking/delete");
        Invocation.Builder invocationBuilder = deleteBookingWebTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(Entity.entity(bookingId, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no booking with id {}. Code: {}", bookingId, response.getStatus());
            return -1;
        } else {
            logger.info("Booking '{}' deleted succesfully", bookingId);
            return 0;
        }
    }

    public int modifyBooking(int id, String passengerDNI, int flightId, String userEmail, int airlineId) {
        WebTarget modifyBookingWebTarget = webTarget.path("/booking/modify");
        Invocation.Builder invocationBuilder = modifyBookingWebTarget.request(MediaType.APPLICATION_JSON);

        BookingData bookingData = new BookingData();
        bookingData.setId(id);
        bookingData.setPassengerDNI(passengerDNI);
        bookingData.setFlightId(flightId);
        bookingData.setUserEmail(userEmail);
        bookingData.setAirlineId(airlineId);

        Response response = invocationBuilder.post(Entity.entity(bookingData, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no booking with that id. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Booking '{}' modified succesfully", bookingData.getId());
            return 0;
        }
    }
    
    public int createAirline(String name, int airlineId) {
        WebTarget registerAirlineWebTarget = webTarget.path("/airline/create");
        Invocation.Builder invocationBuilder = registerAirlineWebTarget.request(MediaType.APPLICATION_JSON);

        AirlineData airlineData = new AirlineData();
        airlineData.setId(airlineId);
        airlineData.setName(name);

        Response response = invocationBuilder.post(Entity.entity(airlineData, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Cannot create a airline with data that does not exist in the database. Error code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Airline correctly registered");
            return 0;
        }
    }

    public BookingData getBooking(int id) {
        WebTarget getUserWebTarget = webTarget.path("/booking/get").queryParam("id", id);
        Invocation.Builder invocationBuilder = getUserWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("Booking retrieved succesfully");
            return response.readEntity(BookingData.class);
        } else {
            logger.error("Failed to get booking '{}'. Status code: {}", id, response.getStatus());
            return null;
        }
    }
    
    public int deleteAirline(String airlineId) {
        WebTarget deleteAirlineWebTarget = webTarget.path("/airline/delete");
        Invocation.Builder invocationBuilder = deleteAirlineWebTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.post(Entity.entity(airlineId, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no airline with id {}. Code: {}", airlineId, response.getStatus());
            return -1;
        } else {
            logger.info("Airline '{}' deleted succesfully", airlineId);
            return 0;
        }
    }
    
    public int modifyAirline(int id, String name) {
        WebTarget modifyAirlineWebTarget = webTarget.path("/airline/modify");
        Invocation.Builder invocationBuilder = modifyAirlineWebTarget.request(MediaType.APPLICATION_JSON);

        AirlineData airlineData = new AirlineData();
        airlineData.setId(id);
        airlineData.setName(name);

        Response response = invocationBuilder.post(Entity.entity(airlineData, MediaType.APPLICATION_JSON));
        if(response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("There is no airline with that id. Code: {}", response.getStatus());
            return -1;
        } else {
            logger.info("Airline '{}' modified succesfully", airlineData.getId());
            return 0;
        }
    }

    public ArrayList<BookingData> getAllBookings() {
        WebTarget getAllUsersWebTarget = webTarget.path("/booking/getAll");
        Invocation.Builder invocationBuilder = getAllUsersWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
    
        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("All bookings retrieved successfully");
            return response.readEntity(new GenericType<ArrayList<BookingData>>() {});
        } else if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
            logger.error("No bookings registered");
            return new ArrayList<BookingData>(); // Return an empty list if no bookings are registered
        } else {
            logger.error("Failed to get all bookings. Status code: {}", response.getStatus());
            return null;
        }
    }
    
    public AirlineData getAirline(int id) {
        WebTarget getAirlineWebTarget = webTarget.path("/airline/get").queryParam("id", id);
        Invocation.Builder invocationBuilder = getAirlineWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("Airline retrieved succesfully");
            return response.readEntity(AirlineData.class);
        } else {
            logger.error("Failed to get airline '{}'. Status code: {}", id, response.getStatus());
            return null;
        }
    }
    
    public ArrayList<AirlineData> getAllAirlines(){
    	WebTarget getAllAirlinesWebTarget = webTarget.path("/airline/getAll");
        Invocation.Builder invocationBuilder = getAllAirlinesWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
    
        if (response.getStatus() == Status.OK.getStatusCode()) {
            logger.info("All airlines retrieved successfully");
            return response.readEntity(new GenericType<ArrayList<AirlineData>>() {});
        } else if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
            logger.error("No airlines registered");
            return new ArrayList<AirlineData>(); // Return an empty list if no airlines are registered
        } else {
            logger.error("Failed to get all airlines. Status code: {}", response.getStatus());
            return null;
        }
    }
    
    // Main
    public static void main(String[] args) {
        if (args.length != 2) {
            logger.info("Use: java Client.Client [host] [port]");
            System.exit(0);
        }

        String hostname = args[0];
        String port = args[1];

        AeroLogixClient aerologixClient = new AeroLogixClient(hostname, port);
        
        // Sample data
        aerologixClient.registerUser("juan@mail.es", "juan1234", "COUNTER_CLERK", "Juan");
        aerologixClient.registerUser("admin", "admin", "ADMIN", "admin");
        aerologixClient.registerUser("userTest@mail.com", "test1234", "COUNTER_CLERK", "Test");

        // Login Window
        LoginWindow lw = LoginWindow.getInstanceLogin(aerologixClient);
        lw.setVisible(true);

        // Modify user
        aerologixClient.modifyUser("juan@mail.es", "juan2002", "COUNTER_CLERK", "Juan O.");
        aerologixClient.deleteUser("admin");

        // Get all users
        ArrayList<UserData> userList = aerologixClient.getAllUsers();
        
        for(UserData user : userList) {
            System.out.println(user.getEmail());
        }

        // Create booking
        aerologixClient.createBooking("12345678A", 0, "prueba@mail.com", 0);
        aerologixClient.createBooking("00000000C", 0, "prueba@mail.com", 0);
        aerologixClient.deleteBooking("51");

        // Modify booking
        aerologixClient.modifyBooking(71, "00000000B", 0, "juan@mail.es", 0);

        // Get booking
        System.out.println(aerologixClient.getBooking(71));
        
        // Get all bookings
        ArrayList<BookingData> bookingList = aerologixClient.getAllBookings();
        
        for(BookingData booking : bookingList) {
            System.out.println(booking.getId());
        }
    }
}
