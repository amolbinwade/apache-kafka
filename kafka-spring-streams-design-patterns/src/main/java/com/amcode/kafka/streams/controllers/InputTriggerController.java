package com.amcode.kafka.streams.controllers;

import com.amcode.kafka.streams.util.StockTickersPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
public class InputTriggerController {

    @Autowired
    StockTickersPublisher publisher;

    @GetMapping("stockTickers/start")
    public ResponseEntity<String> startStockTickers(){
        publisher.produceStockTickersToKafka();
        return new ResponseEntity<>("Stock Tickers Started !",HttpStatus.OK);
    }
}
