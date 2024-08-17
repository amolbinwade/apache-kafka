package com.amcode.kafka.streams.configs;

import com.amcode.kafka.streams.models.StockTickerRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.bootstrap-server}")
    private String kafkaBootstrapServer;

    public Map<String, Object> getConfigs(){
        Map<String, Object> configs = new HashMap<>();

        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "Kafka_Amcode_Stream_App_Consumer_Group");
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return configs;
    }

    @Bean
    public ConsumerFactory<String, StockTickerRecord> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(getConfigs(), new StringDeserializer(), new JsonDeserializer<>(StockTickerRecord.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StockTickerRecord> concurrentKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, StockTickerRecord> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(1);
        return factory;
    }

}
