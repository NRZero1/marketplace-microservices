package com.phincon.backend.bootcamp.marketplace.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotBlank(message = "Billing Address cannot be null or blank")
    private String billingAddress;

    @NotEmpty(message = "Customer id cannot be empty")
    private long customerId;

    @NotBlank(message = "Order Status must be either created/completed/failed")
    private String orderStatus;

    @NotBlank(message = "Payment method cannot be null or blank")
    private String paymentMethod;

    @NotBlank(message = "Shipping address cannot be null or blank")
    private String shippingAddress;

    @Valid
    private List<OrderItemRequest> orderItem;

    private double totalAmount;
}
