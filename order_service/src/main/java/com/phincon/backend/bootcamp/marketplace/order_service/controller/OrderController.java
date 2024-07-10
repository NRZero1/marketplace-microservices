package com.phincon.backend.bootcamp.marketplace.order_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.phincon.backend.bootcamp.marketplace.dto.request.OrderRequest;
import com.phincon.backend.bootcamp.marketplace.dto.response.OrderResponse;
import com.phincon.backend.bootcamp.marketplace.order_service.model.Order;
import com.phincon.backend.bootcamp.marketplace.order_service.service.OrderService;

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
@RequestMapping("/api/order")
public class OrderController {
        @Autowired
        private OrderService orderService;

        @Operation(summary = "Get All Orders")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Orders retrieved", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)) })
        })
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public Flux<Order> getAll() {
                return orderService.getAll();
        }

        @Operation(summary = "Create a new Order")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Order created", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class)) })
        })
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<OrderResponse> create(@RequestBody @Valid OrderRequest order) {
                return orderService.save(order);
        }

        @Operation(summary = "Get Order by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Order retrieved", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)) }),
                        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
        })
        @GetMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<Order> getById(@PathVariable @Positive(message = "Id must be a positive number") long id) {
                return orderService.getById(id);
        }

        @Operation(summary = "Update Order by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Order updated", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)) }),
                        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
        })
        @PutMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<Order> update(@PathVariable @Positive(message = "Id must be a positive number") long id,
                        @RequestBody OrderRequest orderRequest) {
                return orderService.update(id, orderRequest);
        }

        @Operation(summary = "Delete Order by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Order deleted", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
        })
        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public Mono<Void> delete(@PathVariable @Positive(message = "Id must be a positive number") long id) {
                return orderService.delete(id);
        }

        @Operation(summary = "Delete All Orders")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "All orders deleted", content = @Content)
        })
        @DeleteMapping
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public Mono<Void> deleteAll() {
                return orderService.deleteAll();
        }

        @Operation(summary = "Update Order Status by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Order status updated", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class)) }),
                        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
        })
        @PatchMapping("/{id}/updateStatus")
        @ResponseStatus(HttpStatus.OK)
        public Mono<OrderResponse> updateStatus(
                        @PathVariable @Positive(message = "Id must be a positive number") long id,
                        @RequestBody OrderResponse orderResponse) {
                return orderService.updateStatus(orderResponse);
        }
}
