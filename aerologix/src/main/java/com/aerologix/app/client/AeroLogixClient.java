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

public class AeroLogixClient {
    
    protected static final Logger logger = LogManager.getLogger();

    private static AeroLogixClient instance;

    private Client client;
    private WebTarget webTarget;

    private AeroLogixClient() {
        this.client = ClientBuilder.newClient();
        this.webTarget = client.target(String.format("http://%s:%s/rest/aerologix", System.getProperty("aerologix.hostname"), System.getProperty("aerologix.port")));
    }

    public static AeroLogixClient getInstance() {
        if (instance == null) {
            instance = new AeroLogixClient();
        }
        return instance;
    }

    public WebTarget getWebTarget() {
        return this.webTarget;
    }

    // Method to demonstrate functionality for Sprint 2
    public void initializeData() {

        // User
        UserController.getInstance().registerUser("admin", "admin", "ADMIN", "AeroLogix Administrator");
        UserController.getInstance().registerUser("marta.sanchez@aena.es", "marta1234", "COUNTER_CLERK", "Marta Sanchez");
        UserController.getInstance().registerUser("pepeprueba@mail.com", "pepeprueba", "COUNTER_CLERK", "Pepe Prueba");

        ArrayList<UserData> users = UserController.getInstance().getAllUsers();

        UserController.getInstance().modifyUser(users.get(users.size()-1).getEmail(), "prueba", "COUNTER_CLERK", "Prueba");

        UserController.getInstance().deleteUser(users.get(0).getEmail());

        // Aircraft
        AircraftController.getInstance(AeroLogixClient.getInstance()).createAircraft("Boeing", "737", 200);
        AircraftController.getInstance(AeroLogixClient.getInstance()).createAircraft("Boeing", "747", 300);
        AircraftController.getInstance(AeroLogixClient.getInstance()).createAircraft("Airbus", "A380", 500);
        AircraftController.getInstance(AeroLogixClient.getInstance()).createAircraft("Airbus", "A330", 400);

        ArrayList<AircraftData> aircrafts = AircraftController.getInstance(AeroLogixClient.getInstance()).getAllAircrafts();

        AircraftController.getInstance(AeroLogixClient.getInstance()).modifyAircraft(aircrafts.get(0).getId(), "Embraer", "190", 100);

        AircraftController.getInstance(AeroLogixClient.getInstance()).deleteAircraft(aircrafts.get(aircrafts.size()-1).getId());

        // Airline
        AirlineController.getInstance(AeroLogixClient.getInstance()).createAirline("Iberia");
        AirlineController.getInstance(AeroLogixClient.getInstance()).createAirline("Vueling");
        AirlineController.getInstance(AeroLogixClient.getInstance()).createAirline("Volotea");
        AirlineController.getInstance(AeroLogixClient.getInstance()).createAirline("Ryanair");

        ArrayList<AirlineData> airlines = AirlineController.getInstance(AeroLogixClient.getInstance()).getAllAirlines();
        
        AirlineController.getInstance(AeroLogixClient.getInstance()).modifyAirline(airlines.get(2).getId(), "Air France");

        AirlineController.getInstance(AeroLogixClient.getInstance()).deleteAirline(airlines.get(airlines.size()-1).getId());

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

        ArrayList<PassengerData> passengers = PassengerController.getInstance(AeroLogixClient.getInstance()).getAllPassengers();

        passengers.get(passengers.size()-1).setName("Nombre modificado");
        PassengerController.getInstance(AeroLogixClient.getInstance()).modifyPassenger(passengers.get(passengers.size()-1));

        PassengerController.getInstance(AeroLogixClient.getInstance()).deletePassenger(passengers.get(0).getDNI());

        // Flight
        FlightController.getInstance(AeroLogixClient.getInstance()).createFlight("Bilbao", "Madrid", 1714683000000L, aircrafts.get(0).getId());
        FlightController.getInstance(AeroLogixClient.getInstance()).createFlight("Madrid", "Bilbao", 1714743000000L, aircrafts.get(0).getId());
        FlightController.getInstance(AeroLogixClient.getInstance()).createFlight("Bilbao", "Madrid", 1714769400000L, aircrafts.get(0).getId());
        FlightController.getInstance(AeroLogixClient.getInstance()).createFlight("Madrid", "Bilbao", 1714829400000L, aircrafts.get(0).getId());

        ArrayList<FlightData> flights = FlightController.getInstance(AeroLogixClient.getInstance()).getAllFlights();

        FlightController.getInstance(AeroLogixClient.getInstance()).modifyFlight(flights.get(0).getIdFlight(), "Bilbao", "Valencia", 0, aircrafts.get(1).getId(), new ArrayList<Integer>());
        
        FlightController.getInstance(AeroLogixClient.getInstance()).deleteFlight(flights.get(flights.size()-1).getIdFlight());

        // Booking
        passengers = PassengerController.getInstance(AeroLogixClient.getInstance()).getAllPassengers();
        users = UserController.getInstance().getAllUsers();
        airlines = AirlineController.getInstance(AeroLogixClient.getInstance()).getAllAirlines();
        BookingController.getInstance(AeroLogixClient.getInstance()).createBooking(passengers.get(0).getDNI(), flights.get(0).getIdFlight(), users.get(0).getEmail(), airlines.get(0).getId());
        BookingController.getInstance(AeroLogixClient.getInstance()).createBooking(passengers.get(0).getDNI(), flights.get(1).getIdFlight(), users.get(0).getEmail(), airlines.get(0).getId());

        ArrayList<BookingData> bookings = BookingController.getInstance(AeroLogixClient.getInstance()).getAllBookings();

        BookingController.getInstance(AeroLogixClient.getInstance()).modifyBooking(bookings.get(0).getId(), passengers.get(0).getDNI(), flights.get(0).getIdFlight(), users.get(0).getEmail(), airlines.get(airlines.size()-1).getId());
        
        BookingController.getInstance(AeroLogixClient.getInstance()).deleteBooking("2");

    }
    
    // Main
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

        // Sprint 2 demo
        AeroLogixClient.getInstance().initializeData();

        // Login Window
        LoginWindow lw = LoginWindow.getInstanceLogin();
        lw.setVisible(true);
    }
}
