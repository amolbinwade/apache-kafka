package com.amcode.kafka.streams.producers;

import com.amcode.kafka.streams.models.StockTickerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import org.springframework.kafka.core.KafkaTemplate;


import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;

@Component
public class EventProducer {

    @Autowired
    KafkaTemplate<String, StockTickerRecord> kafkaTemplate;

    Logger logger = LoggerFactory.getLogger(EventProducer.class.getName());

    @Value("${kafka.topic}")
    String topic;

    public void sendEvent(StockTickerRecord ticker)  {
        logger.warn("Sending message to Kafka topic. {}", ticker.eventDate());
        CompletableFuture<SendResult<String, StockTickerRecord>> future = kafkaTemplate
                .send(topic, ticker.stock(), ticker);
        logger.warn("Producing message: {}", ticker);


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

    public static void main(String[] args) {
        ZonedDateTime dt = ZonedDateTime.now();
        System.out.println("dt: "+dt);
        String dts = dt.toString();
        System.out.println("dt String: "+ dts);
        ZonedDateTime dt1 = ZonedDateTime.parse(dts);
        System.out.println("dt1 :"+dt1);

    }
}
