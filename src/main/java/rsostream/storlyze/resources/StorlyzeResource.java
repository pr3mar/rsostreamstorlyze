package rsostream.storlyze.resources;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.annotation.Metric;
import rsostream.storlyze.properties.PropertiesRabbitMQ;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Log
@Path("/storlyze")
@ApplicationScoped
public class StorlyzeResource {

    private static final Logger log = LogManager.getLogger(StorlyzeResource.class.getName());

    @Inject
    private PropertiesRabbitMQ propertiesRabbitMQ;

    @Inject
    @Metric(name = "req_counter")
    private Counter reqCounter;


    @GET
    public Response getDeviceSettings(){
        return Response.ok().build();
    }

}
