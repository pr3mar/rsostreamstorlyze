package com.rsostream.storlyze.services;

import com.google.gson.Gson;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.rsostream.storlyze.models.sensorReadings.*;
import com.rsostream.storlyze.util.InvalidMessageException;
import org.eclipse.microprofile.metrics.annotation.Metered;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ServiceHandleMessages {
    private static final Logger log = LogManager.getLogger(ServiceHandleMessages.class.getName());

    @Inject
    private ServiceMongoDB serviceMongoDB;

    @Metered
    public void handleData(String receivedData) throws InvalidMessageException {
        Gson deserializer = new Gson();
        String[] splitted = receivedData.split("&&");
        log.error("LOOK AT THIS: " + splitted[0] + ", " + splitted[1]);
        SensorReading receivedReading;
        switch (splitted[0]) {
            case "SUPER":
                receivedReading = deserializer.fromJson(splitted[1], SensorReading.class);
                break;
            case "ALT":
                receivedReading = deserializer.fromJson(splitted[1], AltitudeReading.class);
                break;
            case "BAT":
                receivedReading = deserializer.fromJson(splitted[1], BatteryReading.class);
                break;
            case "GPS":
                receivedReading = deserializer.fromJson(splitted[1], GPSReading.class);
                break;
            case "HUM":
                receivedReading = deserializer.fromJson(splitted[1], HumidityReading.class);
                break;
            case "LUX":
                receivedReading = deserializer.fromJson(splitted[1], LuxReading.class);
                break;
            default:
                log.error("Received an unknown type of message!");
                throw new InvalidMessageException("Message type not valid!");
        }
        log.info("Found: " + receivedReading.toString());
        return;
    }
}
