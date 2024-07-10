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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Operation(summary = "Get All Products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product List", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)) })
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Product> getAll() {
        return productService.getAll();
    }

    @Operation(summary = "Create a New Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product Created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)) })
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> create(@RequestBody @Valid ProductRequest product) {
        return productService.save(product);
    }

    @Operation(summary = "Get Product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProductStockResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Product Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductStockResponse> getById(@PathVariable @Positive long id) {
        return productService.getById(id);
    }

    @Operation(summary = "Get Products by Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "404", description = "Products Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Product> getByName(@RequestParam("name") @NotBlank String name) {
        return productService.getByName(name);
    }

    @Operation(summary = "Update Product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product Updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "404", description = "Product Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Product> update(@PathVariable @Positive long id, @RequestBody @Valid ProductRequest productRequest) {
        return productService.update(id, productRequest);
    }

    @Operation(summary = "Delete Product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product Deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable @Positive long id) {
        return productService.delete(id);
    }

    @Operation(summary = "Delete All Products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All Products Deleted", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAll() {
        return productService.deleteAll();
    }

    @Operation(summary = "Deduct Product Stock by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock Deducted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProductStockResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Product Not Found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PatchMapping("/{id}/deduct")
    public Mono<ResponseEntity<ProductStockResponse>> deduct(@PathVariable @Positive long id,
            @RequestParam(name = "amount") @Positive int amount) {
        log.trace("Entering deduct controller");
        return productService.deduct(id, amount);
    }

    @Operation(summary = "Add Stock to Product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock Added", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "404", description = "Product Not Found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PatchMapping("/{id}/addStock")
    public Mono<Product> addStock(@PathVariable @Positive long id,
            @RequestParam(name = "amount") @Positive int amount) {
        return productService.addStock(id, amount);
    }
}
