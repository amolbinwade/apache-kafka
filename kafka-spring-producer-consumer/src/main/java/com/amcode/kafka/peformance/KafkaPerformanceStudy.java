package com.amcode.kafka.peformance;

import com.amcode.kafka.producer.EventProducer;
import com.amcode.kafka.stopwatch.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaPerformanceStudy {

    Logger logger = LoggerFactory.getLogger(KafkaPerformanceStudy.class.getName());

    @Autowired
    EventProducer eventProducer;

    public void initiateKafkaPerformanceTest(int msgCount){
        StopWatch.getStopWatch().setCountDown(msgCount);
        StopWatch.getStopWatch().start();
        try {
            Runnable eventRunnable = new Runnable() {
                int count=msgCount;
                int j=1;
                @Override
                public void run() {
                    int i = 1;
                    while(i<=10 && count > 0) {
                        eventProducer.sendEvent("Kafka message" + " : " +j++);
                        i++;
                        count--;
                    }
                }
            };
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(eventRunnable,0,1, TimeUnit.SECONDS);
            while(StopWatch.getStopWatch().isRunning()){
                if(StopWatch.getStopWatch().getCountDownLatch().getCount() == 0){
                    StopWatch.getStopWatch().stop();
                }
            }
            System.out.println("#############################################");
            System.out.println("Start Time:       "+ StopWatch.getStopWatch().getStartTime());
            System.out.println("End Time:         "+ StopWatch.getStopWatch().getStopTime());
            System.out.println("Total time taken: "+StopWatch.getStopWatch().getElapsedTimeSecs()+ " seconds");
            System.out.println("#############################################");

    } catch(Exception e){
            logger.error("Exception occurred while processing request.", e);
        }
    }


}
