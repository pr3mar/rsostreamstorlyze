package com.rsostream.storlyze.resources;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.rsostream.storlyze.properties.PropertiesCRUD;
import com.rsostream.storlyze.services.ServiceMongoDB;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Log
@Path("/storlyze")
@RequestScoped
public class StorlyzeResource {

    private static final Logger log = LogManager.getLogger(StorlyzeResource.class.getName());

    @Inject
    @Metric(name = "req_counter")
    private Counter reqCounter;

    @Inject
    @DiscoverService(value = "rsostream-crud", version = "1.0.0", environment = "dev")
    private Optional<WebTarget> crudService;

    @Inject
    private PropertiesCRUD propertiesCRUD;

    @Inject
    private ServiceMongoDB serviceMongoDB;

    @GET
    @Path("/{IMEI}")
    @Metered
    @CircuitBreaker
    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "fallbackDevice")
    public Response getDevice(@PathParam("IMEI") String imei){
        reqCounter.inc();
//        Device d = serviceMongoDB.findByIMEI(imei);
//        if (d == null) {
//            return Response.status(500).build();
//        }
//        return Response.ok(d.toString()).build();
        String path = propertiesCRUD.getDeviceBaseUri();
        path += imei;
        WebTarget crud = crudService.get().path(path);
        Response response = crud.request(MediaType.APPLICATION_JSON).get();
        log.error(response.toString());
        return Response.ok(response.getEntity()).build();
    }

    public Response fallbackDevice(String imei) {
        log.error("EXECUTING FALLBACK METHOD!!!");
        return Response.noContent().build();
    }
}
