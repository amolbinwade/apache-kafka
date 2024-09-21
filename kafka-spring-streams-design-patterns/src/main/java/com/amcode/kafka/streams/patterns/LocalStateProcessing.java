package com.amcode.kafka.streams.patterns;

import com.amcode.kafka.streams.models.StockStats;
import com.amcode.kafka.streams.models.StockTickerRecord;
import com.amcode.kafka.streams.serdes.StockStatsSerde;
import com.amcode.kafka.streams.serdes.StockTickerRecordSerde;
import lombok.Setter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.time.Duration;

import java.util.HashMap;
import java.util.Map;


/***
 * In Local State Processing pattern, the stream processing application
 * needs to depend on maintaining a local state.
 * For example, to show the Max and Min price of stocks for last 5 minutes window
 * application need to save and keep updating the max and min price values on each
 * new price message.
 *
 */
@Configuration
@Setter
public class LocalStateProcessing {

    @Value("${kafka.topic}")
    String KAFKA_TOPIC;

    @Value("${kafka.topic.pattern2}")
    String LOCAL_STATE_STATS_KAFKA_TOPIC;

    @Value("${kafka.bootstrap-server}")
    private String BOOTSTRAP_SERVER;

    private static final long WINDOW_SIZE = 10000;

    @Bean(name = "LocalStateProcessingStreamsBuilder")
    public FactoryBean<StreamsBuilder> myKStreamBuilder() {
        Map<String, Object> configs = getConfigs();

        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(configs));
    }

    public Map<String, Object> getConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configs.put(StreamsConfig.APPLICATION_ID_CONFIG, "amcode_streams_local_state_processing");
        configs.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        configs.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StockTickerRecordSerde.class.getName());
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        return configs;
    }

    @Bean(name = "LocalStateProcessingKStream")
    public KStream<Windowed<String>, StockStats> localStateProcessingKStream(
            @Qualifier("LocalStateProcessingStreamsBuilder") StreamsBuilder builder) {
        KStream<String, StockTickerRecord> source = builder.stream(KAFKA_TOPIC);
        KStream<Windowed<String>, StockStats> statsStream = source.groupByKey()
                .windowedBy(TimeWindows
                        .ofSizeWithNoGrace(Duration.ofMillis(WINDOW_SIZE))
                        //.advanceBy(Duration.ofMillis(WINDOW_SIZE))
                )
                .<StockStats>aggregate(
                        () -> new StockStats(),
                        (k, v, stockStats) -> stockStats.add(v),
                        Materialized.<String, StockStats, WindowStore<Bytes, byte[]>>as("local_state_processing")
                                .withValueSerde(new StockStatsSerde()))
                //.suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                //.map((wk, value) -> KeyValue.pair(wk.key(), value))
                .peek((key, value) -> System.out.println(
                        "#########################"+
                        "Here : "
                +key+" :: "+value));
        statsStream.to(LOCAL_STATE_STATS_KAFKA_TOPIC, Produced.keySerde(
                WindowedSerdes.timeWindowedSerdeFrom(String.class, WINDOW_SIZE)));

        return statsStream;
    }
}
