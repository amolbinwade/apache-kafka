package com.amcode.kafka.stopwatch;

import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.Date;

public class StopWatch {

    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;
    private CountDownLatch latch;
    private SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");

    private static final StopWatch instance = new StopWatch();

    public static StopWatch getStopWatch(){
        return instance;
    }

    public void setCountDown(int countDown){
        if(latch==null)
            latch = new CountDownLatch(countDown);
        else if(latch.getCount()==0)
            latch = new CountDownLatch(countDown);
        else
            throw new IllegalArgumentException("Count down already set");
    }

    public CountDownLatch getCountDownLatch(){
        return this.latch;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    public String getStartTime(){
        Date date = new Date(startTime);
        return format.format(date);
    }

    public String getStopTime(){
        Date date = new Date(stopTime);
        return format.format(date);
    }


    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }


    //elaspsed time in milliseconds
    public long getElapsedTime() {
        long elapsed;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }


    //elaspsed time in seconds
    public long getElapsedTimeSecs() {
        long elapsed;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000);
        } else {
            elapsed = ((stopTime - startTime) / 1000);
        }
        return elapsed;
    }

    public boolean isRunning(){
        return running;
    }
}
