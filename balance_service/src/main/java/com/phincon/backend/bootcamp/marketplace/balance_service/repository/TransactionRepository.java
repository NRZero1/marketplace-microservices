package com.phincon.backend.bootcamp.marketplace.balance_service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.phincon.backend.bootcamp.marketplace.balance_service.model.Transaction;

@Repository
public interface TransactionRepository extends R2dbcRepository<Transaction, Long> {
}
