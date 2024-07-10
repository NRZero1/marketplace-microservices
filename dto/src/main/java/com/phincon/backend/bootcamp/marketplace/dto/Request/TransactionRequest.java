package com.phincon.backend.bootcamp.marketplace.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {
    @Positive(message = "customer id must be a positive integer")
    private long customerId;

    @Positive(message = "amount must be a positive integer")
    private double amount;

    @Positive(message = "order id must be a positive integer")
    private long orderId;

    @NotBlank(message = "mode cannot be null or blank")
    private String mode;
    private String status;
    private String referenceNumber;
}
