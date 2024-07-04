package com.phincon.backend.bootcamp.marketplace.balance_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phincon.backend.bootcamp.marketplace.balance_service.model.Transaction;
import com.phincon.backend.bootcamp.marketplace.balance_service.service.TransactionService;
import com.phincon.backend.bootcamp.marketplace.dto.TransactionRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public Flux<Transaction> getAll() {
        return transactionService.getAll();
    }

    @PostMapping
    public Mono<Transaction> create(@RequestBody TransactionRequest transactionRequest) {
        return transactionService.save(transactionRequest);
    }

    @GetMapping("/{id}")
    public Mono<Transaction> getById(@PathVariable long id) {
        return transactionService.getById(id);
    }

    @PutMapping("/{id}")
    public Mono<Transaction> update(@PathVariable long id, TransactionRequest transactionRequest) {
        return transactionService.update(id, transactionRequest);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable long id) {
        return transactionService.delete(id);
    }

    public Mono<Void> deleteAll() {
        return transactionService.deleteAll();
    }
}
