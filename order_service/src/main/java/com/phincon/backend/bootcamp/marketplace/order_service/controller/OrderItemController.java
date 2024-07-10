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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/order-item")
public class OrderItemController {
        @Autowired
        private OrderItemService orderItemService;

        @Operation(summary = "Get All Order Items")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Order items retrieved", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class)) })
        })
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public Flux<OrderItem> getAll() {
                return orderItemService.getAll();
        }

        @Operation(summary = "Create a new Order Item")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Order item created", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class)) })
        })
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<OrderItem> create(@RequestBody @Valid OrderItemRequest orderItemRequest) {
                return orderItemService.save(orderItemRequest);
        }

        @Operation(summary = "Get Order Item by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Order item retrieved", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class)) }),
                        @ApiResponse(responseCode = "404", description = "Order item not found", content = @Content)
        })
        @GetMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<OrderItem> getById(@PathVariable @Positive(message = "Id must be a positive number") long id) {
                return orderItemService.getById(id);
        }

        @Operation(summary = "Update Order Item by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Order item updated", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class)) }),
                        @ApiResponse(responseCode = "404", description = "Order item not found", content = @Content)
        })
        @PutMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<OrderItem> update(@PathVariable @Positive(message = "Id must be a positive number") long id,
                        @RequestBody @Valid OrderItemRequest orderItemRequest) {
                return orderItemService.update(id, orderItemRequest);
        }

        @Operation(summary = "Delete Order Item by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Order item deleted", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Order item not found", content = @Content)
        })
        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public Mono<Void> delete(@PathVariable @Positive(message = "Id must be a positive number") long id) {
                return orderItemService.delete(id);
        }

        @Operation(summary = "Delete All Order Items")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "All order items deleted", content = @Content)
        })
        @DeleteMapping
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public Mono<Void> deleteAll() {
                return orderItemService.deleteAll();
        }
}
