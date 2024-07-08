package com.phincon.backend.bootcamp.marketplace.orchestration_service.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    @Qualifier("order")
    public WebClient orderClient() {
        return WebClient.builder()
                .baseUrl("localhost:8084")
                .build();
    }

    @Bean
    @Qualifier("transaction")
    public WebClient paymentClient() {
        return WebClient.builder()
                .baseUrl("localhost:8083")
                .build();
    }

    @Bean
    @Qualifier("product")
    public WebClient productClient() {
        return WebClient.builder()
                .baseUrl("localhost:8081")
                .build();
    }
}
