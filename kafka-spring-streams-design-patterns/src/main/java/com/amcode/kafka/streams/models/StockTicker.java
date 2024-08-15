package com.amcode.kafka.streams.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class StockTicker {

    private String stock;
    private float stockPrice;

    public StockTicker(String stock, float price){
        this.stock = stock;
        this.stockPrice = price;
    }

}
