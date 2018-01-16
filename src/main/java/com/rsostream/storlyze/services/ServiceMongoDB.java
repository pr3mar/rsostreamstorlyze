package com.rsostream.storlyze.services;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rsostream.storlyze.models.DeviceSettings;
import com.rsostream.storlyze.properties.PropertiesMongoDB;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@ApplicationScoped
public class ServiceMongoDB {

    private static final Logger log = LogManager.getLogger(ServiceMongoDB.class.getName());

    @Inject
    PropertiesMongoDB propertiesMongoDB;

    private MongoClient mongoClient;
    private MongoDatabase db;
//    private MongoCollection<Document> deviceCollection;
    private MongoCollection<DeviceSettings> settingsCollection;

    private void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        mongoClient = new MongoClient(
                new MongoClientURI(propertiesMongoDB.getUri())
            );

        db = mongoClient.getDatabase(propertiesMongoDB.getDb());
        db = db.withCodecRegistry(pojoCodecRegistry);
//        deviceCollection = db.getCollection(propertiesMongoDB.getDeviceCollection(), Device.class);
        settingsCollection = db.getCollection(propertiesMongoDB.getSettingsCollection(), DeviceSettings.class);
//        sensorDataCollection = db.getCollection(propertiesMongoDB.getSensorDataCollection(), SensorReading.class);
    }

    private void stop(@Observes @Destroyed(ApplicationScoped.class) Object destroyed) {
        mongoClient.close();
    }

    public DeviceSettings findSettingsByDeviceId(ObjectId deviceId) {
        try {
            DeviceSettings doc = settingsCollection.find(eq("deviceId", deviceId)).first();
            return doc;
        } catch (NullPointerException e) {
            log.info("Not found anything :(");
            e.printStackTrace();
            return null;
        }
    }
}
