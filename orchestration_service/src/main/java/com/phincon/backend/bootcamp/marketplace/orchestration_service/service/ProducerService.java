package com.phincon.backend.bootcamp.marketplace.orchestration_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.backend.bootcamp.marketplace.orchestration_service.dto.Test;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProducerService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // public void sendMessage(String message) {
    // log.debug(message);
    // kafkaTemplate.send("test", message);
    // }

    public void sendMessageJson(Test test) throws JsonProcessingException {
        log.debug("Object message is: {}", test);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(test);
        kafkaTemplate.send("test", jsonString);
    }
}
