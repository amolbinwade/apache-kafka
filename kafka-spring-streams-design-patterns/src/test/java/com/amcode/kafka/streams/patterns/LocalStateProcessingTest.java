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
import java.util.List;
import java.util.Properties;

public class LocalStateProcessingTest {

    private LocalStateProcessing localStateProcessing;
    private Properties props = new Properties();
    private String eventDate;

    @BeforeEach
    public void setup() {
        localStateProcessing = new LocalStateProcessing();
        localStateProcessing.setBOOTSTRAP_SERVER("test");
        localStateProcessing.setKAFKA_TOPIC("input_topic");
        localStateProcessing.setLOCAL_STATE_STATS_KAFKA_TOPIC("output_topic");
        props.putAll(localStateProcessing.getConfigs());
        props.put(StreamsConfig.InternalConfig.EMIT_INTERVAL_MS_KSTREAMS_WINDOWED_AGGREGATION,"0");
        eventDate = "01-Sep-2024 11:11:11";
    }

    @Test
    public void testLocalStateProcessing() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        localStateProcessing.localStateProcessingKStream(streamsBuilder);
        Topology topology = streamsBuilder.build();

        try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, props)) {
            TestInputTopic<String, StockTickerRecord> inputTopic = topologyTestDriver
                    .createInputTopic("input_topic", new StringSerializer(),
                            new JsonSerializer<StockTickerRecord>());
            TestOutputTopic<String, StockStats> outputTopic = topologyTestDriver
                    .createOutputTopic("output_topic", new StringDeserializer(),
                            new JsonDeserializer<StockStats>(StockStats.class));
            populateInputTopic(inputTopic);
            List<KeyValue<String, StockStats>> list = outputTopic.readKeyValuesToList();
            list.forEach(System.out::println);

        }
    }

    private void populateInputTopic(TestInputTopic<String, StockTickerRecord> inputTopic) {
        LocalDate localDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        LocalDateTime localDateTime = LocalDateTime.of(localDate.getYear(),
                localDate.getMonthValue(),
                localDate.getDayOfMonth(), 12, 0, 18);
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

        inputTopic.pipeInput("ITC",
                new StockTickerRecord("01-Sep-2024 11:11:00", "ITC", 101.11f),
                instant);

        inputTopic.pipeInput("ITC",
                new StockTickerRecord("01-Sep-2024 11:11:01", "ITC", 102.11f),
                instant.plusSeconds(1));

        inputTopic.pipeInput("ITC",
                new StockTickerRecord("01-Sep-2024 11:11:02", "ITC", 103.11f),
                instant.plusSeconds(2));

        inputTopic.pipeInput("ITC",
                new StockTickerRecord("01-Sep-2024 11:11:03", "ITC", 100.11f),
                instant.plusSeconds(3));

        inputTopic.pipeInput("ITC",
                new StockTickerRecord("01-Sep-2024 11:11:04", "ITC", 107.11f),
                instant.plusSeconds(4));

        inputTopic.pipeInput("ITC",
                new StockTickerRecord("01-Sep-2024 11:11:05", "ITC", 99.11f),
                instant.plusSeconds(5));

        inputTopic.pipeInput("ITC",
                new StockTickerRecord("01-Sep-2024 11:11:06", "ITC", 98.11f),
                instant.plusSeconds(6));

        inputTopic.pipeInput("ITC",
                new StockTickerRecord("01-Sep-2024 11:11:07", "ITC", 97.11f),
                instant.plusSeconds(7));

        inputTopic.pipeInput("ITC",
                new StockTickerRecord("01-Sep-2024 11:11:08", "ITC", 88.11f),
                instant.plusSeconds(8));

        inputTopic.pipeInput("ITC",
                new StockTickerRecord("01-Sep-2024 11:11:09", "ITC", 99.21f),
                instant.plusSeconds(9));
    }


}
