package com.phincon.backend.bootcamp.marketplace.order_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic testTopic() {
        return TopicBuilder
                .name("order-service")
                .build();
    }
}
