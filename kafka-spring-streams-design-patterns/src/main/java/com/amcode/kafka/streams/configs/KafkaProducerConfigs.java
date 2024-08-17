package com.amcode.kafka.streams.configs;

import com.amcode.kafka.streams.models.StockTicker;
import com.amcode.kafka.streams.models.StockTickerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import javax.swing.text.ParagraphView;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfigs {

    @Value("${kafka.bootstrap-server}")
    private String BOOTSTRAP_SERVER;

    @Bean
    public Map<String, Object> producerConfig(){
        Map<String, Object> producerConfig = new HashMap<>();

        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        producerConfig.put(ProducerConfig.ACKS_CONFIG, "-1");
        producerConfig.put(ProducerConfig.BATCH_SIZE_CONFIG,"16");
        producerConfig.put(ProducerConfig.LINGER_MS_CONFIG, "100");
        producerConfig.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "500");
        producerConfig.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,"20000");

        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return producerConfig;
    }

    @Bean
    public ProducerFactory<String, StockTickerRecord> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, StockTickerRecord> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
}
