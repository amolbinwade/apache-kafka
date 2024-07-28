package com.amcode.kafka.controller;

import com.amcode.kafka.peformance.KafkaPerformanceStudy;
import com.amcode.kafka.producer.EventProducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    Logger logger = LoggerFactory.getLogger(EventController.class.getName());

    @Autowired
    EventProducer eventProducer;

    @Autowired
    KafkaPerformanceStudy study;

    @PostMapping("/event")
    public ResponseEntity<String> sendEvent(@RequestBody String eventMessage){
        try {
            eventProducer.sendEvent(eventMessage);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            logger.error("Exception occurred while processing request.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/kafkaPerformanceTest")
    public ResponseEntity<String> startKafkaPerformanceTest(@RequestBody String messageCount){
        try{
            int intMessageCount = Integer.parseInt(messageCount);
            logger.warn("Received request for Kafka performance Test");
            study.initiateKafkaPerformanceTest(intMessageCount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            logger.error("Exception occurred while processing request.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
