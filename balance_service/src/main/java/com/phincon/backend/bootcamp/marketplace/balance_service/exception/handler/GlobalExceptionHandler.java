package com.phincon.backend.bootcamp.marketplace.balance_service.exception.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.phincon.backend.bootcamp.marketplace.balance_service.exception.BalanceNotFoundException;
import com.phincon.backend.bootcamp.marketplace.balance_service.exception.TransactionNotFoundException;
import com.phincon.backend.bootcamp.marketplace.dto.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BalanceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handleBalanceNotFoundException(BalanceNotFoundException ex) {
        Mono<ErrorResponse> errResponse = Mono.just(
                new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(), ex.getMessage()));
        return errResponse;
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        Mono<ErrorResponse> errResponse = Mono.just(
                new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(), ex.getMessage()));
        return errResponse;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(WebExchangeBindException ex) {
        log.trace("Entering handleValidationExceptions()");
        Map<String, String> errors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage(),
                        (existingValue, newValue) -> existingValue,
                        HashMap::new));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("An unexpected error occurred: ", ex);
        return Mono.just(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred"));
    }
}
