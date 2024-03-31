package com.amcode.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic}")
    String topic;

    public void sendEvent(String eventString){
        kafkaTemplate.send(topic, eventString);
    }
}
