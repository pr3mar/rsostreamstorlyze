package com.rsostream.storlyze.services;

import com.google.gson.Gson;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import com.rsostream.storlyze.models.device.Device;
import com.rsostream.storlyze.properties.PropertiesMongoDB;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class ServiceMongoDB {

    private static final Logger log = LogManager.getLogger(ServiceMongoDB.class.getName());


    @Inject
    PropertiesMongoDB propertiesMongoDB;

    private MongoClient mongoClient;
    private MongoDatabase db;
//    private MongoCollection<Document> deviceCollection;
    private MongoCollection<Device> deviceCollection;
    private MongoCollection<Document> settingsCollection;
    private MongoCollection<Document> sensorDataCollection;

    private void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        mongoClient = new MongoClient(
                new MongoClientURI(propertiesMongoDB.getUri())
            );

        db = mongoClient.getDatabase(propertiesMongoDB.getDb());
        db = db.withCodecRegistry(pojoCodecRegistry);
        deviceCollection = db.getCollection(propertiesMongoDB.getDeviceCollection(), Device.class);
//        deviceCollection = db.getCollection(propertiesMongoDB.getDeviceCollection());
        settingsCollection = db.getCollection(propertiesMongoDB.getSettingsCollection());
        sensorDataCollection = db.getCollection(propertiesMongoDB.getSensorDataCollection());
    }

    private void stop(@Observes @Destroyed(ApplicationScoped.class) Object destroyed) {
        mongoClient.close();
    }

    public Device find() {
        try {
            deviceCollection.insertOne(new Device("newer device!"));
            /*Block<Device> printBlock = new Block<Device>() {
                @Override
                public void apply(Device device) {
                    log.info(device.toString());
                }
            };
            deviceCollection.find().forEach(printBlock);*/
//            Document doc = deviceCollection.find().first();
            Device doc = deviceCollection.find().first();
            // a very bad hack.
            /*Gson deserializer = new Gson();
            Device doc = deserializer.fromJson(deviceCollection.find().first().toJson(), Device.class);*/
            log.info(":D :D :D FOUND:" + doc);
            return doc;
        } catch (NullPointerException e) {
            log.info("Not found anything :(");
            e.printStackTrace();
            return null;
        }
    }
}
