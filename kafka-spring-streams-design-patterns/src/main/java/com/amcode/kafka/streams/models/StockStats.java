package com.amcode.kafka.streams.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class StockStats {

    static Logger logger = LoggerFactory.getLogger(StockStats.class);
    private String stock;
    private float currentPrice;
    private float maxPrice;
    private float minPrice;
    private long latestEventDate;

    public StockStats add(StockTickerRecord record){
        logger.warn("## StockTickerRecord : {}", record);
        this.stock = record.stock();
        this.currentPrice = record.stockPrice();
        this.maxPrice = Math.max(this.maxPrice, record.stockPrice());
        this.minPrice = this.minPrice==0?record.stockPrice():this.minPrice;
        this.minPrice = Math.min(this.minPrice, record.stockPrice());
        this.latestEventDate = record.eventDate();
        logger.warn("## StockStats : {}", this);
        return this;
    }

    public String toString(){
        return "SotckStats : "
                +this.stock
                +"  maxPrice: "+this.maxPrice
                +"  minPrice: "+this.minPrice
                +"  latestEventDate: "+new Date(latestEventDate);
    }
}
