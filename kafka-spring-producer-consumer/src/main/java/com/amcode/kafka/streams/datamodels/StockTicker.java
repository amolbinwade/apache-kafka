package com.amcode.kafka.streams.datamodels;

public class StockTicker {

    private StocksEnum stock;
    private float stockPrice;

    private StockTicker(StocksEnum stock, float price){
        this.stock = stock;
        this.stockPrice = price;
    }

}
