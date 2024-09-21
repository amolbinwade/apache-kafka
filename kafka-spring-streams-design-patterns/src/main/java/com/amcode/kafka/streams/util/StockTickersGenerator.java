package com.amcode.kafka.streams.util;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.amcode.kafka.streams.models.*;


/***
 * Utility class with static methods to generate random price movement for Stock Tickers
 */
public class StockTickersGenerator {

    private static final Random PRG = new Random();
    private static final StocksEnum[] stocks = StocksEnum.values();
    private static final Map<StocksEnum, Float> currentStocksPriceMap = new HashMap<>();

    static{
        AtomicInteger i = new AtomicInteger(1);
        Arrays.stream(StocksEnum.values()).sorted().forEach(
                (s) -> {
                    currentStocksPriceMap.put(s, 100f*i.incrementAndGet());
                }
        );
    }

    /***
     * This method generate stock tickers with random controlled price movement.
     * @return List<StockTickerRecord>
     */
    public static List<StockTickerRecord> generateStockTickers(){
        return Arrays.stream(StocksEnum.values())
                .map(StockTickersGenerator::generateStockTickerRecord)
                .collect(Collectors.toList());
    }

    /***
     * This method generates StockTicker with Random price movement. The current
     * price is stored in the in-memory map.
     * @return
     */
    public static StockTickerRecord generateStockTickerRecord(StocksEnum ticker){
        float price = 0;
        changeStockPrice(ticker);
        return new StockTickerRecord(System.currentTimeMillis(),ticker.code, currentStocksPriceMap.get(ticker));
    }

    private static void changeStockPrice(StocksEnum randomStock) {
        float price;
        if(currentStocksPriceMap.containsKey(randomStock)){
            float cPrice = currentStocksPriceMap.get(randomStock);
            price = cPrice*getRandomMultiplier(cPrice);
        } else {
            price = PRG.nextInt(10)*100+100;
        }
        currentStocksPriceMap.put(randomStock, price);
    }

    private static float getRandomMultiplier(float cPrice) {
        if(PRG.nextInt(10)%2==0){
            return 1+(PRG.nextFloat()/100);
        } else {
            return 1-(PRG.nextFloat()/100);
        }
    }
}
