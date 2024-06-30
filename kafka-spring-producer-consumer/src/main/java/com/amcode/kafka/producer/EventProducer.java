package com.amcode.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    Logger logger = LoggerFactory.getLogger(EventProducer.class.getName());

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic}")
    String topic;

    public void sendEvent(String eventString){
        kafkaTemplate.send(topic, eventString);
        logger.warn("Message Sent {}", eventString);
    }
}
