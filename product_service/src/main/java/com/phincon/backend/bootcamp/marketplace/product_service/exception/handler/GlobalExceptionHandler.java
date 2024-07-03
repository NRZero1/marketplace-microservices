package com.phincon.backend.bootcamp.marketplace.product_service.exception.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.phincon.backend.bootcamp.marketplace.dto.ErrorResponse;
import com.phincon.backend.bootcamp.marketplace.product_service.exception.ProductNotFoundException;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        Mono<ErrorResponse> errResponse = Mono.just(
                new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(), ex.getMessage()));
        return errResponse;
    }
}