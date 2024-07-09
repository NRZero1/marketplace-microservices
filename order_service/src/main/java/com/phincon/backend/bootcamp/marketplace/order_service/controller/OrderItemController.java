package com.phincon.backend.bootcamp.marketplace.order_service.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.phincon.backend.bootcamp.marketplace.dto.request.OrderItemRequest;
import com.phincon.backend.bootcamp.marketplace.order_service.model.OrderItem;
import com.phincon.backend.bootcamp.marketplace.order_service.service.OrderItemService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/order-item")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<OrderItem> getAll() {
        return orderItemService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OrderItem> create(@RequestBody @Valid OrderItem orderItem) {
        return orderItemService.save(orderItem);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<OrderItem> getById(@PathVariable long id) {
        return orderItemService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<OrderItem> update(@PathVariable long id, @RequestBody OrderItemRequest orderItemRequest) {
        return orderItemService.update(id, orderItemRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable long id) {
        return orderItemService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAll() {
        return orderItemService.deleteAll();
    }
}
