package com.phincon.backend.bootcamp.marketplace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionUpdateStatusRequest {
    private long customerId;
    private double amount;
    private long orderId;
    private String mode;
    private String status;
    private String referenceNumber;
}
