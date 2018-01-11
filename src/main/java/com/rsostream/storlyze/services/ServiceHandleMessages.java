package com.rsostream.storlyze.services;

import com.google.gson.Gson;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.rsostream.storlyze.models.sensorReadings.SensorReading;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ServiceHandleMessages {
    private static final Logger log = LogManager.getLogger(ServiceHandleMessages.class.getName());

    @Inject
    private ServiceMongoDB serviceMongoDB;

    public void handleData(String receivedData) {
        Gson deserializer = new Gson();
        String[] splitted = receivedData.split(",");

        SensorReading receivedReading;
        switch (splitted[0]) {
            case "SUPER":
                receivedReading = deserializer.fromJson(splitted[1], SensorReading.class);
            case "ALT":
                receivedReading = deserializer.fromJson(splitted[1], SensorReading.class);
            case "BAT":
                receivedReading = deserializer.fromJson(splitted[1], SensorReading.class);
            case "GPS":
                receivedReading = deserializer.fromJson(splitted[1], SensorReading.class);
            case "HUM":
                receivedReading = deserializer.fromJson(splitted[1], SensorReading.class);
            case "LUX":
                receivedReading = deserializer.fromJson(splitted[1], SensorReading.class);
            default:
                log.error("Received an unknown type of message!");
                return;
        }
    }
}
