package com.phincon.backend.bootcamp.marketplace.balance_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.phincon.backend.bootcamp.marketplace.balance_service.model.Transaction;
import com.phincon.backend.bootcamp.marketplace.balance_service.service.TransactionService;
import com.phincon.backend.bootcamp.marketplace.dto.request.TransactionRequest;
import com.phincon.backend.bootcamp.marketplace.dto.response.TransactionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/api/transaction")
public class TransactionController {
        @Autowired
        private TransactionService transactionService;

        @Operation(summary = "Get All Transactions")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Transactions retrieved", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class)) })
        })
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public Flux<Transaction> getAll() {
                return transactionService.getAll();
        }

        @Operation(summary = "Create a new Transaction")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Transaction created", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class)) })
        })
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<TransactionResponse> create(@RequestBody @Valid TransactionRequest transactionRequest) {
                return transactionService.save(transactionRequest);
        }

        @Operation(summary = "Get Transaction by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Transaction retrieved", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class)) }),
                        @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content)
        })
        @GetMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<Transaction> getById(@PathVariable @Positive(message = "Id must be a positive number") long id) {
                return transactionService.getById(id);
        }

        @Operation(summary = "Update Transaction by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Transaction updated", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class)) }),
                        @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content)
        })
        @PutMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<Transaction> update(@PathVariable @Positive(message = "Id must be a positive number") long id,
                        @RequestBody TransactionResponse transactionResponse) {
                log.trace("Entering put request update");
                return transactionService.update(id, transactionResponse);
        }

        @Operation(summary = "Delete Transaction by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Transaction deleted", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content)
        })
        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public Mono<Void> delete(@PathVariable @Positive(message = "Id must be a positive number") long id) {
                return transactionService.delete(id);
        }

        @Operation(summary = "Delete All Transactions")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "All transactions deleted", content = @Content)
        })
        @DeleteMapping
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public Mono<Void> deleteAll() {
                return transactionService.deleteAll();
        }
}
