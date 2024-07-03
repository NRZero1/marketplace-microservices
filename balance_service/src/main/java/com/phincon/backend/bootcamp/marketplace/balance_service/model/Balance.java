package com.phincon.backend.bootcamp.marketplace.balance_service.model;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Balance {
    @Id
    private long id;
    private double balance;
}
