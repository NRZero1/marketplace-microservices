package com.phincon.backend.bootcamp.marketplace.balance_service.exception;

public class BalanceNotFoundException extends Exception {
    public BalanceNotFoundException(String message) {
        super(message);
    }

    public BalanceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BalanceNotFoundException(Throwable cause) {
        super(cause);
    }
}
