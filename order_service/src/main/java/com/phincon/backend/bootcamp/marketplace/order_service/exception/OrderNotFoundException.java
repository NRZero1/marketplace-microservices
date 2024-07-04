package com.phincon.backend.bootcamp.marketplace.order_service.exception;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotFoundException(Throwable cause) {
        super(cause);
    }
}
