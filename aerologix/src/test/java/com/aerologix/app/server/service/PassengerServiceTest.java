package com.aerologix.app.server.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aerologix.app.server.pojo.PassengerData;
import com.aerologix.app.server.service.PassengerService;
import com.aerologix.app.server.jdo.Passenger;

import javax.jdo.*;

import jakarta.ws.rs.core.Response;

import java.util.Iterator;


public class PassengerServiceTest {

    private PassengerService passengerService;
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

        passengerService = new PassengerService();
        passengerService.setPersistenceManagerFactory(pmfMock);
        passengerService.setPersistenceManager(pmMock); 
        passengerService.setTransaction(txMock);
    }

    @Test
    public void testCreatePassenger() {
        PassengerData passengerData = new PassengerData();
        passengerData.setDNI("12345678A");
        passengerData.setPhone(123456789);
        passengerData.setName("Ortega");
        passengerData.setEmail("oretega@example.com");
        passengerData.setNationality("ES");
        passengerData.setBirthdate(946684800000L);

        when(pmMock.getObjectById(Passenger.class, "12345678A")).thenThrow(new JDOObjectNotFoundException());
        Response response = passengerService.createPassenger(passengerData);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(pmMock, times(1)).makePersistent(any(Passenger.class));
        verify(txMock, times(1)).commit();
    }

    @Test
    public void testCreateExistingPassenger() {
        Passenger passengerMock = mock(Passenger.class);
        when(pmMock.getObjectById(Passenger.class, "12345678A")).thenReturn(passengerMock);

        PassengerData passengerData = new PassengerData();
        passengerData.setDNI("12345678A");

        Response response = passengerService.createPassenger(passengerData);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testModifyPassenger() {
        Passenger passengerMock = mock(Passenger.class);
        when(pmMock.getObjectById(Passenger.class, "12345678A")).thenReturn(passengerMock);

        PassengerData passengerData = new PassengerData();
        passengerData.setDNI("12345678A");
        passengerData.setName("Gaset");

        Response response = passengerService.modifyPassenger(passengerData);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(passengerMock, times(1)).setName("Gaset");
        verify(txMock, times(1)).commit();
    }

    @Test
    public void testModifyNonexistentPassenger() {
        when(pmMock.getObjectById(Passenger.class, "12345678A")).thenThrow(new JDOObjectNotFoundException());

        PassengerData passengerData = new PassengerData();
        passengerData.setDNI("12345678A");

        Response response = passengerService.modifyPassenger(passengerData);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDeletePassenger() {
        Passenger passengerMock = mock(Passenger.class);
        when(pmMock.getObjectById(Passenger.class, "12345678A")).thenReturn(passengerMock);

        Response response = passengerService.deletePassenger("12345678A");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(pmMock, times(1)).deletePersistent(passengerMock);
        verify(txMock, times(1)).commit();
    }

    @Test
    public void testDeleteNonexistentPassenger() {
        when(pmMock.getObjectById(Passenger.class, "12345678A")).thenThrow(new JDOObjectNotFoundException());

        Response response = passengerService.deletePassenger("12345678A");
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetPassenger() {
        Passenger passengerMock = mock(Passenger.class);
        when(passengerMock.getDNI()).thenReturn("12345678A");
        when(passengerMock.getName()).thenReturn("Ortega");
        when(pmMock.getObjectById(Passenger.class, "12345678A")).thenReturn(passengerMock);

        Response response = passengerService.getPassenger("12345678A");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        PassengerData passengerData = (PassengerData) response.getEntity();
        assertEquals("12345678A", passengerData.getDNI());
        assertEquals("Ortega", passengerData.getName());
    }

    @Test
    public void testGetNonexistentPassenger() {
        when(pmMock.getObjectById(Passenger.class, "12345678A")).thenThrow(new JDOObjectNotFoundException());

        Response response = passengerService.getPassenger("12345678A");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetAllPassengers() {
        Extent<Passenger> extentMock = mock(Extent.class);
        Iterator<Passenger> iteratorMock = mock(Iterator.class);
        Passenger passengerMock = mock(Passenger.class);

        when(pmMock.getExtent(Passenger.class, true)).thenReturn(extentMock);
        when(extentMock.iterator()).thenReturn(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(true, false);
        when(iteratorMock.next()).thenReturn(passengerMock);

        Response response = passengerService.getAllPassengers();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetAllPassengersEmpty() {
        Extent<Passenger> extentMock = mock(Extent.class);
        Iterator<Passenger> iteratorMock = mock(Iterator.class);

        when(pmMock.getExtent(Passenger.class, true)).thenReturn(extentMock);
        when(extentMock.iterator()).thenReturn(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(false);

        Response response = passengerService.getAllPassengers();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
}
