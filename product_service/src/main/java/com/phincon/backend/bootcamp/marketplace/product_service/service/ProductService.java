package com.phincon.backend.bootcamp.marketplace.product_service.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phincon.backend.bootcamp.marketplace.dto.ProductRequest;
import com.phincon.backend.bootcamp.marketplace.product_service.exception.ProductNotFoundException;
import com.phincon.backend.bootcamp.marketplace.product_service.model.Product;
import com.phincon.backend.bootcamp.marketplace.product_service.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
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
                                        "Product not found with ID: ", id))));
    }

    public Flux<Product> getByName(String name) {
        return productRepository.findByName(name)
                .switchIfEmpty(
                        Mono.error(
                                new ProductNotFoundException(
                                        String.format("Product not found with name: %s", name))));
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
                .build());
    }

    public Mono<Product> update(long id, ProductRequest request) {
        return productRepository.findById(id)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalProduct -> {
                    if (optionalProduct.isPresent()) {
                        return productRepository.save(
                                Product.builder()
                                        .id(id)
                                        .build());
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

    public Mono<Product> deduct(long id, int deducted, Product product) {
        return productRepository.findById(id)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalProduct -> {
                    if (optionalProduct.isPresent()) {
                        product.setStockQuantity(deducted);
                        return productRepository.save(product);
                    }
                    return Mono.empty();
                });
    }
}
