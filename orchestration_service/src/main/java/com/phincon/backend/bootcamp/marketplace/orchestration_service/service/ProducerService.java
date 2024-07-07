package com.phincon.backend.bootcamp.marketplace.orchestration_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.backend.bootcamp.marketplace.orchestration_service.dto.Test;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProducerService {
    @Autowired
    private KafkaTemplate<String, Test> kafkaTemplate;

    // public void sendMessage(String message) {
    // log.debug(message);
    // kafkaTemplate.send("test", message);
    // }

    // public void sendMessageJson(Test test) {
    // log.debug("Object message is: {}", test);
    // ObjectMapper objectMapper = new ObjectMapper();
    // String jsonString = objectMapper.writeValueAsString(test);
    // kafkaTemplate.send("test", jsonString);
    // }

    public void sendMessage(Test test) {
        log.debug("Object message is: {}", test);

        Message<Test> message = MessageBuilder
                .withPayload(test)
                .setHeader(KafkaHeaders.TOPIC, "test")
                .build();

        kafkaTemplate.send(message);
    }
}
