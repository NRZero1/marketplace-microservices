package com.phincon.backend.bootcamp.marketplace.balance_service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.phincon.backend.bootcamp.marketplace.balance_service.model.Balance;

@Repository
public interface BalanceRepository extends R2dbcRepository<Balance, Long> {

}
