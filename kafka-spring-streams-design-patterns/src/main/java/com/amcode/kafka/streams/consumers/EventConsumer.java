package com.amcode.kafka.streams.consumers;

import com.amcode.kafka.streams.models.StockTickerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    Logger logger = LoggerFactory.getLogger(EventConsumer.class.getName());

    @KafkaListener(
            topics={"${kafka.topic}"},
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void consumeEvent(StockTickerRecord record){
        //logger.warn("Received Key: {}", key);
        logger.warn("Received value: {}", record);
    }

}
