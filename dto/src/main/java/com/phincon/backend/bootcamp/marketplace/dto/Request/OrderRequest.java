package com.phincon.backend.bootcamp.marketplace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotBlank(message = "Billing Address cannot be null or blank")
    private String billingAddress;

    private long customerId;

    private String orderStatus;

    @NotBlank(message = "Payment method cannot be null or blank")
    private String paymentMethod;

    @NotBlank(message = "Shipping address cannot be null or blank")
    private String shippingAddress;

    @Valid
    private List<OrderItemRequest> orderItem;

    private double totalAmount;
}
