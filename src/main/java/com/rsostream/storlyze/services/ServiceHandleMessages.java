package com.rsostream.storlyze.services;

import com.google.gson.Gson;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.fault.tolerance.annotations.GroupKey;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.rsostream.storlyze.models.sensorReadings.*;
import com.rsostream.storlyze.properties.PropertiesCRUD;
import com.rsostream.storlyze.util.GsonUtils;
import com.rsostream.storlyze.util.InvalidMessageException;
import com.rsostream.storlyze.util.ResourceNotAvailableException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.microprofile.faulttolerance.Fallback;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
@GroupKey("crud")
public class ServiceHandleMessages {
    private static final Logger log = LogManager.getLogger(ServiceHandleMessages.class.getName());

    private List<SensorReading> fallbackStorage = new ArrayList<SensorReading>();

    @Inject
    private ServiceMongoDB serviceMongoDB;

    @Inject
    private PropertiesCRUD propertiesCRUD;

    @Inject
    @DiscoverService(value = "rsostream-crud", version = "1.0.0", environment = "dev")
    private Optional<WebTarget> crudService;

    @Fallback(fallbackMethod = "fallbackStore")
    public void handleData(String receivedData) throws InvalidMessageException, ResourceNotAvailableException {
        Gson deserializer = new Gson();
        String[] splitted = receivedData.split("&&");
        log.error("LOOK AT THIS: " + splitted[0] + ", " + splitted[1]);
        SensorReading receivedReading;
        String path = propertiesCRUD.getReadingBaseUri();
        switch (splitted[0]) {
            case "ALT":
                receivedReading = deserializer.fromJson(splitted[1], AltitudeReading.class);
                path += "alt";
                break;
            case "BAT":
                receivedReading = deserializer.fromJson(splitted[1], BatteryReading.class);
                path += "bat";
                break;
            case "GPS":
                receivedReading = deserializer.fromJson(splitted[1], GPSReading.class);
                path += "gps";
                break;
            case "HUM":
                receivedReading = deserializer.fromJson(splitted[1], HumidityReading.class);
                path += "hum";
                break;
            case "LUX":
                receivedReading = deserializer.fromJson(splitted[1], LuxReading.class);
                path += "lux";
                break;
            case "SUPER":
//                receivedReading = deserializer.fromJson(splitted[1], SensorReading.class);
            default:
                log.error("Received an unknown type of message!");
                throw new InvalidMessageException("Message type not valid!");
        }
        log.info("Found: " + receivedReading.toString());
        deserializer = GsonUtils.getGson();
        if(crudService.isPresent()) {
            try {
//                receivedReading.setTimeObtained(null);
                WebTarget crud = crudService.get().path(path);
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost request = new HttpPost(crud.getUri());
                StringEntity param = new StringEntity(deserializer.toJson(receivedReading));
                request.addHeader("content-type", "application/json");
                request.setEntity(param);
                HttpResponse response = httpClient.execute(request);
                log.error(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*WebTarget crud = crudService.get().path(path);
//            receivedReading.setTimeObtained(null);
            Response response = null;
            try {
                response = crud.request(MediaType.APPLICATION_JSON).post(Entity.json(receivedReading));

            } catch (ProcessingException e) {
                e.printStackTrace();
            }
            if(response != null) {
                log.error(response.toString());
                log.error(response.getEntity().toString());
            } else {
                log.error("NULL!!!!!");
            }*/
            /*log.info("THIS IS THE URI:" + crud.getUri());
            Response response = HandleResponsesService.postResponse(crud, (GPSReading) receivedReading);
            if(response != null) {
                log.error(response.toString());
            } else {
                log.error("RESPONSE IS NULL!!!!");
            }*/
        } else {
            log.error("NOT GOOD!!!");
            throw new ResourceNotAvailableException();
        }
    }

    public void fallbackStore(String receivedData) throws InvalidMessageException {
        Gson deserializer = new Gson();
        String[] splitted = receivedData.split("&&");
        log.error("Entered fallback method!!!");
        SensorReading receivedReading;
        String path = propertiesCRUD.getReadingBaseUri();
        switch (splitted[0]) {
            case "ALT":
                receivedReading = deserializer.fromJson(splitted[1], AltitudeReading.class);
                path += "alt";
                break;
            case "BAT":
                receivedReading = deserializer.fromJson(splitted[1], BatteryReading.class);
                path += "bat";
                break;
            case "GPS":
                receivedReading = deserializer.fromJson(splitted[1], GPSReading.class);
                path += "gps";
                break;
            case "HUM":
                receivedReading = deserializer.fromJson(splitted[1], HumidityReading.class);
                path += "hum";
                break;
            case "LUX":
                receivedReading = deserializer.fromJson(splitted[1], LuxReading.class);
                path += "lux";
                break;
            case "SUPER":
//                receivedReading = deserializer.fromJson(splitted[1], SensorReading.class);
            default:
                log.error("Received an unknown type of message!");
                throw new InvalidMessageException("Message type not valid!");
        }
        fallbackStorage.add(receivedReading);
        log.info("Storage size: " + fallbackStorage.size());
    }
}
