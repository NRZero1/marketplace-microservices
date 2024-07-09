package com.phincon.backend.bootcamp.marketplace.orchestration_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.phincon.backend.bootcamp.marketplace.dto.Response.TransactionResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TransactionWebClient {
    @Autowired
    @Qualifier("transaction")
    private WebClient webClient;

    public Mono<TransactionResponse> debit(long id) {
        log.trace("Entering debit function");
        String uriPath = String.format("localhost:8083/api/transaction");
        Mono<TransactionResponse> result = this.webClient.post()
                .uri((builder) -> builder.path(uriPath).build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TransactionResponse.class);
        return result;
    }
}
