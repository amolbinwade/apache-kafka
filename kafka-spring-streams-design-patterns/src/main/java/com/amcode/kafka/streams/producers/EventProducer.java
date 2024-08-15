package com.amcode.kafka.streams.producers;

import com.amcode.kafka.streams.models.StockTicker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class EventProducer {

    @Autowired
    KafkaTemplate<String, StockTicker> kafkaTemplate;

    Logger logger = LoggerFactory.getLogger(EventProducer.class.getName());

    @Value("${kafka.topic}")
    String topic;

    public void sendEvent(StockTicker ticker)  {
        logger.warn("Sending message to Kafka topic.");
        CompletableFuture<SendResult<String, StockTicker>> future = kafkaTemplate
                .send(topic, ticker.getStock(), ticker);

        future.handle((r,e) -> {
            if(e==null){
                logger.warn("Message produced to Kafka Topic.");
                logger.warn(r.getRecordMetadata().toString());
            } else {
                logger.error("Error while producing event.", e);
            }
            return 1;
        }).thenRun(()->{
            logger.warn("Send operation completed");
        });
    }
}
