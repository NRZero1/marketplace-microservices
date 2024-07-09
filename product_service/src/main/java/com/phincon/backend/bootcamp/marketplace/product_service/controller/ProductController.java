package com.phincon.backend.bootcamp.marketplace.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.phincon.backend.bootcamp.marketplace.dto.request.ProductRequest;
import com.phincon.backend.bootcamp.marketplace.dto.response.ProductStockResponse;
import com.phincon.backend.bootcamp.marketplace.product_service.model.Product;
import com.phincon.backend.bootcamp.marketplace.product_service.service.ProductService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Product> getAll() {
        return productService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> create(@RequestBody @Valid ProductRequest product) {
        return productService.save(product);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductStockResponse> getById(@PathVariable long id) {
        return productService.getById(id);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Product> getByName(@RequestParam("name") String name) {
        return productService.getByName(name);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Product> update(@PathVariable long id, @RequestBody ProductRequest productRequest) {
        return productService.update(id, productRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable long id) {
        return productService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAll() {
        return productService.deleteAll();
    }

    @PatchMapping("/{id}/deduct")
    public Mono<ResponseEntity<ProductStockResponse>> deduct(@PathVariable long id,
            @RequestParam(name = "amount") int amount) {
        log.trace("Entering deduct controller");
        return productService.deduct(id, amount);
    }

    @PatchMapping("/{id}/addStock")
    public Mono<Product> addStock(@PathVariable long id, @RequestParam(name = "amount") int amount) {
        return productService.addStock(id, amount);
    }
}
