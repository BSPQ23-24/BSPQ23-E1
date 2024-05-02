package com.aerologix.app.client.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.FlightData;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class FlightControllerTest {

    private FlightController flightController;
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

        flightController = FlightController.getInstance(clientMock);

        Response responseMock = mock(Response.class);
        when(responseMock.getStatus()).thenReturn(Status.OK.getStatusCode());
        when(responseMock.readEntity(FlightData.class)).thenReturn(new FlightData());
        when(responseMock.readEntity(new GenericType<ArrayList<FlightData>>() {})).thenReturn(new ArrayList<>());
        when(invocationBuilderMock.get()).thenReturn(responseMock);
        when(invocationBuilderMock.post(any(Entity.class))).thenReturn(responseMock);
    }

    @Test
    public void testCreateFlight() {
        int result = flightController.createFlight("Origin", "Destination", 1234567890L, 1);
        assertEquals(0, result);
    }

    @Test
    public void testDeleteFlight() {
        int result = flightController.deleteFlight(1);
        assertEquals(0, result);
    }

    @Test
    public void testModifyFlight() {
        List<Integer> bookings = new ArrayList<>();
        bookings.add(1);
        int result = flightController.modifyFlight(1, "Origin", "Destination", 1234567890L, 1, bookings);
        assertEquals(0, result);
    }

    @Test
    public void testGetFlight() {
        FlightData flight = flightController.getFlight(1);
        assertNotNull(flight);
    }

    @Test
    public void testGetAllFlights() {
        ArrayList<FlightData> flightList = flightController.getAllFlights();
        assertNotNull(flightList);
    }
}
