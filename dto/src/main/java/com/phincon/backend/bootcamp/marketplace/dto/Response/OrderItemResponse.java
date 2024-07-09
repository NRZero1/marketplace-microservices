package com.phincon.backend.bootcamp.marketplace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {
    private long id;

    private double price;

    private long productId;

    private int quantity;

    private long orderId;
}
