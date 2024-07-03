package com.phincon.backend.bootcamp.marketplace.product_service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.phincon.backend.bootcamp.marketplace.product_service.model.Product;

import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Long> {
    public Flux<Product> findByName(String name);
}
