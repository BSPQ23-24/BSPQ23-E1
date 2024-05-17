package com.aerologix.app.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.aerologix.app.server.jdo.Airline;
import com.aerologix.app.server.pojo.AirlineData;
import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import jakarta.ws.rs.core.Response;
@Tag("PerformanceTest")
public class AirlineServicePerfTest {
	 private AirlineService airlineService;
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

	        airlineService = new AirlineService();
	        airlineService.setPersistenceManagerFactory(pmfMock);
	        airlineService.setPersistenceManager(pmMock); 
	        airlineService.setTransaction(txMock);
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testCreateAirline() {
	        AirlineData airlineData = new AirlineData();
	        airlineData.setId(0);
	        airlineData.setName("Test");

	        when(pmMock.getObjectById(Airline.class, 0)).thenThrow(new JDOObjectNotFoundException());
	        Response response = airlineService.createAirline(airlineData);

	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	        verify(pmMock, times(1)).makePersistent(any(Airline.class));
	        verify(txMock, times(1)).commit();
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testCreateExistingAirline() {
	        Airline airlineMock = mock(Airline.class);
	        when(pmMock.getObjectById(Airline.class, 0)).thenReturn(airlineMock);

	        AirlineData airlineData = new AirlineData();
	        airlineData.setId(0);

	        Response response = airlineService.createAirline(airlineData);
	        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testModifyAirline() {
	        Airline airlineMock = mock(Airline.class);
	        when(pmMock.getObjectById(Airline.class, 0)).thenReturn(airlineMock);

	        AirlineData airlineData = new AirlineData();
	        airlineData.setId(0);
	        airlineData.setName("Test1");

	        Response response = airlineService.modifyAirline(airlineData);
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	        verify(airlineMock, times(1)).setName("Test1");
	        verify(txMock, times(1)).commit();
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testModifyNonexistentAirline() {
	        when(pmMock.getObjectById(Airline.class, 0)).thenThrow(new JDOObjectNotFoundException());

	        AirlineData airlineData = new AirlineData();
	        airlineData.setId(0);

	        Response response = airlineService.modifyAirline(airlineData);
	        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testDeleteAirline() {
	        Airline airlineMock = mock(Airline.class);
	        when(pmMock.getObjectById(Airline.class, 0)).thenReturn(airlineMock);

	        Response response = airlineService.deleteAirline(0);
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	        verify(pmMock, times(1)).deletePersistent(airlineMock);
	        verify(txMock, times(1)).commit();
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testDeleteNonexistentAirline() {
	        when(pmMock.getObjectById(Airline.class, 100)).thenThrow(new JDOObjectNotFoundException());

	        Response response = airlineService.deleteAirline(0);
	        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testGetAirline() {
	        Airline airlineMock = mock(Airline.class);
	        when(airlineMock.getId()).thenReturn(0);
	        when(airlineMock.getName()).thenReturn("Test");
	        when(pmMock.getObjectById(Airline.class, 0)).thenReturn(airlineMock);

	        Response response = airlineService.getAirline(0);
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	        AirlineData airlineData = (AirlineData) response.getEntity();
	        assertEquals(0, airlineData.getId());
	        assertEquals("Test", airlineData.getName());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testGetNonexistentAirline() {
	        when(pmMock.getObjectById(Airline.class, 0)).thenThrow(new JDOObjectNotFoundException());

	        Response response = airlineService.getAirline(0);
	        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testGetAllAirlines() {
	        Extent<Airline> extentMock = mock(Extent.class);
	        Iterator<Airline> iteratorMock = mock(Iterator.class);
	        Airline airlineMock = mock(Airline.class);

	        when(pmMock.getExtent(Airline.class, true)).thenReturn(extentMock);
	        when(extentMock.iterator()).thenReturn(iteratorMock);
	        when(iteratorMock.hasNext()).thenReturn(true, false);
	        when(iteratorMock.next()).thenReturn(airlineMock);

	        Response response = airlineService.getAllAirlines();
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testGetAllAirlinesEmpty() {
	        Extent<Airline> extentMock = mock(Extent.class);
	        Iterator<Airline> iteratorMock = mock(Iterator.class);

	        when(pmMock.getExtent(Airline.class, true)).thenReturn(extentMock);
	        when(extentMock.iterator()).thenReturn(iteratorMock);
	        when(iteratorMock.hasNext()).thenReturn(false);

	        Response response = airlineService.getAllAirlines();
	        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
	    }
}
