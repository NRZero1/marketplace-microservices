package com.phincon.backend.bootcamp.marketplace.orchestration_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.phincon.backend.bootcamp.marketplace.dto.request.BalanceRequest;
import com.phincon.backend.bootcamp.marketplace.dto.request.OrderRequest;
import com.phincon.backend.bootcamp.marketplace.dto.request.TransactionRequest;
import com.phincon.backend.bootcamp.marketplace.dto.response.OrderItemResponse;
import com.phincon.backend.bootcamp.marketplace.dto.response.OrderResponse;
import com.phincon.backend.bootcamp.marketplace.dto.response.TransactionResponse;
import com.phincon.backend.bootcamp.marketplace.service_enums.OrderStatus;
import com.phincon.backend.bootcamp.marketplace.service_enums.ProductStockStatus;
import com.phincon.backend.bootcamp.marketplace.service_enums.TransactionStatus;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

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
		// This hashmap is for mapping product status, product id, order item quantity
		List<Map<String, String>> hashMaps = new ArrayList<Map<String, String>>();

		Flux<OrderItemResponse> orderItemsFlux = Flux.fromIterable(listItems)
				.flatMap(item -> {
					log.info(item.toString());
					return productWebClient.deductProduct(item.getProductId(), item.getQuantity())
							.flatMap(fetchedData -> {
								log.trace("Entering flatmap deduct");
								item.setPrice(fetchedData.getTotalPriceItem());
								log.debug("Item price is set as: {}", item.getPrice());
								orderResponse.setTotalAmount(orderResponse.getTotalAmount() + item.getPrice());
								log.debug("Order total amount is set as: {}", orderResponse.getTotalAmount());
								HashMap<String, String> hashMap = new HashMap<>();
								/*
								 * need to put this in hashmap because if putting it in a list the index of
								 * listItem and the variable containing it for storing status will be reversed
								 * for some reason
								 */
								hashMap.put("id", String.valueOf(fetchedData.getId()));
								hashMap.put("status", fetchedData.getStatus());
								hashMap.put("qty", String.valueOf(item.getQuantity()));
								hashMaps.add(hashMap);
								log.debug("Product status is added: {}", fetchedData.getStatus());
								return Mono.just(item);
							});
				});

		orderItemsFlux.collectList()
				.flatMap(orderItemResponses -> {
					hashMaps.stream()
							.forEach(item -> log.info("product status stream item is {}", item.get("status")));
					boolean allProductsInStock = hashMaps.stream()
							.allMatch(status -> status.get("status").equals(ProductStockStatus.OK.name()));

					if (allProductsInStock) {
						log.trace("Entering create transaction");
						return transactionWebClient.create(mapTransactionRequest(orderResponse))
								.flatMap(response -> transactionWebClient.checkBalance(orderResponse.getCustomerId())
										.flatMap(balanceResponse -> {
											log.trace("Inside flatmap checkBalance");
											log.debug("orderReponse total amount is set as {}",
													orderResponse.getTotalAmount());
											if (orderResponse.getTotalAmount() <= balanceResponse.getBalance()) {
												log.trace("After checking customer balance");
												BalanceRequest balanceRequest = new BalanceRequest();
												double amount = balanceResponse.getBalance()
														- orderResponse.getTotalAmount();
												balanceRequest.setBalance(amount);
												return transactionWebClient
														.updateBalance(orderResponse.getCustomerId(), balanceRequest)
														.flatMap(balanceUpdateResponse -> {
															log.trace("Inside flatmap update customer balance");
															response.setStatus(TransactionStatus.APPROVED.name());
															return transactionWebClient
																	.updateTransactionStatus(response);
														})
														.flatMap(updateTransactionStatusResponse -> {
															orderResponse.setOrderStatus(OrderStatus.COMPLETED.name());

															Message<OrderResponse> message = MessageBuilder
																	.withPayload(orderResponse)
																	.setHeader(KafkaHeaders.TOPIC,
																			"order-update-status")
																	.build();

															return Mono.fromRunnable(() -> template.send(message))
																	.then(Mono.just(orderResponse));
														});
											} else {
												response.setStatus(TransactionStatus.REJECTED.name());
												transactionWebClient.updateTransactionStatus(response).subscribe();
												IntStream.range(0, listItems.size())
														.forEach(index -> {
															log.debug("IntStream index value is {}", index);
															log.debug("Product status for index {} is {}", index,
																	hashMaps.get(index).get("status"));
															boolean productStatusIndex = (hashMaps.get(index)
																	.get("status")
																	.equals(ProductStockStatus.OK
																			.toString()));
															log.debug("productStatusIndex value is {}",
																	productStatusIndex);
															if (productStatusIndex) {
																log.trace("Entering checking product status");
																log.debug("current index value is {}", index);
																log.debug("Product Id is {}",
																		hashMaps.get(index).get("id"));
																productWebClient
																		.getProduct(
																				Long.parseLong(hashMaps.get(index).get(
																						"id")))
																		.flatMap(responseProduct -> {
																			log.trace("flatMap getProduct");
																			log.debug(
																					"Product with id {} current quantity is {}",
																					responseProduct.getId(),
																					responseProduct.getStockQuantity());
																			int newStockQuantity = Integer
																					.parseInt(hashMaps.get(index)
																							.get("qty"))
																					+ responseProduct
																							.getStockQuantity();
																			log.debug("newStockQuantity value is {}",
																					newStockQuantity);
																			return productWebClient.addStock(
																					responseProduct.getId(),
																					newStockQuantity);
																		})
																		.doOnError(error -> {
																			log.error(
																					"Error occurred while adding stock: ",
																					error);
																		})
																		.subscribe();
															}
														});
												orderResponse.setOrderStatus(OrderStatus.FAILED.name());
												Message<OrderResponse> message = MessageBuilder
														.withPayload(orderResponse)
														.setHeader(KafkaHeaders.TOPIC, "order-update-status")
														.build();

												template.send(message);
												return Mono.just(orderResponse);
											}
										}));
					} else {
						log.trace(
								"Entering condition if transaction is failed because balance is less than order amount");
						IntStream.range(0, listItems.size())
								.forEach(index -> {
									log.debug("IntStream index value is {}", index);
									log.debug("Product status for index {} is {}", index,
											hashMaps.get(index).get("status"));
									boolean productStatusIndex = (hashMaps.get(index).get("status")
											.equals(ProductStockStatus.OK
													.toString()));
									log.debug("productStatusIndex value is {}", productStatusIndex);
									if (productStatusIndex) {
										log.trace("Entering checking product status");
										log.debug("current index value is {}", index);
										log.debug("Product Id is {}", hashMaps.get(index).get("id"));
										productWebClient.getProduct(Long.parseLong(hashMaps.get(index).get(
												"id")))
												.flatMap(responseProduct -> {
													log.trace("flatMap getProduct");
													log.debug("Product with id {} current quantity is {}",
															responseProduct.getId(),
															responseProduct.getStockQuantity());
													int newStockQuantity = Integer.parseInt(hashMaps.get(index)
															.get("qty"))
															+ responseProduct.getStockQuantity();
													log.debug("newStockQuantity value is {}", newStockQuantity);
													return productWebClient.addStock(responseProduct.getId(),
															Integer.parseInt(hashMaps.get(index)
																	.get("qty")));
												})
												.doOnError(error -> {
													log.error("Error occurred while adding stock: ", error);
												})
												.subscribe();
									}
								});
						orderResponse.setOrderStatus(OrderStatus.FAILED.name());
						Message<OrderResponse> message = MessageBuilder
								.withPayload(orderResponse)
								.setHeader(KafkaHeaders.TOPIC, "order-update-status")
								.build();

						template.send(message);
						return Mono.just(orderResponse);
					}
				})
				.doOnError(error -> {
					IntStream.range(0, listItems.size())
							.forEach(index -> {
								log.trace("forEach error 1");
								if (hashMaps.get(index).get("status").equals(ProductStockStatus.OK.name())) {
									productWebClient.getProduct(Integer.parseInt(hashMaps.get(index).get(
											"id")))
											.flatMap(responseProduct -> {
												return productWebClient.addStock(responseProduct.getId(),
														listItems.get(index).getQuantity()
																+ responseProduct.getStockQuantity());
											}).subscribe();
								}
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
		log.trace("Entering mapToTransactionRequest");
		log.debug("Transaction Response data received with data {}", transactionResponse.toString());
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
