package com.rsostream.storlyze.resources;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.rsostream.storlyze.models.Device;
import com.rsostream.storlyze.services.ServiceMongoDB;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.annotation.Metric;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Log
@Path("/storlyze")
@ApplicationScoped
public class StorlyzeResource {

    private static final Logger log = LogManager.getLogger(StorlyzeResource.class.getName());

    @Inject
    @Metric(name = "req_counter")
    private Counter reqCounter;

    @Inject
    private ServiceMongoDB serviceMongoDB;

    @GET
    @Path("/{IMEI}")
    public Response getDevice(@PathParam("IMEI") String imei){
        reqCounter.inc();
//        Device d = serviceMongoDB.findByIMEI(imei);
//        if (d == null) {
//            return Response.status(500).build();
//        }
//        return Response.ok(d.toString()).build();
        return Response.ok().build();
    }

}
