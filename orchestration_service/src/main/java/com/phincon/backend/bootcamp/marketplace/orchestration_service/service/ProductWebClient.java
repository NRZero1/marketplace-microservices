package com.phincon.backend.bootcamp.marketplace.orchestration_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.phincon.backend.bootcamp.marketplace.dto.response.ProductStockResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductWebClient {
    @Autowired
    @Qualifier("product")
    private WebClient webClient;

    public Mono<ProductStockResponse> deductProduct(Long id, int quantity) {
        log.trace("Entering deductProduct function");
        String uriPath = String.format("api/product/%s/deduct", id.toString());
        log.info("value of uriPath is {}", uriPath);
        Mono<ProductStockResponse> result = this.webClient.patch()
                .uri((builder) -> builder.path(uriPath).queryParam("amount", quantity).build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductStockResponse.class)
                .flatMap(rawResponse -> {
                    log.info("Raw response is: {}", rawResponse);
                    return Mono.just(rawResponse);
                })
                .doOnError(response -> log.error(response.getMessage()));
        return result;
    }

    public Mono<ProductStockResponse> addStock(long id, int quantity) {
        log.trace("Entering addStock function");
        String uriPath = String.format("localhost:8081/api/product/%d/addStock", id);
        Mono<ProductStockResponse> result = this.webClient.patch()
                .uri((builder) -> builder.path(uriPath).queryParam("amount", quantity).build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductStockResponse.class);
        return result;
    }

    public Mono<ProductStockResponse> getProduct(long id) {
        log.trace("Entering getProduct function");
        String uriPath = String.format("localhost:8081/api/product/%d/", id);
        Mono<ProductStockResponse> result = this.webClient.get()
                .uri((builder) -> builder.path(uriPath).build())
                .retrieve()
                .bodyToMono(ProductStockResponse.class);
        return result;
    }
}
