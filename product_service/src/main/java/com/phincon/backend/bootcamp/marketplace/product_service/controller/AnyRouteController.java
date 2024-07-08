package com.phincon.backend.bootcamp.marketplace.product_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.phincon.backend.bootcamp.marketplace.dto.Response.ErrorResponse;
import com.phincon.backend.bootcamp.marketplace.product_service.exception.InvalidUrlException;

import reactor.core.publisher.Mono;

@RestController
@Order(Ordered.LOWEST_PRECEDENCE)
public class AnyRouteController {
    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public Mono<ErrorResponse> requestMethodName(@RequestParam String param) {
        return Mono.error(
                new InvalidUrlException(
                        String.format("Invalid Url: %s", param)));
    }
}
