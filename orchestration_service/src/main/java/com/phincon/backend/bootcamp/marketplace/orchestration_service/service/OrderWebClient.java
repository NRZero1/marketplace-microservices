package com.phincon.backend.bootcamp.marketplace.orchestration_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.phincon.backend.bootcamp.marketplace.dto.Response.OrderResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderWebClient {
    @Autowired
    @Qualifier("order")
    private WebClient webClient;

    public Mono<OrderResponse> updateStatus(long id, String status) {
        log.trace("Entering updateStatus function");
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("orderStatus", status);
        String uriPath = String.format("localhost:8084/api/order/%d/updateStatus", id);
        Mono<OrderResponse> result = this.webClient.patch()
                .uri((builder) -> builder.path(uriPath).build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bodyMap))
                .retrieve()
                .bodyToMono(OrderResponse.class);
        return result;
    }
}
