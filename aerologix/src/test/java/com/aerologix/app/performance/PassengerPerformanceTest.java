package com.aerologix.app.performance;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.aerologix.app.server.service.PassengerService;
import com.aerologix.app.server.pojo.PassengerData;

public class PassengerPerformanceTest {

    private static final Logger logger = LogManager.getLogger(PassengerPerformanceTest.class);
    private PassengerService passengerService;

    @BeforeEach
    public void setUp() {
        passengerService = new PassengerService();
    }

    @Test
    public void testCreatePassengerPerformance() {
        logger.info("Starting performance test for creating a passenger");

        long startTime = System.currentTimeMillis();

        PassengerData passengerData = new PassengerData();
        passengerData.setDNI("12345678A");
        passengerData.setPhone(123456789);
        passengerData.setName("Ortega");
        passengerData.setEmail("ortega@example.com");
        passengerData.setNationality("ES");
        passengerData.setBirthdate(946684800000L);

        passengerService.createPassenger(passengerData);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        logger.info("Passenger creation test completed. Time taken: {} ms", executionTime);
    }

    @Test
    public void testModifyPassengerPerformance() {
        logger.info("Starting performance test for modifying a passenger");

        long startTime = System.currentTimeMillis();

        PassengerData passengerData = new PassengerData();
        passengerData.setDNI("12345678A");
        passengerData.setPhone(123456789);
        passengerData.setName("Ortega");
        passengerData.setEmail("ortega@example.com");
        passengerData.setNationality("ES");
        passengerData.setBirthdate(946684800000L);

        passengerService.modifyPassenger(passengerData);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        logger.info("Passenger modification test completed. Time taken: {} ms", executionTime);

    }
}