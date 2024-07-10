package com.phincon.backend.bootcamp.marketplace.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    private double price;

    @Positive(message = "order id must be a positive number")
    private long productId;

    @Min(value = 1, message = "Quantity order min is 1")
    @NotNull(message = "Quantity cannot be null")
    private int quantity;

    private long orderId;
}
