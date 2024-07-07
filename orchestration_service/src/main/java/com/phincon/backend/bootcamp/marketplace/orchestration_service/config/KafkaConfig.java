package com.phincon.backend.bootcamp.marketplace.orchestration_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    // @Bean
    // public ProducerFactory<String, String> kafkaProducerFactory() {
    // Map<String, Object> configProps = new HashMap<>();
    // configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    // configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
    // StringSerializer.class);
    // configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
    // StringSerializer.class);
    // return new DefaultKafkaProducerFactory<>(configProps);
    // }

    // @Bean
    // public ConsumerFactory<String, String> kafkaConsumerFactory() {
    // Map<String, Object> props = new HashMap<>();
    // props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    // props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
    // props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
    // StringDeserializer.class);
    // props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
    // StringDeserializer.class);
    // return new DefaultKafkaConsumerFactory<>(props);
    // }

    // @Bean
    // public KafkaTemplate<String, String> kafkaTemplate() {
    // return new KafkaTemplate<>(kafkaProducerFactory());
    // }

    @Bean
    public NewTopic testTopic() {
        return TopicBuilder
                .name("order-service")
                .build();
    }
}
