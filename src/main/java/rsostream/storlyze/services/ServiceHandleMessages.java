package rsostream.storlyze.services;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import rsostream.storlyze.properties.PropertiesMongoDB;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
public class ServiceHandleMessages {

    private static final Logger log = LogManager.getLogger(ServiceHandleMessages.class.getName());


    @Inject
    PropertiesMongoDB propertiesMongoDB;

    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Document> deviceCollection;
    private MongoCollection<Document> settingsCollection;
    private MongoCollection<Document> sensorDataCollection;

    private void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        MongoCredential credential = MongoCredential.createCredential(
                                            propertiesMongoDB.getUsername(),
                                            propertiesMongoDB.getDb(),
                                            propertiesMongoDB.getPassword().toCharArray());
        mongoClient = new MongoClient(
                new MongoClientURI(propertiesMongoDB.getUri())
            );

        db = mongoClient.getDatabase(propertiesMongoDB.getDb());
        deviceCollection = db.getCollection(propertiesMongoDB.getDeviceCollection());
        settingsCollection = db.getCollection(propertiesMongoDB.getSettingsCollection());
        sensorDataCollection = db.getCollection(propertiesMongoDB.getSensorDataCollection());


        try {
            Document doc = deviceCollection.find().first();
            log.info(":D :D :D FOUND:" + doc.toString());
        } catch (NullPointerException e) {
            log.info("Not found anything :(");
        }
    }

    private void stop(@Observes @Destroyed(ApplicationScoped.class) Object destroyed) {
        mongoClient.close();
    }
}
