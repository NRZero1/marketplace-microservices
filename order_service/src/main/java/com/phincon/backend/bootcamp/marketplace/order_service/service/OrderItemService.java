package com.phincon.backend.bootcamp.marketplace.order_service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phincon.backend.bootcamp.marketplace.dto.OrderItemRequest;
import com.phincon.backend.bootcamp.marketplace.order_service.exception.OrderItemNotFoundException;
import com.phincon.backend.bootcamp.marketplace.order_service.model.OrderItem;
import com.phincon.backend.bootcamp.marketplace.order_service.repository.OrderItemRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    public Flux<OrderItem> getAll() {
        return orderItemRepository.findAll();
    }

    public Mono<OrderItem> getById(long id) {
        return orderItemRepository.findById(id).switchIfEmpty(
                Mono.error(
                        new OrderItemNotFoundException(
                                String.format(
                                        "Order Item not found with ID: ", id))));
    }

    public Mono<OrderItem> save(OrderItem orderItem) {

        return orderItemRepository.save(orderItem);
    }

    public Mono<OrderItem> update(long id, OrderItemRequest request) {
        OrderItem orderItem = new OrderItem();
        return orderItemRepository.findById(id)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalBalance -> {
                    if (optionalBalance.isPresent()) {
                        orderItem.setId(id);
                        orderItemRepository.save(orderItem);
                    }

                    return Mono.empty();
                });
    }

    public Mono<Void> delete(long id) {
        return orderItemRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return orderItemRepository.deleteAll();
    }

    public Mono<Void> deleteByOrderId(long id) {
        return orderItemRepository.deleteByOrderId(id);
    }
}
