package com.phincon.backend.bootcamp.marketplace.orchestration_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phincon.backend.bootcamp.marketplace.orchestration_service.dto.Test;
import com.phincon.backend.bootcamp.marketplace.orchestration_service.service.ProducerService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/message")
public class KafkaController {
    @Autowired
    private ProducerService producerService;

    @PostMapping
    public Mono<String> sendMessage(@RequestBody Test test) {
        producerService.sendMessage(test);
        return Mono.just("Message sent with message: " + test);
    }
}
