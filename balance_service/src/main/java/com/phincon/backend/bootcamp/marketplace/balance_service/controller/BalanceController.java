package com.phincon.backend.bootcamp.marketplace.balance_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.phincon.backend.bootcamp.marketplace.balance_service.model.Balance;
import com.phincon.backend.bootcamp.marketplace.balance_service.service.BalanceService;
import com.phincon.backend.bootcamp.marketplace.dto.BalanceRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {
    @Autowired
    private BalanceService balanceService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Balance> getAll() {
        return balanceService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Balance> create(@RequestBody BalanceRequest balanceRequest) {
        return balanceService.save(balanceRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Balance> getById(@PathVariable long id) {
        return balanceService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Balance> update(@PathVariable long id, BalanceRequest balanceRequest) {
        return balanceService.update(id, balanceRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable long id) {
        return balanceService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAll() {
        return balanceService.deleteAll();
    }
}
