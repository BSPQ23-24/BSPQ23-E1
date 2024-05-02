package com.aerologix.app.client.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.FlightData;


import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
public class FlightControllerTest {

	@Mock
    private Client client;

    @Mock(answer=Answers.RETURNS_DEEP_STUBS)
    private WebTarget webTarget;
    
    @Captor
    private ArgumentCaptor<Entity<FlightData>> flightDataEntityCaptor;
    
    private AeroLogixClient exampleClient;
    private FlightController flightController;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // prepare static mock of ClientBuilder
        try (MockedStatic<ClientBuilder> clientBuilder = Mockito.mockStatic(ClientBuilder.class)) {
            clientBuilder.when(ClientBuilder::newClient).thenReturn(client);
            when(client.target(String.format("http://%s:%s/rest/aerologix", System.getProperty("aerologix.hostname"), System.getProperty("aerologix.port")))).thenReturn(webTarget);
            
            exampleClient = AeroLogixClient.getInstance();
            flightController = FlightController.getInstance();
        }
    }

    @Test
    public void testCreateFlight() {
        when(webTarget.path("/flight/create")).thenReturn(webTarget);

        Response response = Response.ok().build();
        when(webTarget.request(MediaType.APPLICATION_JSON).post(any(Entity.class))).thenReturn(response);
        
        assertEquals(0,flightController.createFlight("Bilbao", "Madrid", 1, 1));
        
        
        verify(webTarget.request(MediaType.APPLICATION_JSON)).post(flightDataEntityCaptor.capture());
        assertEquals("Bilbao", flightDataEntityCaptor.getValue().getEntity().getOrigin());
        assertEquals("Madrid", flightDataEntityCaptor.getValue().getEntity().getDestination());
        assertEquals(1,flightDataEntityCaptor.getValue().getEntity().getDate());
        assertEquals(1,flightDataEntityCaptor.getValue().getEntity());
    }

	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
