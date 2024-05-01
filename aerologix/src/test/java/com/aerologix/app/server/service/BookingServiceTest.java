package com.aerologix.app.server.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aerologix.app.server.pojo.BookingData;
import com.aerologix.app.server.pojo.PassengerData;
import com.aerologix.app.server.service.BookingService;
import com.aerologix.app.server.jdo.Booking;
import com.aerologix.app.server.jdo.Passenger;

import javax.jdo.*;

import jakarta.ws.rs.core.Response;

import java.util.Iterator;

public class BookingServiceTest {
	
	private BookingService bookingService;
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

        bookingService = new BookingService();
        bookingService.setPersistenceManagerFactory(pmfMock);
        bookingService.setPersistenceManager(pmMock); 
        bookingService.setTransaction(txMock);
    }
	
    
    @Test
    public void testCreateBooking() {
        BookingData bookingData = new BookingData();
        bookingData.setId(2);
        bookingData.setPassengerDNI("79050089D");
        bookingData.setFlightId(1);
        bookingData.setUserEmail("anagonzalez02@opendeusto.es");
        bookingData.setAirlineId(3);

        when(pmMock.getObjectById(Booking.class, 2)).thenThrow(new JDOObjectNotFoundException());
        Response response = bookingService.createBooking(bookingData);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(pmMock, times(1)).makePersistent(any(Booking.class));
        verify(txMock, times(1)).commit();
    }
    
    @Test
    public void testGetBooking() {
    	Booking bookingMock = mock(Booking.class);
        when(bookingMock.getId()).thenReturn(2);
        when(bookingMock.getPassenger().getDNI()).thenReturn("79050089D");
        when(pmMock.getObjectById(Booking.class, 2)).thenReturn(bookingMock);

        Response response = bookingService.getBooking("2");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        BookingData bookingData = (BookingData) response.getEntity();
        assertEquals(2, BookingData.getId());
        assertEquals("79050089D", bookingData.getPassengerDNI());
    }
    
    @Test
    public void testGetAllBooking() {
    	Extent<Booking> extentMock = mock(Extent.class);
        Iterator<Booking> iteratorMock = mock(Iterator.class);
        Booking bookingMock = mock(Booking.class);

        when(pmMock.getExtent(Booking.class, true)).thenReturn(extentMock);
        when(extentMock.iterator()).thenReturn(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(true, false);
        when(iteratorMock.next()).thenReturn(bookingMock);

        Response response = bookingService.getAllBooking();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testModifyBooking() {
        Booking bookingMock = mock(Booking.class);
        when(pmMock.getObjectById(Booking.class, 2)).thenReturn(bookingMock);

        BookingData bookingData = new BookingData();
        bookingData.setId(2);
        bookingData.setPassengerDNI("79050089D");

        Response response = bookingService.modifyBooking(bookingData);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(bookingMock, times(1)).setId(2);
        verify(txMock, times(1)).commit();
    }
    
    @Test
    public void testDeleteBooking() {
        Booking bookingMock = mock(Booking.class);
        when(pmMock.getObjectById(Booking.class, 2)).thenReturn(bookingMock);

        Response response = bookingService.deleteBooking(2);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(pmMock, times(1)).deletePersistent(bookingMock);
        verify(txMock, times(1)).commit();
    }
	
}
