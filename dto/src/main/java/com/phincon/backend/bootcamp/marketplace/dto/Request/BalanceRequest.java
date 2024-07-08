package com.phincon.backend.bootcamp.marketplace.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceRequest {
    private double balance;
}
