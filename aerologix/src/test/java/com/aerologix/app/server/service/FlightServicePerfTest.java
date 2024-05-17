package com.aerologix.app.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import javax.jdo.Extent;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.aerologix.app.server.jdo.Aircraft;
import com.aerologix.app.server.jdo.Booking;
import com.aerologix.app.server.jdo.Flight;
import com.aerologix.app.server.pojo.FlightData;
import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import jakarta.ws.rs.core.Response;


@Tag("PerformanceTest")
public class FlightServicePerfTest {
	 private FlightService flightService;
	    private PersistenceManagerFactory pmfMock;
	    private PersistenceManager pmMock;
	    private Transaction txMock;

	    @Rule
	    public JUnitPerfRule perfTestRule = new JUnitPerfRule(new HtmlReportGenerator("/target/junitperf/report.html"));
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
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testGetFlight() {
	        Flight flightMock = mock(Flight.class);
	        when(flightMock.getIdFlight()).thenReturn(1);
	        when(flightMock.getOrigin()).thenReturn("Origin");
	        when(flightMock.getDestination()).thenReturn("Destination");
	        when(flightMock.getDate()).thenReturn(1L);

	        Aircraft aircraftMock = mock(Aircraft.class);
	        when(aircraftMock.getId()).thenReturn(123);

	        Set<Booking> bookingsMock = new HashSet<>();
	        bookingsMock.add(mock(Booking.class));
	        bookingsMock.add(mock(Booking.class));

	        when(flightMock.getAircraft()).thenReturn(aircraftMock);
	        when(flightMock.getBookings()).thenReturn((Set<Booking>) bookingsMock);

	        when(pmMock.getObjectById(Flight.class, 1)).thenReturn(flightMock);

	        Response response = flightService.getFlight(1);
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	        FlightData flightData = (FlightData) response.getEntity();
	        assertEquals(1, flightData.getIdFlight());
	        assertEquals("Origin", flightData.getOrigin());
	        assertEquals("Destination", flightData.getDestination());
	        assertEquals(1L, flightData.getDate());
	        assertEquals(123, flightData.getAircraftId());
	        assertEquals(2, flightData.getBookingIds().size());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testGetNonexistentFlight() {
	        when(pmMock.getObjectById(Flight.class, 1)).thenThrow(new JDOObjectNotFoundException());

	        Response response = flightService.getFlight(1);
	        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testGetAllFlights() {
	        Extent<Flight> extentMock = mock(Extent.class);
	        Iterator<Flight> iteratorMock = mock(Iterator.class);
	        Flight flightMock = mock(Flight.class);
	        Aircraft aircraftMock = mock(Aircraft.class);

	        when(pmMock.getExtent(Flight.class, true)).thenReturn(extentMock);
	        when(flightMock.getAircraft()).thenReturn(aircraftMock);
	        when(extentMock.iterator()).thenReturn(iteratorMock);
	        when(iteratorMock.hasNext()).thenReturn(true, false);
	        when(iteratorMock.next()).thenReturn(flightMock);

	        Response response = flightService.getAllFlights();
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testGetAllFlightsEmpty() {
	        Extent<Flight> extentMock = mock(Extent.class);
	        Iterator<Flight> iteratorMock = mock(Iterator.class);

	        when(pmMock.getExtent(Flight.class, true)).thenReturn(extentMock);
	        when(extentMock.iterator()).thenReturn(iteratorMock);
	        when(iteratorMock.hasNext()).thenReturn(false);

	        Response response = flightService.getAllFlights();
	        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testCreateFlight() {
	        FlightData flightData = new FlightData();
	        flightData.setIdFlight(1);
	        flightData.setOrigin("Origin");
	        flightData.setDestination("Destination");
	        flightData.setDate(1);
	        flightData.setAircraftId(123);

	        List<Integer> bookingIds = new ArrayList<>();
	        bookingIds.add(1);
	        bookingIds.add(2);
	        flightData.setBookingIds(bookingIds);

	        Response response = flightService.createFlight(flightData);
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	        verify(pmMock, times(1)).makePersistent(any(Flight.class));
	        verify(txMock, times(1)).commit();
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testModifyFlight() {
	        Flight flightMock = mock(Flight.class);
	        when(pmMock.getObjectById(Flight.class, 1)).thenReturn(flightMock);

	        FlightData flightData = new FlightData();
	        flightData.setIdFlight(1);
	        flightData.setOrigin("Origin");
	        flightData.setDestination("Destination");
	        flightData.setDate(1);
	        flightData.setAircraftId(123);

	        List<Integer> bookingIds = new ArrayList<>();
	        bookingIds.add(1);
	        bookingIds.add(2);
	        flightData.setBookingIds(bookingIds);

	        Response response = flightService.modifyFlight(flightData);
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	        verify(flightMock, times(1)).setOrigin("Origin");
	        verify(txMock, times(1)).commit();
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testModifyNonexistentFlight() {
	        when(pmMock.getObjectById(Flight.class, 1)).thenThrow(new JDOObjectNotFoundException());

	        FlightData flightData = new FlightData();
	        flightData.setIdFlight(1);

	        Response response = flightService.modifyFlight(flightData);
	        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testDeleteFlight() {
	        Flight flightMock = mock(Flight.class);
	        when(pmMock.getObjectById(Flight.class, 1)).thenReturn(flightMock);

	        Response response = flightService.deleteFlight(1);
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	        verify(pmMock, times(1)).deletePersistent(flightMock);
	        verify(txMock, times(1)).commit();
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testDeleteNonexistentFlight() {
	        when(pmMock.getObjectById(Flight.class, 1)).thenThrow(new JDOObjectNotFoundException());

	        Response response = flightService.deleteFlight(1);
	        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	    }
}
