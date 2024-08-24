package com.amcode.kafka.streams.patterns;

import com.amcode.kafka.streams.models.StockTickerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.ZonedDateTime;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleEventProcessingTest {

    private SingleEventProcessing singleEventProcessing;
    private Properties props = new Properties();
    private String eventDate, getEventDate2;

    @BeforeEach
    void setup(){
        singleEventProcessing = new SingleEventProcessing();
        singleEventProcessing.setKAFKA_TOPIC("input_topic");
        singleEventProcessing.setDEST_KAFKA_TOPIC("output_topic");
        singleEventProcessing.setBOOTSTRAP_SERVER("test");
        props.putAll(singleEventProcessing.getConfigs());
    }

    @Test
    void givenInputMessages_whenProcessed_filteredMessagesAreProduced(){
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        singleEventProcessing.singleEventProcessingKStream(streamsBuilder);
        Topology topology = streamsBuilder.build();

        try(TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, props)){
            TestInputTopic<String, StockTickerRecord> inputTopic = topologyTestDriver
                    .createInputTopic("input_topic", new StringSerializer(), new JsonSerializer<StockTickerRecord>());

            TestOutputTopic<String, StockTickerRecord> outputTopic = topologyTestDriver
                    .createOutputTopic("output_topic", new StringDeserializer(), new JsonDeserializer<StockTickerRecord>(StockTickerRecord.class));

            eventDate = ZonedDateTime.now().toString();
            inputTopic.pipeInput("ITC", new StockTickerRecord(eventDate, "ITC", 101.11f));
            inputTopic.pipeInput("REL", new StockTickerRecord(eventDate, "ITC", 101.11f));

            getEventDate2 = ZonedDateTime.now().toString();
            inputTopic.pipeInput("REL", new StockTickerRecord(getEventDate2, "ITC", 101.11f));
            inputTopic.pipeInput("ITC", new StockTickerRecord(getEventDate2, "ITC", 110.11f));
            inputTopic.pipeInput("SEZ", new StockTickerRecord(getEventDate2, "ITC", 101.11f));

            assertThat(outputTopic.readKeyValuesToList())
                    .containsExactly(
                            KeyValue.pair("ITC", new StockTickerRecord(eventDate, "ITC", 101.11f)),
                            KeyValue.pair("ITC", new StockTickerRecord(getEventDate2, "ITC", 110.11f))
                    );

        }
    }
}
