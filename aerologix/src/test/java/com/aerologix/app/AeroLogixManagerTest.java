package com.aerologix.app;

import com.aerologix.app.server.AeroLogixManager;
import com.aerologix.app.server.jdo.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AeroLogixManagerTest {
    HashMap<Integer, Booking> bookings;
    HashMap<Integer, Flight> flights;
    HashMap<Integer, Airline> airlines;
    HashMap<String, Passenger> passengers;
    HashMap<Integer, Aircraft> aircrafts;
    @Mock
    HashMap<String, User> users;

    AeroLogixManager manager;

    //////USERS///////
    @Test
    void getUser() {
        users = Mockito.mock(HashMap.class);
        manager = new AeroLogixManager(bookings, flights, airlines, passengers, aircrafts, users);
        User mockUser = new User();
        mockUser.setName("Test");
        mockUser.setUserType(User.UserType.COUNTER_CLERK);
        mockUser.setPassword("Test");
        mockUser.setEmail("Test@Aerologix.com");

        Mockito.when(users.get("Test")).thenReturn(mockUser);

        User resultUser = manager.getUser("Test");
        assertEquals(resultUser, mockUser);
        assertEquals(resultUser.getName(), "Test");
        assertEquals(resultUser.getEmail(), "Test@Aerologix.com");
        assertEquals(resultUser.getPassword(), "Test");
        assertEquals(resultUser.getUserType(), User.UserType.COUNTER_CLERK);
        assertNotEquals(resultUser.getName(), "Test2");
    }

    @Test
    void addUser() {
        users = new HashMap<String, User>();
        manager = new AeroLogixManager(bookings, flights, airlines, passengers, aircrafts, users);

        User resultUser = new User();
        resultUser.setName("Test");
        resultUser.setUserType(User.UserType.ADMIN);
        resultUser.setPassword("Test");
        resultUser.setEmail("Test@Aerologix.com");

        manager.addUser(resultUser);

        assertEquals(manager.getUser("Test@Aerologix.com"), resultUser);
        assertEquals(manager.getUser("Test@Aerologix.com").getEmail(), resultUser.getEmail());
        assertEquals(manager.getUser("Test@Aerologix.com").getName(), resultUser.getName());
        assertEquals(manager.getUser("Test@Aerologix.com").getPassword(), resultUser.getPassword());
        assertEquals(manager.getUser("Test@Aerologix.com").getUserType(), resultUser.getUserType());
        assertNotEquals(manager.getUser("Test@Aerologix.com").getName(), "Test2");
    }

    @Test
    void modifyUser() {
        User resultUser = new User();
        resultUser.setName("Test");
        resultUser.setUserType(User.UserType.ADMIN);
        resultUser.setPassword("Test");
        resultUser.setEmail("Test@Aerologix.com");
        users = new HashMap<String, User>();
        users.put(resultUser.getEmail(), resultUser);
        manager = new AeroLogixManager(bookings, flights, airlines, passengers, aircrafts, users);

        manager.modifyUser(resultUser.getEmail(), "Test2", User.UserType.COUNTER_CLERK, "Test2");

        assertEquals(manager.getUser("Test@Aerologix.com").getName(), "Test2");
        assertEquals(manager.getUser("Test@Aerologix.com").getPassword(), "Test2");
        assertEquals(manager.getUser("Test@Aerologix.com").getEmail(), "Test@Aerologix.com");
        assertEquals(manager.getUser("Test@Aerologix.com").getUserType(), User.UserType.COUNTER_CLERK);
        assertNotEquals(manager.getUser("Test@Aerologix.com").getName(), "Test");
        assertNotEquals(manager.getUser("Test@Aerologix.com").getEmail(), "Test2@Aerologix.com");
    }

    @Test
    void deleteUser() {
        User resultUser = new User();
        resultUser.setName("Test");
        resultUser.setUserType(User.UserType.ADMIN);
        resultUser.setPassword("Test");
        resultUser.setEmail("Test@Aerologix.com");
        users = new HashMap<String, User>();
        users.put(resultUser.getEmail(), resultUser);
        manager = new AeroLogixManager(bookings, flights, airlines, passengers, aircrafts, users);

        manager.deleteUser(resultUser.getEmail());

        assertNull(manager.getUser("Test@Aerologix.com"));
        assertNull(manager.getUser(resultUser.getEmail()));
    }
    
//////Airlines///////
  @Test
  void getAirline() {
      airlines = Mockito.mock(HashMap.class);
      manager = new AeroLogixManager(bookings, flights, airlines, passengers, aircrafts, users);
      Airline mockAirline = new Airline();
      mockAirline.setId(0);
      mockAirline.setName("Test");

      Mockito.when(airlines.get(0)).thenReturn(mockAirline);

      Airline resultAirline = manager.getAirline(0);
      assertEquals(resultAirline, mockAirline);
      assertEquals(resultAirline.getId(), 0);
      assertEquals(resultAirline.getName(), "Test");
  }

  @Test
  void addAirline() {
      airlines = new HashMap<Integer, Airline>();
      manager = new AeroLogixManager(bookings, flights, airlines, passengers, aircrafts, users);

      Airline resultAirline = new Airline();
      resultAirline.setId(0);
      resultAirline.setName("Test");

      manager.addAirline(resultAirline);

      assertEquals(manager.getAirline(0), resultAirline);
      assertEquals(manager.getAirline(0).getId(), resultAirline.getId());
  }
  
  @Test
  void modifyAirline() {
      Airline resultAirline = new Airline();
      resultAirline.setId(0);
      resultAirline.setName("Test");

      airlines = new HashMap<Integer, Airline>();
      airlines.put(resultAirline.getId(), resultAirline);
      manager = new AeroLogixManager(bookings, flights, airlines, passengers, aircrafts, users);

      manager.modifyAirline(resultAirline.getId(), "Test");

      assertEquals(manager.getAirline(0).getId(), 0);
      assertEquals(manager.getAirline(0).getName(), "Test");
  }

  @Test
  void deleteAirline() {
      Airline resultAirline = new Airline();
      resultAirline.setId(0);
      resultAirline.setName("Test");
      airlines = new HashMap<Integer, Airline>();
      airlines.put(resultAirline.getId(), resultAirline);
      manager = new AeroLogixManager(bookings, flights, airlines, passengers, aircrafts, users);

      manager.deleteAirline(resultAirline.getId());

      assertNull(manager.getAirline(0));
      assertNull(manager.getAirline(resultAirline.getId()));
  }
}