package com.phincon.backend.bootcamp.marketplace.product_service.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.phincon.backend.bootcamp.marketplace.dto.Request.ProductRequest;
import com.phincon.backend.bootcamp.marketplace.dto.Response.ProductStockResponse;
import com.phincon.backend.bootcamp.marketplace.product_service.exception.ProductNotFoundException;
import com.phincon.backend.bootcamp.marketplace.product_service.model.Product;
import com.phincon.backend.bootcamp.marketplace.product_service.repository.ProductRepository;
import com.phincon.backend.bootcamp.marketplace.service_enums.ProductStockStatus;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Flux<Product> getAll() {
        return productRepository.findAll();
    }

    public Mono<Product> getById(long id) {
        return productRepository.findById(id).switchIfEmpty(
                Mono.error(
                        new ProductNotFoundException(
                                String.format(
                                        "Product not found with id: %d", id))));
    }

    public Flux<Product> getByName(String name) {
        return productRepository.findByName(name)
                .switchIfEmpty(
                        Mono.error(
                                new ProductNotFoundException(
                                        String.format(
                                                "Product not found with name: %s", name))));
    }

    public Mono<Product> save(ProductRequest request) {
        return productRepository.save(Product
                .builder()
                .name(request.getName())
                .price(request.getPrice())
                .category(request.getCategory())
                .createdAt(LocalDateTime.now())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .stockQuantity(request.getStockQuantity())
                .updatedAt(LocalDateTime.now())
                .build())
                .doOnSuccess((response) -> {
                    log.info("Product is saved with data: {}", response.toString());
                });
    }

    public Mono<Product> update(long id, ProductRequest request) {
        return productRepository.findById(id)
                .map(Optional::of)
                .switchIfEmpty(
                        Mono.error(new ProductNotFoundException(String.format("Product not found with id: %d", id))))
                .flatMap(optionalProduct -> {
                    if (optionalProduct.isPresent()) {
                        Product product = new Product();
                        product.setId(id);
                        return productRepository.save(product)
                                .doOnSuccess((response) -> {
                                    log.info("Product is updated, current product data with id {} is {}", id,
                                            response.toString());
                                });
                    }

                    return Mono.empty();
                });
    }

    public Mono<Void> delete(long id) {
        return productRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return productRepository.deleteAll();
    }

    public Mono<ResponseEntity<ProductStockResponse>> deduct(long id, int deducted) {
        return productRepository.findById(id)
                .map(Optional::of)
                .switchIfEmpty(
                        Mono.error(new ProductNotFoundException(String.format("Product not found with id: %d", id))))
                .flatMap(optionalProduct -> {
                    if (optionalProduct.isPresent()) {
                        Product product = optionalProduct.get();
                        int currentStock = product.getStockQuantity();
                        log.debug("Current product with Id {} stock is {}", id, product.getStockQuantity());
                        log.debug("Deducted parameter is set as: {}", deducted);

                        if (currentStock < deducted) {
                            log.warn("Product with id {} not having enough stock", id);
                            return Mono.just(ResponseEntity
                                    .ok(mapToProductStockResponse(product,
                                            ProductStockStatus.OUTOFSTOCK.name(),
                                            deducted)));
                        }

                        int deductedStock = currentStock - deducted;
                        log.debug("Stock quantity after calculation: {}", deductedStock);
                        product.setStockQuantity(deductedStock);
                        return productRepository.save(product)
                                .map((savedProduct) -> {
                                    return ResponseEntity
                                            .ok(mapToProductStockResponse(savedProduct, ProductStockStatus.OK.name(),
                                                    deducted));
                                })
                                .doOnSuccess((response) -> {
                                    log.info("Product stock updated with product data now: {}", response.toString());
                                });
                    }
                    return Mono.empty();
                });
    }

    public Mono<Product> addStock(long id, int amount) {
        return productRepository.findById(id)
                .map(Optional::of)
                .switchIfEmpty(
                        Mono.error(new ProductNotFoundException(String.format("Product not found with id: %d", id))))
                .flatMap(optionalProduct -> {
                    Product product = optionalProduct.get();
                    product.setStockQuantity(product.getStockQuantity() + amount);
                    return productRepository.save(product);
                });
    }

    public ProductStockResponse mapToProductStockResponse(Product product, String status, int quantity) {
        return ProductStockResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .status(status)
                .totalPriceItem(product.getPrice() * quantity)
                .build();
    }
}
