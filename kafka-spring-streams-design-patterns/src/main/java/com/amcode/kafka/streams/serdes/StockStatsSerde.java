package com.amcode.kafka.streams.serdes;

import com.amcode.kafka.streams.models.StockStats;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class StockStatsSerde extends Serdes.WrapperSerde<StockStats>{

    public StockStatsSerde() {
        super(new JsonSerializer<StockStats>(),
                new JsonDeserializer<StockStats>(StockStats.class));
    }
}
