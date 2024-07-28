package com.amcode.kafka.consumer;

import com.amcode.kafka.stopwatch.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    Logger logger = LoggerFactory.getLogger(EventConsumer.class.getName());

    @KafkaListener(
            topics= {"${kafka.topic}"},
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void consumeEvent(String message){
        logger.warn("Received events : {}", message);
        try {
            logger.warn("Processing event : {}", message);
            Thread.sleep(1000);
            logger.warn("Processed event : {}", message);
            if(StopWatch.getStopWatch().getCountDownLatch() != null) {
                StopWatch.getStopWatch().getCountDownLatch().countDown();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
