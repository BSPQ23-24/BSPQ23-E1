package com.aerologix.app.client.controller;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.AirlineData;
import com.aerologix.app.server.pojo.PassengerData;

import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class AirlineControllerTest {

	private AirlineController airlineController;
    private WebTarget webTargetMock;
    private Invocation.Builder invocationBuilderMock;
    private AeroLogixClient clientMock;

    @BeforeEach
    public void setUp() {
        webTargetMock = mock(WebTarget.class);
        invocationBuilderMock = mock(Invocation.Builder.class);

        clientMock = mock(AeroLogixClient.class);
        when(clientMock.getWebTarget()).thenReturn(webTargetMock);

        when(webTargetMock.path(anyString())).thenReturn(webTargetMock);
        when(webTargetMock.queryParam(anyString(), any())).thenReturn(webTargetMock);
        when(webTargetMock.request(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilderMock);

        airlineController = AirlineController.getInstance(clientMock);


        Response responseMock = mock(Response.class);
        when(responseMock.getStatus()).thenReturn(Response.Status.OK.getStatusCode());  
        when(invocationBuilderMock.get()).thenReturn(responseMock);
        when(invocationBuilderMock.post(any())).thenReturn(responseMock);
    }
    
    @Test
    public void testCreateAirline() {
        AirlineData airlineData = new AirlineData();
        airlineData.setId(0);
        airlineData.setName("Test");

        when(invocationBuilderMock.post(any())).thenReturn(Response.ok().status(Response.Status.OK).build());

        int result = airlineController.createAirline(airlineData.getName());
        assertEquals(0, result);
    }

    @Test
    public void testModifyAirline() {
    	AirlineData airlineData = new AirlineData();
        airlineData.setId(0);
        airlineData.setName("Test");

        int result = airlineController.modifyAirline(airlineData.getId(), airlineData.getName());
        assertEquals(0, result);
    }


    @Test
    public void testDeletePassenger() {
        int id = 0;

        int result = airlineController.deleteAirline(id);
        assertEquals(0, result);
    }

}
