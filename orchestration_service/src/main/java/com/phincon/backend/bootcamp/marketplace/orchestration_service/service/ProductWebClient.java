package com.phincon.backend.bootcamp.marketplace.orchestration_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.phincon.backend.bootcamp.marketplace.dto.Response.ProductStockResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductWebClient {
    @Autowired
    @Qualifier("product")
    private WebClient webClient;

    public Mono<ProductStockResponse> deductProduct(long id, int quantity) {
        log.trace("Entering deductProduct function");
        String uriPath = String.format("localhost:8081/api/product/%d/deduct", id);
        Mono<ProductStockResponse> result = this.webClient.patch()
                .uri((builder) -> builder.path(uriPath).queryParam("amount", quantity).build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductStockResponse.class);
        return result;
    }
}
