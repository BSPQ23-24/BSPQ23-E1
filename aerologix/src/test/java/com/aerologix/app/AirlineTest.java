package com.aerologix.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.aerologix.app.server.jdo.Airline;

public class AirlineTest {
    
    Airline airline;
	
    @BeforeEach
    public void setUp() {
        airline = new Airline();
        airline.setId(1);
        airline.setName("Prueba001");
    }

    @Test
    public void testCreateAirline() {
        assertEquals(1, airline.getId());
        assertEquals("Prueba001", airline.getName());
    }

    @Test
    public void testSetAndGetId() {
        airline.setId(2);
        assertEquals(2, airline.getId());
    }

    @Test
    public void testSetAndGetName() {
        airline.setName("Prueba002");
        assertEquals("Prueba002", airline.getName());
    }

    @Test
    public void testToString() {
        assertEquals("AirlinePrueba001id:1", airline.toString());
    }   
}
