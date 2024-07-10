package com.phincon.backend.bootcamp.marketplace.orchestration_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.phincon.backend.bootcamp.marketplace.dto.request.TransactionRequest;
import com.phincon.backend.bootcamp.marketplace.dto.response.BalanceResponse;
import com.phincon.backend.bootcamp.marketplace.dto.request.BalanceRequest;
import com.phincon.backend.bootcamp.marketplace.dto.response.TransactionResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TransactionWebClient {
    @Autowired
    @Qualifier("transaction")
    private WebClient webClient;

    public Mono<TransactionResponse> create(TransactionRequest transactionRequest) {
        log.trace("Entering transaction create function");
        String uriPath = String.format("api/transaction");
        Mono<TransactionResponse> result = this.webClient.post()
                .uri((builder) -> builder.path(uriPath).build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(transactionRequest)
                .retrieve()
                .bodyToMono(TransactionResponse.class)
                .flatMap(rawResponse -> {
                    log.info("Raw response is: {}", rawResponse);
                    return Mono.just(rawResponse);
                })
                .doOnError(response -> log.error(response.getMessage()));
        return result;
    }

    public Mono<BalanceResponse> checkBalance(long id) {
        log.trace("Entering checkBalance function");
        String uriPath = String.format("api/balance/%d", id);
        Mono<BalanceResponse> result = this.webClient.get()
                .uri((builder) -> builder.path(uriPath).build())
                .retrieve()
                .bodyToMono(BalanceResponse.class)
                .flatMap(rawResponse -> {
                    log.info("Raw response is: {}", rawResponse);
                    return Mono.just(rawResponse);
                });
        return result;
    }

    public Mono<TransactionResponse> updateTransactionStatus(TransactionResponse transactionResponse) {
        log.trace("Entering updateTransactionStatus function");
        String uriPath = String.format("api/transaction/%d", transactionResponse.getId());
        log.debug("uriPath value is: {}", uriPath);
        Mono<TransactionResponse> result = this.webClient.put()
                .uri((builder) -> builder.path(uriPath).build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(transactionResponse)
                .retrieve()
                .bodyToMono(TransactionResponse.class)
                .flatMap(rawResponse -> {
                    log.info("Raw response is: {}", rawResponse);
                    return Mono.just(rawResponse);
                });
        return result;
    }

    public Mono<BalanceResponse> updateBalance(long id, BalanceRequest balanceRequest) {
        log.trace("Entering updateBalance function");
        String uriPath = String.format("api/balance/%d", id);
        Mono<BalanceResponse> result = this.webClient.put()
                .uri((builder) -> builder.path(uriPath)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(balanceRequest)
                .retrieve()
                .bodyToMono(BalanceResponse.class)
                .flatMap(rawResponse -> {
                    log.info("Raw response is: {}", rawResponse);
                    return Mono.just(rawResponse);
                })
                .doOnError(response -> log.error(response.getMessage()));
        return result;
    }
}
