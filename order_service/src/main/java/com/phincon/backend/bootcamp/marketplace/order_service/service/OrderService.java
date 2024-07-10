package com.phincon.backend.bootcamp.marketplace.order_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.phincon.backend.bootcamp.marketplace.dto.request.OrderItemRequest;
import com.phincon.backend.bootcamp.marketplace.dto.request.OrderRequest;
import com.phincon.backend.bootcamp.marketplace.dto.response.OrderItemResponse;
import com.phincon.backend.bootcamp.marketplace.dto.response.OrderResponse;
import com.phincon.backend.bootcamp.marketplace.order_service.repository.OrderRepository;
import com.phincon.backend.bootcamp.marketplace.service_enums.OrderStatus;

import lombok.extern.slf4j.Slf4j;

import com.phincon.backend.bootcamp.marketplace.order_service.exception.OrderNotFoundException;
import com.phincon.backend.bootcamp.marketplace.order_service.model.Order;
import com.phincon.backend.bootcamp.marketplace.order_service.model.OrderItem;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private KafkaTemplate<String, OrderRequest> template;

    public Flux<Order> getAll() {
        return orderRepository.findAll();
    }

    public Mono<Order> getById(long id) {
        return orderRepository.findById(id).switchIfEmpty(
                Mono.error(
                        new OrderNotFoundException(
                                String.format(
                                        "Order not found with ID: ", id))));
    }

    public Mono<OrderResponse> save(OrderRequest request) {
        log.debug("Order items: {}", request.getOrderItem());
        List<OrderItemResponse> orderItemResponse = new ArrayList<>();
        return orderRepository.save(mapToOrder(request))
                .flatMap(savedOrder -> {
                    log.info("Order saved with data: {}", savedOrder);

                    request.getOrderItem().forEach(orderItem -> {
                        log.info("Order item to be saved: {}", orderItem);
                    });

                    return Flux.fromIterable(request.getOrderItem())
                            .flatMap(itemRequest -> {
                                itemRequest.setOrderId(savedOrder.getId());
                                log.info("Saving order item: {}", itemRequest);
                                return orderItemService.save(itemRequest)
                                        .doOnSuccess(savedItem -> {
                                            log.info("Order item saved: {}", savedItem);
                                            orderItemResponse.add(mapToOrderItemResponse(savedItem));
                                        });
                            })
                            .collectList()
                            .map(savedItems -> {
                                return mapToOrderResponse(savedOrder, orderItemResponse);
                            });

                })
                .doOnNext((response) -> {
                    log.info("Response get: {}", response);
                    Message<OrderResponse> message = MessageBuilder
                            .withPayload(response)
                            .setHeader(KafkaHeaders.TOPIC, "order-service")
                            .build();

                    template.send(message);
                });
    }

    public Mono<Order> update(long id, OrderRequest request) {
        Order order = new Order();
        return orderRepository.findById(id)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalBalance -> {
                    if (optionalBalance.isPresent()) {
                        order.setId(id);
                        orderRepository.save(order);
                    }

                    return Mono.empty();
                });
    }

    @KafkaListener(topics = "order-update-status", groupId = "order-status")
    public Mono<OrderResponse> updateStatus(OrderResponse orderResponse) {
        return orderRepository.findById(orderResponse.getId())
                .switchIfEmpty(Mono.error(new OrderNotFoundException(String.format(
                        "Order not found with ID: %d", orderResponse.getId()))))
                .flatMap(order -> {
                    order.setOrderStatus(orderResponse.getOrderStatus());
                    order.setTotalAmount(orderResponse.getTotalAmount());
                    return orderRepository.save(order)
                            .flatMap(savedOrder -> orderItemService.findByOrderId(orderResponse.getId())
                                    .collectList()
                                    .map(orderItems -> {
                                        List<OrderItemResponse> orderItemResponses = orderItems.stream()
                                                .map(this::mapToOrderItemResponse)
                                                .collect(Collectors.toList());
                                        return mapToOrderResponse(savedOrder, orderItemResponses);
                                    }));
                });
    }

    public Mono<Void> delete(long id) {
        return orderRepository.deleteById(id)
                .doOnNext((response) -> {
                    orderItemService.deleteByOrderId(id);
                });
    }

    public Mono<Void> deleteAll() {
        orderItemService.deleteAll();
        return orderRepository.deleteAll();
    }

    public Order mapToOrder(OrderRequest request) {
        return Order
                .builder()
                .billingAddress(request.getBillingAddress())
                .customerId(request.getCustomerId())
                .orderStatus(OrderStatus.CREATED.name())
                .orderDate(LocalDateTime.now())
                .paymentMethod(request.getPaymentMethod())
                .shippingAddress(request.getShippingAddress())
                .totalAmount(request.getTotalAmount())
                .build();
    }

    public OrderItem mapToOrderItem(OrderItemRequest request) {
        return OrderItem
                .builder()
                .price(request.getPrice())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .orderId(request.getOrderId())
                .build();
    }

    public OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse
                .builder()
                .id(orderItem.getId())
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .orderId(orderItem.getOrderId())
                .build();
    }

    public OrderResponse mapToOrderResponse(Order order, List<OrderItemResponse> orderItem) {
        return OrderResponse
                .builder()
                .id(order.getId())
                .billingAddress(order.getBillingAddress())
                .customerId(order.getCustomerId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .shippingAddress(order.getShippingAddress())
                .orderItem(orderItem)
                .totalAmount(order.getTotalAmount())
                .build();
    }
}
