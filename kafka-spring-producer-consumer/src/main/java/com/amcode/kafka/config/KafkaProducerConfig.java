package com.amcode.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Config class to define KafkaTemplate with custom configs
 * @author Amol Binwade
 */
@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.bootstrap-server}")
    private String BOOTSTRAP_SERVER;

    /**
     * Bean for ProducerConfig map
     * @return producerConfig
     */
    @Bean
    public Map<String, Object> producerConfig(){
        Map<String, Object> producerConfig = new HashMap<>();

            producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);

            //acks set to ALL. It will increase reliability as broker will ack only after event written to minimum ISRs.
            producerConfig.put(ProducerConfig.ACKS_CONFIG, "-1");

            //set BATCH_SIZE and LINGER_MS to increase producer throughput
            producerConfig.put(ProducerConfig.BATCH_SIZE_CONFIG, "16");
            producerConfig.put(ProducerConfig.LINGER_MS_CONFIG, "100");

            //UniformStickyPartitioner is deprecated from V 3.3. Instead set partitioner.ignore.keys=true
            //producerConfig.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, UniformStickyPartitioner.class.getName());
            producerConfig.put(ProducerConfig.PARTITIONER_IGNORE_KEYS_CONFIG, true);

            producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

            return producerConfig;
    }

    /**
     * Bean for producerFactory
     * @return producerFactory
     */

    @Bean
    public ProducerFactory<String, String> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    /**
     * Bean for kafkaTemplate
     * @return kafkaTemplate
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
