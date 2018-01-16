package com.rsostream.storlyze.services;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.rabbitmq.client.*;
import com.rsostream.storlyze.properties.PropertiesRabbitMQ;
import com.rsostream.storlyze.util.InvalidMessageException;
import com.rsostream.storlyze.util.ResourceNotAvailableException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
public class ServiceRabbitMQSubscribe {

    private static final Logger log = LogManager.getLogger(ServiceRabbitMQSubscribe.class.getName());

    @Inject
    private PropertiesRabbitMQ propertiesRabbitMQ;

    @Inject
    private ServiceHandleMessages serviceHandleMessages;

    private Connection connection;
    private Channel channel;

    @Log
    private void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        ConnectionFactory factory = new ConnectionFactory();
        log.info("Username: "+ propertiesRabbitMQ.getUsername());
        log.info("Password: "+ propertiesRabbitMQ.getPassword());
        log.info("Host: "+ propertiesRabbitMQ.getHost());
        factory.setHost(propertiesRabbitMQ.getHost());
        // not required for the basic setup!
        factory.setUsername(propertiesRabbitMQ.getUsername());
        factory.setPassword(propertiesRabbitMQ.getPassword());
        factory.setVirtualHost(propertiesRabbitMQ.getUsername());

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws UnsupportedEncodingException {
                String message = new String(body, "UTF-8");
                log.info("message received:" + message);
                // logic goes here
                try {
                    serviceHandleMessages.handleData(message);
                } catch (InvalidMessageException|ResourceNotAvailableException e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        };

        try {
            System.out.println(propertiesRabbitMQ.getRoutingKey());
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(propertiesRabbitMQ.getRoutingKey(), true, false, false, null);
            channel.basicConsume(propertiesRabbitMQ.getRoutingKey(), true, consumer);
            log.info("Q created!");
        } catch (IOException | TimeoutException e) {
            log.error("ERROR OCCURED WHILE CREATING Q");
            log.error(e.getMessage());
        }
    }

    private void stop(@Observes @Destroyed(ApplicationScoped.class) Object destroyed) {
        try {
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            log.error(e.getMessage());
        }
    }
}
