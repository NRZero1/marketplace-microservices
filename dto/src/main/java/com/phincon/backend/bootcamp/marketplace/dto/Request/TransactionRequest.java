package com.phincon.backend.bootcamp.marketplace.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private double amount;
    private long orderId;
    private String mode;
    private String status;
    private String referenceNumber;
}
