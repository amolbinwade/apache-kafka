package com.amcode.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    Logger logger = LoggerFactory.getLogger(EventConsumer.class.getName());

    @KafkaListener(
            topics=("first_topic"),
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void consumeEvent(String message){
        logger.warn("Received events : {}", message);
        try {
            logger.warn("Processing event : {}", message);
            Thread.sleep(10000);
            logger.warn("Processed event : {}", message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
