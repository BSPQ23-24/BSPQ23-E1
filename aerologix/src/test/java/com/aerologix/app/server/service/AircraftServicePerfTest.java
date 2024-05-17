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

import com.aerologix.app.server.jdo.Aircraft;
import com.aerologix.app.server.pojo.AircraftData;
import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import jakarta.ws.rs.core.Response;
@Tag("PerformanceTest")
public class AircraftServicePerfTest {
	  private AircraftService aircraftService;
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

	        aircraftService = new AircraftService();
	        aircraftService.setPersistenceManagerFactory(pmfMock);
	        aircraftService.setPersistenceManager(pmMock); 
	        aircraftService.setTransaction(txMock);
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testGetAircraft() {
	        Aircraft aircraftMock = mock(Aircraft.class);
	        when(aircraftMock.getId()).thenReturn(1);
	        when(aircraftMock.getManufacturer()).thenReturn("Boeing");
	        when(pmMock.getObjectById(Aircraft.class, 1)).thenReturn(aircraftMock);

	        Response response = aircraftService.getAircraft(1);
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	        AircraftData aircraftData = (AircraftData) response.getEntity();
	        assertEquals(1, aircraftData.getId());
	        assertEquals("Boeing", aircraftData.getManufacturer());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testGetNonexistentAircraft() {
	        when(pmMock.getObjectById(Aircraft.class, 1)).thenThrow(new JDOObjectNotFoundException());

	        Response response = aircraftService.getAircraft(1);
	        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testGetAllAircrafts() {
	        Extent<Aircraft> extentMock = mock(Extent.class);
	        Iterator<Aircraft> iteratorMock = mock(Iterator.class);
	        Aircraft aircraftMock = mock(Aircraft.class);

	        when(pmMock.getExtent(Aircraft.class, true)).thenReturn(extentMock);
	        when(extentMock.iterator()).thenReturn(iteratorMock);
	        when(iteratorMock.hasNext()).thenReturn(true, false);
	        when(iteratorMock.next()).thenReturn(aircraftMock);

	        Response response = aircraftService.getAllAircrafts();
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testGetAllAircraftsEmpty() {
	        Extent<Aircraft> extentMock = mock(Extent.class);
	        Iterator<Aircraft> iteratorMock = mock(Iterator.class);

	        when(pmMock.getExtent(Aircraft.class, true)).thenReturn(extentMock);
	        when(extentMock.iterator()).thenReturn(iteratorMock);
	        when(iteratorMock.hasNext()).thenReturn(false);

	        Response response = aircraftService.getAllAircrafts();
	        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testCreateAircraft() {
	        AircraftData aircraftData = new AircraftData();
	        aircraftData.setManufacturer("Airbus");
	        aircraftData.setType("A320");
	        aircraftData.setMaxCapacity(180);

	        Response response = aircraftService.createAircraft(aircraftData);
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	        verify(pmMock, times(1)).makePersistent(any(Aircraft.class));
	        verify(txMock, times(1)).commit();
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testModifyAircraft() {
	        Aircraft aircraftMock = mock(Aircraft.class);
	        when(pmMock.getObjectById(Aircraft.class, 1)).thenReturn(aircraftMock);

	        AircraftData aircraftData = new AircraftData();
	        aircraftData.setId(1);
	        aircraftData.setManufacturer("Boeing");

	        Response response = aircraftService.modifyAircraft(aircraftData);
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	        verify(aircraftMock, times(1)).setManufacturer("Boeing");
	        verify(txMock, times(1)).commit();
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testModifyNonexistentAircraft() {
	        when(pmMock.getObjectById(Aircraft.class, 1)).thenThrow(new JDOObjectNotFoundException());

	        AircraftData aircraftData = new AircraftData();
	        aircraftData.setId(1);

	        Response response = aircraftService.modifyAircraft(aircraftData);
	        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testDeleteAircraft() {
	        Aircraft aircraftMock = mock(Aircraft.class);
	        when(pmMock.getObjectById(Aircraft.class, 1)).thenReturn(aircraftMock);

	        Response response = aircraftService.deleteAircraft(1);
	        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	        verify(pmMock, times(1)).deletePersistent(aircraftMock);
	        verify(txMock, times(1)).commit();
	    }

	    @Test
	    @JUnitPerfTest(threads = 10, durationMs = 2000)
	    public void testDeleteNonexistentAircraft() {
	        when(pmMock.getObjectById(Aircraft.class, 1)).thenThrow(new JDOObjectNotFoundException());

	        Response response = aircraftService.deleteAircraft(1);
	        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	    }
}
