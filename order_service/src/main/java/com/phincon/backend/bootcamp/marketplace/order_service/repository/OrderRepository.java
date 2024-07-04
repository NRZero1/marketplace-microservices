package com.phincon.backend.bootcamp.marketplace.order_service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.phincon.backend.bootcamp.marketplace.order_service.model.Order;

@Repository
public interface OrderRepository extends R2dbcRepository<Order, Long> {

}
