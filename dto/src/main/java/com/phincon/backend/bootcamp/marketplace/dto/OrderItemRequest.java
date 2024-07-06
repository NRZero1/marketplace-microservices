package com.phincon.backend.bootcamp.marketplace.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    private double price;

    @NotEmpty(message = "Product id cannot be null or empty")
    private long productId;

    @Min(value = 1, message = "Quantity order min is 1")
    @NotNull(message = "Quantity cannot be null")
    private int quantity;

    @NotEmpty(message = "Order id cannot be null or empty")
    private long orderId;
}
