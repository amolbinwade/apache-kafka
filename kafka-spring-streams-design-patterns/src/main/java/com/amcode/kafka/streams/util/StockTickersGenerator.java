package com.amcode.kafka.streams.util;

import java.util.*;
import java.util.stream.Collectors;

import com.amcode.kafka.streams.models.*;


/***
 * Utility class with static methods to generate random price movement for Stock Tickers
 */
public class StockTickersGenerator {

    private static final Random PRG = new Random();
    private static final StocksEnum[] stocks = StocksEnum.values();
    private static final Map<StocksEnum, Float> currentStocksPriceMap = new HashMap<>();

    /***
     * This method generate stock tickers with random controlled price movement.
     * @return List<StockTicker>
     */
    public static List<StockTicker> generateStockTickers(){
        return Arrays.stream(StocksEnum.values())
                .map(StockTickersGenerator::generateStockTicker)
                .collect(Collectors.toList());
    }

    /***
     * This method generates StockTicker with Random price movement. The current
     * price is stored in the in-memory map.
     * @return
     */
    public static StockTicker generateStockTicker(StocksEnum ticker){
        float price = 0;
        changeStockPrice(ticker);
        return new StockTicker(ticker.code, currentStocksPriceMap.get(ticker));
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
