package com.phincon.backend.bootcamp.marketplace.balance_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.phincon.backend.bootcamp.marketplace.balance_service.model.Balance;
import com.phincon.backend.bootcamp.marketplace.balance_service.service.BalanceService;
import com.phincon.backend.bootcamp.marketplace.dto.request.BalanceRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

@RestController
@RequestMapping("/api/balance")
public class BalanceController {
        @Autowired
        private BalanceService balanceService;

        @Operation(summary = "Get All Balances")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Balances retrieved", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Balance.class)) })
        })
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public Flux<Balance> getAll() {
                return balanceService.getAll();
        }

        @Operation(summary = "Create a new Balance")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Balance created", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Balance.class)) })
        })
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<Balance> create(@RequestBody @Valid BalanceRequest balanceRequest) {
                return balanceService.save(balanceRequest);
        }

        @Operation(summary = "Get Balance by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Balance retrieved", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Balance.class)) }),
                        @ApiResponse(responseCode = "404", description = "Balance not found", content = @Content)
        })
        @GetMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<Balance> getById(@PathVariable @Positive(message = "Id must be a positive number") long id) {
                return balanceService.getById(id);
        }

        @Operation(summary = "Update Balance by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Balance updated", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Balance.class)) }),
                        @ApiResponse(responseCode = "404", description = "Balance not found", content = @Content)
        })
        @PutMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<Balance> update(@PathVariable @Positive(message = "Id must be a positive number") long id,
                        @RequestBody @Valid BalanceRequest balanceRequest) {
                return balanceService.update(id, balanceRequest);
        }

        @Operation(summary = "Delete Balance by Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Balance deleted", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Balance not found", content = @Content)
        })
        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public Mono<Void> delete(@PathVariable @Positive(message = "Id must be a positive number") long id) {
                return balanceService.delete(id);
        }

        @Operation(summary = "Delete All Balances")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "All balances deleted", content = @Content)
        })
        @DeleteMapping
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public Mono<Void> deleteAll() {
                return balanceService.deleteAll();
        }
}
