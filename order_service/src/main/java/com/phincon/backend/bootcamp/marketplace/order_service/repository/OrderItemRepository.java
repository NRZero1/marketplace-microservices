package com.phincon.backend.bootcamp.marketplace.order_service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.phincon.backend.bootcamp.marketplace.order_service.model.OrderItem;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Repository
public interface OrderItemRepository extends R2dbcRepository<OrderItem, Long> {
    @Query("DELETE FROM order_item WHERE order_id = :orderId")
    Mono<Void> deleteByOrderId(Long orderId);

    Flux<OrderItem> findByOrderId(long orderId);
}
