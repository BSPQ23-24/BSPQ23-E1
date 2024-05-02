package com.aerologix.app.server.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aerologix.app.server.pojo.AircraftData;
import com.aerologix.app.server.jdo.Aircraft;

import javax.jdo.*;

import jakarta.ws.rs.core.Response;

import java.util.Iterator;


public class AircraftServiceTest {

    private AircraftService aircraftService;
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

        aircraftService = new AircraftService();
        aircraftService.setPersistenceManagerFactory(pmfMock);
        aircraftService.setPersistenceManager(pmMock); 
        aircraftService.setTransaction(txMock);
    }

    @Test
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
    public void testGetNonexistentAircraft() {
        when(pmMock.getObjectById(Aircraft.class, 1)).thenThrow(new JDOObjectNotFoundException());

        Response response = aircraftService.getAircraft(1);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
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
    public void testModifyNonexistentAircraft() {
        when(pmMock.getObjectById(Aircraft.class, 1)).thenThrow(new JDOObjectNotFoundException());

        AircraftData aircraftData = new AircraftData();
        aircraftData.setId(1);

        Response response = aircraftService.modifyAircraft(aircraftData);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDeleteAircraft() {
        Aircraft aircraftMock = mock(Aircraft.class);
        when(pmMock.getObjectById(Aircraft.class, 1)).thenReturn(aircraftMock);

        Response response = aircraftService.deleteAircraft(1);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(pmMock, times(1)).deletePersistent(aircraftMock);
        verify(txMock, times(1)).commit();
    }

    @Test
    public void testDeleteNonexistentAircraft() {
        when(pmMock.getObjectById(Aircraft.class, 1)).thenThrow(new JDOObjectNotFoundException());

        Response response = aircraftService.deleteAircraft(1);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }
}
