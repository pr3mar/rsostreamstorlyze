package com.rsostream.storlyze.services;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.rsostream.storlyze.models.sensorReadings.GPSReading;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

class HandleResponsesService {
    private static final Logger log = LogManager.getLogger(HandleResponsesService.class.getName());
    static Response getResponse(WebTarget service) {
        Response response;
        try {
            response = service.request().get();
        } catch (ProcessingException e) {
            return null;
        }
        return response;
    }

    static Response postResponse(WebTarget service, GPSReading data) {
        Response response;
        try {
            log.error("SENDING = " + Entity.json(data).getEntity().toString());
//            response = service.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(data));
            response = service.request(MediaType.APPLICATION_JSON).post(Entity.entity(data, MediaType.APPLICATION_JSON));
            if(response != null) {
                log.error(response.toString());
//                log.error(response..toString());
                log.error(response.getStatusInfo().toString());
                log.error(Integer.toString(response.getStatus()));
            } else {
                log.error("NULL RESPONSE");
            }
        } catch (ProcessingException e) {
            return null;
        }
        return response;
    }
}
