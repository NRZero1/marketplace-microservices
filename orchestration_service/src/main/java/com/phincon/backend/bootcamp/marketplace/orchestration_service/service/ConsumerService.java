package com.phincon.backend.bootcamp.marketplace.orchestration_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.phincon.backend.bootcamp.marketplace.dto.request.OrderRequest;
import com.phincon.backend.bootcamp.marketplace.dto.request.TransactionRequest;
import com.phincon.backend.bootcamp.marketplace.dto.response.OrderItemResponse;
import com.phincon.backend.bootcamp.marketplace.dto.response.OrderResponse;
import com.phincon.backend.bootcamp.marketplace.dto.response.ProductStockResponse;
import com.phincon.backend.bootcamp.marketplace.dto.response.TransactionResponse;
import com.phincon.backend.bootcamp.marketplace.service_enums.OrderStatus;
import com.phincon.backend.bootcamp.marketplace.service_enums.ProductStockStatus;
import com.phincon.backend.bootcamp.marketplace.service_enums.TransactionStatus;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ConsumerService {
    // @KafkaListener(topics = "test", groupId = "test")
    // public void consumeMessage(String message) {
    // log.info("Received raw message: {}", message);
    // try {
    // ObjectMapper objectMapper = new ObjectMapper();
    // Test test = objectMapper.readValue(message, Test.class);
    // log.info("The content of the message is: {}", test.toString());
    // } catch (JsonProcessingException e) {
    // log.error("Error deserializing message", e);
    // }
    // }

    @Autowired
    ProductWebClient productWebClient;

    @Autowired
    TransactionWebClient transactionWebClient;

    @Autowired
    OrderWebClient orderWebClient;

    @Autowired
    private KafkaTemplate<String, OrderRequest> template;

    @KafkaListener(topics = "order-service", groupId = "orders")
    public void consumeMessage(OrderResponse orderResponse) {
        log.info("The content of the received message is: {}", orderResponse.toString());
        List<OrderItemResponse> listItems = orderResponse.getOrderItem();
        List<Mono<OrderItemResponse>> itemMonos = new ArrayList<>();

        for (OrderItemResponse item : listItems) {
            log.info(item.toString());
            Mono<OrderItemResponse> itemMono = productWebClient.deductProduct(item.getProductId(), item.getQuantity())
                    .flatMap(fetchedData -> {
                        log.trace("Entering flatmap deduct");
                        item.setPrice(fetchedData.getTotalPriceItem());
                        log.debug("Item price is set as: {}", item.getPrice());
                        orderResponse.setTotalAmount(orderResponse.getTotalAmount() + item.getPrice());
                        log.debug("Order total amount is set as: {}", orderResponse.getTotalAmount());
                        if (!fetchedData.getStatus().equals(ProductStockStatus.OK.name())) {
                            return Mono.error(new IllegalStateException("Product stock status is not OK"));
                        }
                        return Mono.just(item);
                    });
            itemMonos.add(itemMono);
        }

        Flux.merge(itemMonos)
                .collectList()
                .flatMap(items -> {
                    log.trace("Entering create transaction");
                    return transactionWebClient.create(mapTransactionRequest(orderResponse))
                            .log("Transaction created")
                            .flatMap(response -> transactionWebClient.checkBalance(orderResponse.getCustomerId())
                                    .flatMap(balanceResponse -> {
                                        if (orderResponse.getTotalAmount() > balanceResponse.getBalance()) {
                                            return transactionWebClient.updateBalance(orderResponse.getCustomerId(),
                                                    balanceResponse.getBalance() - orderResponse.getTotalAmount())
                                                    .flatMap(balanceUpdateResponse -> transactionWebClient
                                                            .updateTransactionStatus(response.getId(),
                                                                    mapToTransactionRequest(response)))
                                                    .flatMap(updateTransactionStatusResponse -> {
                                                        orderResponse.setOrderStatus(OrderStatus.COMPLETED.name());

                                                        Message<OrderResponse> message = MessageBuilder
                                                                .withPayload(orderResponse)
                                                                .setHeader(KafkaHeaders.TOPIC, "order-update-status")
                                                                .build();

                                                        return Mono.fromRunnable(() -> template.send(message));
                                                    });
                                        } else {
                                            return Mono.just(orderResponse);
                                        }
                                    }));
                })
                .doOnError(error -> {
                    listItems.forEach(item -> {
                        productWebClient.getProduct(item.getProductId())
                                .flatMap(response -> productWebClient.addStock(response.getId(),
                                        item.getQuantity() + response.getStockQuantity()))
                                .subscribe();
                    });
                    orderResponse.setOrderStatus(OrderStatus.FAILED.name());
                    Message<OrderResponse> message = MessageBuilder
                            .withPayload(orderResponse)
                            .setHeader(KafkaHeaders.TOPIC, "order-update-status")
                            .build();

                    template.send(message);
                })
                .subscribe();
    }

    public TransactionRequest mapTransactionRequest(OrderResponse orderResponse) {
        log.trace("Entering mapTransactionRequest");
        log.debug("Order Response Total Amount is: {}", orderResponse.getTotalAmount());
        return TransactionRequest
                .builder()
                .customerId(orderResponse.getCustomerId())
                .amount(orderResponse.getTotalAmount())
                .orderId(orderResponse.getId())
                .mode(orderResponse.getPaymentMethod())
                .status(TransactionStatus.PENDING.name())
                .referenceNumber(UUID.randomUUID().toString())
                .build();
    }

    public TransactionRequest mapToTransactionRequest(TransactionResponse transactionResponse) {
        return TransactionRequest
                .builder()
                .amount(transactionResponse.getAmount())
                .orderId(transactionResponse.getOrderId())
                .mode(transactionResponse.getMode())
                .status(transactionResponse.getStatus())
                .referenceNumber(transactionResponse.getReferenceNumber())
                .build();
    }
}
