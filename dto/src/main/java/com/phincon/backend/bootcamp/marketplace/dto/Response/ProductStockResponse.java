package com.phincon.backend.bootcamp.marketplace.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStockResponse {
    private long id;
    private String name;
    private double price;
    private int stockQuantity;
    private String status;
}
