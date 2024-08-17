package com.amcode.kafka.streams.util;

import com.amcode.kafka.streams.producers.EventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.amcode.kafka.streams.models.*;

@Component
public class StockTickersPublisher {

    Logger logger = LoggerFactory.getLogger(StockTickersPublisher.class.getName());

    AtomicBoolean isRunning = new AtomicBoolean(false);

    @Autowired
    EventProducer producer;

    /***
     * This method generates stock prices and produces the StockTickers to Kafka topic every 2 seconds.
     */
    public void produceStockTickersToKafka(){
        try{
            if(isRunning.get())
                return;
            Runnable runnable = getRunnable();
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(getRunnable(), 2, 2, TimeUnit.SECONDS);
            isRunning.set(true);

        }catch(Exception e){
            logger.error("Exception occurred while producing Stock Ticker messages to Kafka topic", e);
        }
    }

    private Runnable getRunnable() {
        return new Runnable() {
            boolean start = true;
            @Override
            public void run() {
                if(start)
                    produceStockTickers();
                start = true;
            }
        };
    }

    public void produceStockTickers() {
        StockTickersGenerator.generateStockTickers()
                .forEach(t -> producer.sendEvent(t));
    }
}
