package com.phincon.backend.bootcamp.marketplace.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.phincon.backend.bootcamp.marketplace.dto.Request.ProductRequest;
import com.phincon.backend.bootcamp.marketplace.dto.Response.ProductStockResponse;
import com.phincon.backend.bootcamp.marketplace.product_service.model.Product;
import com.phincon.backend.bootcamp.marketplace.product_service.service.ProductService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
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
    public Mono<Product> getById(@PathVariable long id) {
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
    public Mono<?> deduct(@PathVariable long id, @RequestParam(name = "amount") int amount) {
        return productService.deduct(id, amount);
    }
}
