package com.aerologix.app.server.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.aerologix.app.server.pojo.BookingData;
import com.aerologix.app.server.pojo.PassengerData;
import com.aerologix.app.server.service.BookingService;
import com.aerologix.app.server.jdo.Booking;
import com.aerologix.app.server.jdo.Passenger;
import com.aerologix.app.server.jdo.Airline;
import com.aerologix.app.server.jdo.User;
import com.aerologix.app.server.jdo.Flight;

import javax.jdo.*;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Response;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BookingServiceTest {
	
	private BookingService bookingService;
    private PersistenceManagerFactory pmfMock;
    private PersistenceManager pmMock;
    private Transaction txMock;
    @Mock
    private Passenger passenger;
    @Mock
    private Flight flight;
    @Mock
    private User user;
    @Mock
    private Airline airline;

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
        Flight flightMock = mock(Flight.class);
        bookingData.setId(2);
        bookingData.setPassengerDNI("79050089D");
        bookingData.setFlightId(1);
        bookingData.setUserEmail("anagonzalez02@opendeusto.es");
        bookingData.setAirlineId(3);

        when(pmMock.getObjectById(Booking.class, 2)).thenThrow(new JDOObjectNotFoundException());
        when(pmMock.getObjectById(Flight.class, 1)).thenReturn(flightMock);
        Response response = bookingService.createBooking(bookingData);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(pmMock, times(1)).makePersistent(any(Booking.class));
        verify(txMock, times(1)).commit();
    }
    
   
    @Test
    public void testGetBooking() {
        Booking bookingMock = mock(Booking.class);
        Flight flightMock = mock(Flight.class);
        User userMock = mock(User.class);
        Airline airlineMock = mock(Airline.class);
        Passenger passengerMock = mock(Passenger.class);
        Set<Booking> bookingsMock = new HashSet<>();
        bookingsMock.add(mock(Booking.class));
        bookingsMock.add(mock(Booking.class));

        when(bookingMock.getId()).thenReturn(2);
        when(bookingMock.getFlight()).thenReturn(flightMock);
        when(flightMock.getBookings()).thenReturn(bookingsMock);
        when(bookingMock.getPassenger()).thenReturn(passengerMock);
        when(bookingMock.getUser()).thenReturn(userMock);
        when(bookingMock.getAirline()).thenReturn(airlineMock);
        when(passengerMock.getDNI()).thenReturn("79050089D");
        when(pmMock.getObjectById(Booking.class, 2)).thenReturn(bookingMock);

        Response response = bookingService.getBooking(2);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        BookingData bookingData = (BookingData) response.getEntity();
        assertEquals(2, bookingData.getId());
        assertEquals("79050089D", bookingData.getPassengerDNI());
    }
     @Test
    public void testGetAllBookings() {
    	Extent<Booking> extentMock = mock(Extent.class);
        Iterator<Booking> iteratorMock = mock(Iterator.class);
        Booking bookingMock = mock(Booking.class);
        Flight flightMock = mock(Flight.class);
        User userMock = mock(User.class);
        Airline airlineMock = mock(Airline.class);
        Passenger passengerMock = mock(Passenger.class);
        Set<Booking> bookingsMock = new HashSet<>();
        bookingsMock.add(mock(Booking.class));
        bookingsMock.add(mock(Booking.class));

        when(pmMock.getExtent(Booking.class, true)).thenReturn(extentMock);
        when(bookingMock.getFlight()).thenReturn(flightMock);
        when(flightMock.getBookings()).thenReturn(bookingsMock);
        when(bookingMock.getPassenger()).thenReturn(passengerMock);
        when(bookingMock.getUser()).thenReturn(userMock);
        when(bookingMock.getAirline()).thenReturn(airlineMock);
        when(extentMock.iterator()).thenReturn(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(true, false);
        when(iteratorMock.next()).thenReturn(bookingMock);
        

        Response response = bookingService.getAllBookings();
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
