package com.phincon.backend.bootcamp.marketplace.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceRequest {
    @Min(value = 10000, message = "Min balance is 10000")
    private double balance;
}
