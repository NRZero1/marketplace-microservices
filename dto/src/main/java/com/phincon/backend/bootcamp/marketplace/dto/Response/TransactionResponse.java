package com.phincon.backend.bootcamp.marketplace.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private long customerId;
    private double amount;
    private long orderId;
    private String mode;
    private String status;
    private String referenceNumber;
}
