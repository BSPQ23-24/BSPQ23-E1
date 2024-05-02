package com.aerologix.app.server.service;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import com.aerologix.app.server.jdo.Aircraft;
import com.aerologix.app.server.jdo.Booking;
import com.aerologix.app.server.jdo.Flight;

import com.aerologix.app.server.pojo.AircraftData;
import com.aerologix.app.server.pojo.BookingData;
import com.aerologix.app.server.pojo.FlightData;


import jakarta.ws.rs.core.Response;

public class FlightServiceTest {
	private FlightService flightService;
    private PersistenceManagerFactory pmfMock;
    private PersistenceManager pmMock;
    private Transaction txMock;
    
    @BeforeEach
    public void setUp() {
        pmfMock = mock(PersistenceManagerFactory.class);
        pmMock = mock(PersistenceManager.class);
        txMock = mock(Transaction.class);

        when(pmfMock.getPersistenceManager()).thenReturn(pmMock);
        when(pmMock.currentTransaction()).thenReturn(txMock);

        flightService = new FlightService();
        flightService.setPersistenceManagerFactory(pmfMock);
        flightService.setPersistenceManager(pmMock); 
        flightService.setTransaction(txMock);
    }
    
    @Test
    public void testCreateFlight() {
    	
    	Aircraft aircraftMock = mock(Aircraft.class);
        FlightData flightData = new FlightData();
        flightData.setIdFlight(12);
        flightData.setOrigin("Bilbao");
        flightData.setDestination("Madrid");
        flightData.setDate(1);
        flightData.setBookingIds(new ArrayList<Integer>());
        flightData.setAircraftId(13);
        

        when(pmMock.getObjectById(Flight.class, 12)).thenThrow(new JDOObjectNotFoundException());
        when(pmMock.getObjectById(Aircraft.class, 13)).thenReturn(aircraftMock);
        Response response = flightService.createFlight(flightData);
        
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(pmMock, times(1)).makePersistent(any(Flight.class));
        verify(txMock, times(1)).commit();
    }
    
    @Test
    public void testCreateExistingFlight() {
        Flight flightMock = mock(Flight.class);
        when(pmMock.getObjectById(Flight.class, 1)).thenReturn(flightMock);

        FlightData flightData = new FlightData();
        flightData.setIdFlight(1);

        Response response = flightService.createFlight(flightData);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testModifyFlight() {
        Flight flightMock = mock(Flight.class);
        when(pmMock.getObjectById(Flight.class, 1)).thenReturn(flightMock);
        Aircraft aircraftMock = mock(Aircraft.class);
        when(pmMock.getObjectById(Aircraft.class, 2)).thenReturn(aircraftMock);
        FlightData flightData = new FlightData();
        flightData.setIdFlight(1);
        flightData.setOrigin("Bilbao");
        flightData.setDestination("Madrid");
        flightData.setAircraftId(2);

        Response response = flightService.modifyFlight(flightData);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(flightMock, times(1)).setOrigin("Bilbao");
        verify(txMock, times(1)).commit();
    }
    
    
    @Test
    public void testModifyNonexistentFlight() {
        when(pmMock.getObjectById(Flight.class, 12)).thenThrow(new JDOObjectNotFoundException());

        FlightData flightData = new FlightData();
        flightData.setIdFlight(12);;

        Response response = flightService.modifyFlight(flightData);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testDeleteFlight() {
        Flight flightMock = mock(Flight.class);
        when(pmMock.getObjectById(Flight.class, 1)).thenReturn(flightMock);

        Response response = flightService.deleteFlight(1);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(pmMock, times(1)).deletePersistent(flightMock);
        verify(txMock, times(1)).commit();
    }
    
    @Test
    public void testGetFlight() {
        Flight flightMock = mock(Flight.class);
        Aircraft aircraftMock = mock(Aircraft.class);
        when(flightMock.getIdFlight()).thenReturn(1);
        when(flightMock.getOrigin()).thenReturn("Bilbao");
        when(flightMock.getDestination()).thenReturn("Madrid");
        when(flightMock.getDate()).thenReturn(1l);
        when(flightMock.getBookings()).thenReturn(new ArrayList<Booking>());
        when(pmMock.getObjectById(Aircraft.class, 2)).thenReturn(aircraftMock);
        when(pmMock.getObjectById(Flight.class, 1)).thenReturn(flightMock);

        Response response = flightService.getFlight(1);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        FlightData flightData = (FlightData) response.getEntity();
        assertEquals(1, flightData.getAircraftId());
        assertEquals("Bilbao", flightData.getOrigin());
        assertEquals("Madrid", flightData.getDestination());
        assertEquals(1, flightData.getDate());

    }
    @Test
    public void testGetNonexistentFlight() {
        when(pmMock.getObjectById(Flight.class, 1)).thenThrow(new JDOObjectNotFoundException());

        Response response = flightService.getFlight(1);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testGetAllFlights() {
        Extent<Flight> extentMock = mock(Extent.class);
        Iterator<Flight> iteratorMock = mock(Iterator.class);
        Flight flightMock = mock(Flight.class);

        when(pmMock.getExtent(Flight.class, true)).thenReturn(extentMock);
        when(extentMock.iterator()).thenReturn(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(true, false);
        when(iteratorMock.next()).thenReturn(flightMock);

        Response response = flightService.getAllFlights();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
    @Test
    public void testGetAllFlightsEmpty() {
        Extent<Flight> extentMock = mock(Extent.class);
        Iterator<Flight> iteratorMock = mock(Iterator.class);

        when(pmMock.getExtent(Flight.class, true)).thenReturn(extentMock);
        when(extentMock.iterator()).thenReturn(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(false);

        Response response = flightService.getAllFlights();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
}
