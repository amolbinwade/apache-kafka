package com.amcode.kafka.streams.patterns;

import com.amcode.kafka.streams.models.StockTickerRecord;
import com.amcode.kafka.streams.serdes.StockTickerRecordSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/***
 * In Single Event Processing pattern, the stream processing app consumes
 * events from the stream/topic, modifies or filters each event and
 * then produces events to another stream or topic.
 * No state maintenance needed in such pattern.
 *
 * In this example,
 * 1. the stream application will listen to the stockTickers topic
 * where Stock Tickers for all Stocks are published
 * 2. It will filter only the messages belonging to a particular stock
 * say ITC and produce it to a stream specific to that stock.
 */

@Configuration
@EnableKafka
//@EnableKafkaStreams
public class SingleEventProcessing {

    @Value("${kafka.topic}")
    String KAFKA_TOPIC;

    @Value("${kafka.topic.pattern1}")
    String DEST_KAFKA_TOPIC;

    @Value("${kafka.bootstrap-server}")
    private String BOOTSTRAP_SERVER;

    //commented below default implementation of KafkaStreamsConfiguration because we
    //are implementing more that one Kafka Streams application here with
    //different Configs specially the APPLICATION_ID_CONFIG
    /*@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration(){
        Map<String, Object> configs = new HashMap<>();

        configs.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configs.put(StreamsConfig.APPLICATION_ID_CONFIG, "amcode_streams_single_event_processing");
        configs.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        configs.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StockTickerRecordSerde.class.getName());
        //configs.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10000);
        return new KafkaStreamsConfiguration(configs);
    }*/

    @Bean(name = "SingleEventProcessingStreamsBuilder")
    public FactoryBean<StreamsBuilder> myKStreamBuilder() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configs.put(StreamsConfig.APPLICATION_ID_CONFIG, "amcode_streams_single_event_processing");
        configs.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        configs.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StockTickerRecordSerde.class.getName());
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(configs));
    }

    @Bean
    public KStream<String, StockTickerRecord> singleEventProcessingKStream(
            @Qualifier("SingleEventProcessingStreamsBuilder") StreamsBuilder streamsBuilder){
        KStream<String, StockTickerRecord> stream = streamsBuilder
                .stream(KAFKA_TOPIC);
        stream
                .filter((k,v) -> k.equals("ITC"))
                .to(DEST_KAFKA_TOPIC);
        System.out.println(stream);
        return stream;
    }

}


