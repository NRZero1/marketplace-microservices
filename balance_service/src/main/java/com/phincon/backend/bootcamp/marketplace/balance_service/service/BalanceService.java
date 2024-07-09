package com.phincon.backend.bootcamp.marketplace.balance_service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phincon.backend.bootcamp.marketplace.balance_service.repository.BalanceRepository;
import com.phincon.backend.bootcamp.marketplace.dto.request.BalanceRequest;
import com.phincon.backend.bootcamp.marketplace.balance_service.exception.BalanceNotFoundException;
import com.phincon.backend.bootcamp.marketplace.balance_service.model.Balance;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BalanceService {
    @Autowired
    private BalanceRepository balanceRepository;

    public Flux<Balance> getAll() {
        return balanceRepository.findAll();
    }

    public Mono<Balance> getById(long id) {
        return balanceRepository.findById(id).switchIfEmpty(
                Mono.error(
                        new BalanceNotFoundException(
                                String.format(
                                        "Balance not found with ID: ", id))));
    }

    public Mono<Balance> save(BalanceRequest request) {
        return balanceRepository.save(Balance
                .builder()
                .balance(request.getBalance())
                .build());
    }

    public Mono<Balance> update(long id, BalanceRequest balanceRequest) {
        Balance balance = new Balance();
        return balanceRepository.findById(id)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalBalance -> {
                    if (optionalBalance.isPresent()) {
                        balance.setId(id);
                        balance.setBalance(balanceRequest.getBalance());
                        return balanceRepository.save(balance);
                    }

                    return Mono.empty();
                });
    }

    public Mono<Void> delete(long id) {
        return balanceRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return balanceRepository.deleteAll();
    }
}
