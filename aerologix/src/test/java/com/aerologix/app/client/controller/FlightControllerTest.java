package com.aerologix.app.client.controller;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.jdo.Aircraft;
import com.aerologix.app.server.jdo.Flight;
import com.aerologix.app.server.pojo.FlightData;


import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

	        when(webTargetMock.path(anyString())).thenReturn(webTargetMock);
	        when(webTargetMock.queryParam(anyString(), any())).thenReturn(webTargetMock);
	        when(webTargetMock.request(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilderMock);

	        flightController = FlightController.getInstance(clientMock);


	        Response responseMock = mock(Response.class);
	        when(responseMock.getStatus()).thenReturn(Response.Status.OK.getStatusCode());  
	        when(invocationBuilderMock.get()).thenReturn(responseMock);
	        when(invocationBuilderMock.post(any())).thenReturn(responseMock);
	    }
	    @Test
	    public void testCreateFlight() {
	        when(invocationBuilderMock.post(any())).thenReturn(Response.ok().status(Response.Status.OK).build());
	        
	        int result = flightController.createFlight("Bilbao", "Madrid",1, 13);
	        assertEquals(0, result);
	    }
	    @Test
	    public void testModifyFlight() {
	        FlightData flightData = new FlightData();
	        flightData.setIdFlight(1);
	        flightData.setOrigin("Bilbao");
	        

	        int result = flightController.modifyFlight(1,"Bilbao", "Madrid",1, 13, new ArrayList<Integer>());
	        assertEquals(0, result);
	    }
 
	    @Test
	    public void testDeleteFlight() {
	        int result = flightController.deleteFlight(1);
	        assertEquals(0, result);
	    }

}
