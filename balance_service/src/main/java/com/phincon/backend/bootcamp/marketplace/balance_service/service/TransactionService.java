package com.phincon.backend.bootcamp.marketplace.balance_service.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phincon.backend.bootcamp.marketplace.balance_service.model.Transaction;
import com.phincon.backend.bootcamp.marketplace.balance_service.repository.TransactionRepository;
import com.phincon.backend.bootcamp.marketplace.dto.Request.TransactionRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Flux<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Mono<Transaction> getById(long id) {
        return transactionRepository.findById(id);
    }

    public Mono<Transaction> save(TransactionRequest request) {
        return transactionRepository.save(Transaction
                .builder()
                .amount(request.getAmount())
                .orderId(request.getOrderId())
                .paymentDate(LocalDateTime.now())
                .mode(request.getMode())
                .status(request.getStatus())
                .referenceNumber(request.getReferenceNumber())
                .build());
    }

    public Mono<Transaction> update(long id, TransactionRequest request) {
        Transaction transaction = new Transaction();
        return transactionRepository.findById(id)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalTransaction -> {
                    if (optionalTransaction.isPresent()) {
                        transaction.setId(id);
                        transactionRepository.save(transaction);
                    }

                    return Mono.empty();
                });
    }

    public Mono<Void> delete(long id) {
        return transactionRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return transactionRepository.deleteAll();
    }
}
