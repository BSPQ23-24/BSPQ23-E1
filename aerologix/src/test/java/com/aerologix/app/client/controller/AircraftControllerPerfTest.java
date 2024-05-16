package com.aerologix.app.client.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.AircraftData;
import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Tag("PerformanceTest")
public class AircraftControllerPerfTest {

    private AircraftController aircraftController;
    private WebTarget webTargetMock;
    private Invocation.Builder invocationBuilderMock;
    private AeroLogixClient clientMock;

    @Rule
    public JUnitPerfRule perfTestRule = new JUnitPerfRule(new HtmlReportGenerator("/target/junitperf/report.html"));

    @BeforeEach
    public void setUp() {
        webTargetMock = mock(WebTarget.class);
        invocationBuilderMock = mock(Invocation.Builder.class);

        clientMock = mock(AeroLogixClient.class);
        when(clientMock.getWebTarget()).thenReturn(webTargetMock);

        when(webTargetMock.path(any())).thenReturn(webTargetMock);
        when(webTargetMock.queryParam(any(), any())).thenReturn(webTargetMock);
        when(webTargetMock.request(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilderMock);

        aircraftController = AircraftController.getInstance(clientMock);

        Response responseMock = mock(Response.class);
        when(responseMock.getStatus()).thenReturn(Status.OK.getStatusCode());
        when(responseMock.readEntity(AircraftData.class)).thenReturn(new AircraftData());
        when(responseMock.readEntity(new GenericType<ArrayList<AircraftData>>() {})).thenReturn(new ArrayList<>());
        when(invocationBuilderMock.get()).thenReturn(responseMock);
        when(invocationBuilderMock.post(any(Entity.class))).thenReturn(responseMock);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000)
    public void testCreateAircraft() {
        int result = aircraftController.createAircraft("Boeing", "737", 200);
        assertEquals(0, result);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000)
    public void testDeleteAircraft() {
        int result = aircraftController.deleteAircraft(1);
        assertEquals(0, result);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000)
    public void testModifyAircraft() {
        int result = aircraftController.modifyAircraft(1, "Boeing", "747", 300);
        assertEquals(0, result);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000)
    public void testGetAircraft() {
        AircraftData aircraft = aircraftController.getAircraft(1);
        assertNotNull(aircraft);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000)
    public void testGetAllAircrafts() {
        ArrayList<AircraftData> aircraftList = aircraftController.getAllAircrafts();
        assertNotNull(aircraftList);
    }
}
