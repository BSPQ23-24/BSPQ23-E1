package com.aerologix.app.client.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.PassengerData;
import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

@Tag("PerformanceTest")
public class PassengerControllerPerfTest {

    private PassengerController passengerController;
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

        when(webTargetMock.path(anyString())).thenReturn(webTargetMock);
        when(webTargetMock.queryParam(anyString(), any())).thenReturn(webTargetMock);
        when(webTargetMock.request(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilderMock);

        passengerController = PassengerController.getInstance(clientMock);


        Response responseMock = mock(Response.class);
        when(responseMock.getStatus()).thenReturn(Response.Status.OK.getStatusCode());  
        when(invocationBuilderMock.get()).thenReturn(responseMock);
        when(invocationBuilderMock.post(any())).thenReturn(responseMock);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000)
    public void testCreatePassenger() {
        PassengerData passengerData = new PassengerData();
        passengerData.setDNI("12345678A");
        passengerData.setPhone(123456789);
        passengerData.setName("Ortega");
        passengerData.setEmail("oretega@example.com");
        passengerData.setNationality("ES");
        passengerData.setBirthdate(946684800000L);


        when(invocationBuilderMock.post(any())).thenReturn(Response.ok().status(Response.Status.OK).build());

        int result = passengerController.createPassenger(passengerData);
        assertEquals(0, result);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000)
    public void testModifyPassenger() {
        PassengerData passengerData = new PassengerData();
        passengerData.setDNI("12345678A");
        passengerData.setName("Gaset");

        int result = passengerController.modifyPassenger(passengerData);
        assertEquals(0, result);
    }


    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000)
    public void testDeletePassenger() {
        String dni = "12345678A";

        int result = passengerController.deletePassenger(dni);
        assertEquals(0, result);
    }

}
