package com.amcode.kafka.streams.patterns;

import com.amcode.kafka.streams.models.StockStats;
import com.amcode.kafka.streams.models.StockTickerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LocalStateProcessingTest {

    private LocalStateProcessing localStateProcessing;
    private Properties props = new Properties();
    private String eventDate, getEventDate2;

    @BeforeEach
    public void setup(){
        localStateProcessing = new LocalStateProcessing();
        localStateProcessing.setBOOTSTRAP_SERVER("test");
        localStateProcessing.setKAFKA_TOPIC("input_topic");
        localStateProcessing.setLOCAL_STATE_STATS_KAFKA_TOPIC("output_topic");
        props.putAll(localStateProcessing.getConfigs());
    }

    @Test
    public void testLocalStateProcessing(){
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        localStateProcessing.localStateProcessingKStream(streamsBuilder);
        Topology topology = streamsBuilder.build();

        try(TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, props)){
            TestInputTopic<String, StockTickerRecord> inputTopic = topologyTestDriver
                    .createInputTopic("input_topic", new StringSerializer(), new JsonSerializer<StockTickerRecord>());
            TestOutputTopic<String, StockStats> outputTopic = topologyTestDriver
                    .createOutputTopic("output_topic", new StringDeserializer(), new JsonDeserializer<StockStats>(StockStats.class));

            LocalDate localDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
                    LocalDateTime localDateTime = LocalDateTime.of(localDate.getYear(),
                    localDate.getMonthValue(),
                    localDate.getDayOfMonth(), 12, 0, 18);
            Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

            inputTopic.pipeInput("ITC", new StockTickerRecord(eventDate, "ITC", 101.11f), instant);

            inputTopic.pipeInput("ITC", new StockTickerRecord(eventDate, "ITC", 102.11f), instant.plusSeconds(1));

            inputTopic.pipeInput("ITC", new StockTickerRecord(eventDate, "ITC", 103.11f), instant.plusSeconds(2));

            inputTopic.pipeInput("ITC", new StockTickerRecord(eventDate, "ITC", 100.11f), instant.plusSeconds(3));

            inputTopic.pipeInput("ITC", new StockTickerRecord(eventDate, "ITC", 107.11f), instant.plusSeconds(4));


            inputTopic.pipeInput("ITC", new StockTickerRecord(eventDate, "ITC", 99.11f), instant.plusSeconds(5));

            inputTopic.pipeInput("ITC", new StockTickerRecord(eventDate, "ITC", 98.11f), instant.plusSeconds(6));

            inputTopic.pipeInput("ITC", new StockTickerRecord(eventDate, "ITC", 97.11f), instant.plusSeconds(7));

            inputTopic.pipeInput("ITC", new StockTickerRecord(eventDate, "ITC", 88.11f), instant.plusSeconds(8));

            inputTopic.pipeInput("ITC", new StockTickerRecord(eventDate, "ITC", 99.21f), instant.plusSeconds(9));


            List list = outputTopic.readKeyValuesToList();
            list.forEach(System.out::println);

        }


    }


}
