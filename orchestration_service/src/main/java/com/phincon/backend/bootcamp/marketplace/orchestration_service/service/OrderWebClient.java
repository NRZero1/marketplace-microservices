package com.phincon.backend.bootcamp.marketplace.orchestration_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.phincon.backend.bootcamp.marketplace.dto.response.OrderResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderWebClient {
    @Autowired
    @Qualifier("order")
    private WebClient webClient;

    public Mono<OrderResponse> updateStatus(OrderResponse orderResponse) {
        log.trace("Entering updateStatus function");
        String uriPath = String.format("api/order/%d/updateStatus", orderResponse.getId());
        Mono<OrderResponse> result = this.webClient.patch()
                .uri((builder) -> builder.path(uriPath).build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(orderResponse)
                .retrieve()
                .bodyToMono(OrderResponse.class);
        return result;
    }
}
