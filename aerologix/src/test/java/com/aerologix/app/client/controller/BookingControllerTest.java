package com.aerologix.app.client.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.BookingData;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class BookingControllerTest {

    private BookingController bookingController;
    private WebTarget webTargetMock;
    private Invocation.Builder invocationBuilderMock;
    private AeroLogixClient clientMock;

    @BeforeEach
    public void setUp() {
        webTargetMock = mock(WebTarget.class);
        invocationBuilderMock = mock(Invocation.Builder.class);

        clientMock = mock(AeroLogixClient.class);
        when(clientMock.getWebTarget()).thenReturn(webTargetMock);

        when(webTargetMock.path(any())).thenReturn(webTargetMock);
        when(webTargetMock.queryParam(any(), any())).thenReturn(webTargetMock);
        when(webTargetMock.request(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilderMock);

        bookingController = BookingController.getInstance(clientMock);

        Response responseMock = mock(Response.class);
        when(responseMock.getStatus()).thenReturn(Status.OK.getStatusCode());
        when(responseMock.readEntity(BookingData.class)).thenReturn(new BookingData());
        when(responseMock.readEntity(new GenericType<ArrayList<BookingData>>() {})).thenReturn(new ArrayList<>());
        when(invocationBuilderMock.get()).thenReturn(responseMock);
        when(invocationBuilderMock.post(any(Entity.class))).thenReturn(responseMock);
    }

    @Test
    public void testCreateBooking() {
        int result = bookingController.createBooking("1234567890", 1, "test@example.com", 1);
        assertEquals(0, result);
    }

    @Test
    public void testDeleteBooking() {
        int result = bookingController.deleteBooking("abc123");
        assertEquals(0, result);
    }

    @Test
    public void testModifyBooking() {
        int result = bookingController.modifyBooking(1, "0987654321", 2, "test@example.com", 2);
        assertEquals(0, result);
    }

    @Test
    public void testGetBooking() {
        BookingData booking = bookingController.getBooking(1);
        assertNotNull(booking);
    }

    @Test
    public void testGetAllBookings() {
        ArrayList<BookingData> bookingList = bookingController.getAllBookings();
        assertNotNull(bookingList);
    }
}
