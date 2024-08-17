package com.amcode.kafka.streams.serdes;

import com.amcode.kafka.streams.models.StockTickerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class StockTickerRecordSerde extends Serdes.WrapperSerde<StockTickerRecord> {

    public StockTickerRecordSerde(){
        super(new JsonSerializer<StockTickerRecord>(),
                new JsonDeserializer<StockTickerRecord>(StockTickerRecord.class));
    }
}
