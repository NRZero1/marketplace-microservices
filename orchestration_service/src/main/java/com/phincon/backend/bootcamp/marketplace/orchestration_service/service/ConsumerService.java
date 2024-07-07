package com.phincon.backend.bootcamp.marketplace.orchestration_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.phincon.backend.bootcamp.marketplace.dto.OrderResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConsumerService {
    // @KafkaListener(topics = "test", groupId = "test")
    // public void consumeMessage(String message) {
    // log.info("Received raw message: {}", message);
    // try {
    // ObjectMapper objectMapper = new ObjectMapper();
    // Test test = objectMapper.readValue(message, Test.class);
    // log.info("The content of the message is: {}", test.toString());
    // } catch (JsonProcessingException e) {
    // log.error("Error deserializing message", e);
    // }
    // }

    @KafkaListener(topics = "order-service", groupId = "orders")
    public void consumeMessage(OrderResponse orderResponse) {
        log.info("The content of the received message is: {}", orderResponse.toString());

    }
}
