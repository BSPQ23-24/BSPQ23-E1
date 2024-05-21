package com.aerologix.app.client;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.client.*;

import com.aerologix.app.client.controller.AircraftController;
import com.aerologix.app.client.controller.AirlineController;
import com.aerologix.app.client.controller.BookingController;
import com.aerologix.app.client.controller.FlightController;
import com.aerologix.app.client.controller.PassengerController;
import com.aerologix.app.client.controller.UserController;
import com.aerologix.app.client.gui.*;
import com.aerologix.app.server.pojo.*;

/**
 * @brief This class is the main entry point for the AeroLogix client application.
 * 
 * The {@code AeroLogixClient} class is the main entry point for the AeroLogix client application.
 * It is responsible for initializing the REST client, setting up the web target,
 * and providing methods to interact with the server.
 * <p>
 * This class follows the Singleton design pattern to ensure only one instance is created.
 * </p>
 */
public class AeroLogixClient {
    
    /** Logger for logging purposes */
    protected static final Logger logger = LogManager.getLogger();

    /** Singleton instance of AeroLogixClient */
    private static AeroLogixClient instance;

    /** JAX-RS client */
    private Client client;

    /** Web target for REST API */
    private WebTarget webTarget;

    /**
     * Private constructor to prevent instantiation.
     * Initializes the JAX-RS client and web target using system properties for hostname and port.
     */
    private AeroLogixClient() {
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target(String.format("http://%s:%s/rest/aerologix", System.getProperty("aerologix.hostname"), System.getProperty("aerologix.port")));
    }

    /**
     * Returns the singleton instance of {@code AeroLogixClient}.
     * If the instance does not exist, it is created.
     *
     * @return the singleton instance
     */
    public static AeroLogixClient getInstance() {
        if (instance == null) {
            instance = new AeroLogixClient();
        }
        return instance;
    }

    /**
     * Sets the web target for the REST client using the specified hostname and port.
     *
     * @param hostname the hostname of the server
     * @param port the port of the server
     */
    public void setWebTarget(String hostname, String port) {
        this.webTarget = client.target(String.format("http://%s:%s/rest/aerologix", hostname, port));
    }

    /**
     * Returns the current web target.
     *
     * @return the web target
     */
    public WebTarget getWebTarget() {
        return this.webTarget;
    }

    /**
     * Demonstrates the functionality of the client by initializing data.
     * <p>
     * This method registers users, creates aircraft, airlines, passengers, flights, and bookings.
     * </p>
     */
    public void initializeData() {

        // User
        UserController.getInstance().registerUser("admin", "admin", "ADMIN", "AeroLogix Administrator");
        UserController.getInstance().registerUser("marta.sanchez@aena.es", "marta1234", "COUNTER_CLERK", "Marta Sanchez");
        UserController.getInstance().registerUser("pepeprueba@mail.com", "pepeprueba", "COUNTER_CLERK", "Pepe Prueba");

        // Aircraft
        AircraftController.getInstance(AeroLogixClient.getInstance()).createAircraft("Boeing", "737", 200);
        AircraftController.getInstance(AeroLogixClient.getInstance()).createAircraft("Boeing", "747", 300);
        AircraftController.getInstance(AeroLogixClient.getInstance()).createAircraft("Airbus", "A380", 500);
        AircraftController.getInstance(AeroLogixClient.getInstance()).createAircraft("Airbus", "A330", 400);

        // Airline
        AirlineController.getInstance(AeroLogixClient.getInstance()).createAirline("Iberia");
        AirlineController.getInstance(AeroLogixClient.getInstance()).createAirline("Vueling");
        AirlineController.getInstance(AeroLogixClient.getInstance()).createAirline("Volotea");
        AirlineController.getInstance(AeroLogixClient.getInstance()).createAirline("Ryanair");

        // Passenger
        PassengerData passenger1 = new PassengerData();
        passenger1.setDNI("79000000H");
        passenger1.setEmail("prueba@mail.com");
        passenger1.setName("Passenger 1");
        passenger1.setNationality("Spain");
        passenger1.setPhone(600000000);
        passenger1.setBirthdate(483840000000L);
        PassengerController.getInstance(AeroLogixClient.getInstance()).createPassenger(passenger1);
        
        PassengerData passenger2 = new PassengerData();
        passenger2.setDNI("79000000J");
        passenger2.setEmail("prueba2@mail.com");
        passenger2.setName("Passenger 2");
        passenger2.setNationality("USA");
        passenger2.setPhone(700000000);
        passenger2.setBirthdate(827452800000L);
        PassengerController.getInstance(AeroLogixClient.getInstance()).createPassenger(passenger2);

        // Flight
        FlightController.getInstance(AeroLogixClient.getInstance()).createFlight("Bilbao", "Madrid", 1714683000000L, 1);
        FlightController.getInstance(AeroLogixClient.getInstance()).createFlight("Madrid", "Bilbao", 1714743000000L, 1);
        FlightController.getInstance(AeroLogixClient.getInstance()).createFlight("Bilbao", "Madrid", 1714769400000L, 1);
        FlightController.getInstance(AeroLogixClient.getInstance()).createFlight("Madrid", "Bilbao", 1714829400000L, 1);

        // Booking
        ArrayList<UserData>users = UserController.getInstance().getAllUsers();
        ArrayList<AirlineData>airlines = AirlineController.getInstance(AeroLogixClient.getInstance()).getAllAirlines();
        BookingController.getInstance(AeroLogixClient.getInstance()).createBooking(passenger1.getDNI(), 1, users.get(0).getEmail(), airlines.get(0).getId());
        BookingController.getInstance(AeroLogixClient.getInstance()).createBooking(passenger2.getDNI(), 1, users.get(0).getEmail(), airlines.get(0).getId());

    }
    
    /**
     * The main method is the entry point of the application.
     * It expects two arguments: the hostname and port of the server.
     * <p>
     * It sets system properties for hostname and port, then displays the login window.
     * </p>
     *
     * @param args command line arguments, expects hostname and port
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            logger.info("Use: java Client.Client [host] [port]");
            System.exit(0);
        }

        String hostname = args[0];
        String port = args[1];

        // DO NOT REMOVE THESE TWO LINES
        System.setProperty("aerologix.hostname", hostname);
        System.setProperty("aerologix.port", port);

        // Login Window
        LoginWindow lw = LoginWindow.getInstanceLogin();
        lw.setVisible(true);
    }
}
