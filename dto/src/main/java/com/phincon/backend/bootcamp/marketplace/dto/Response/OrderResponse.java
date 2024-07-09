package com.phincon.backend.bootcamp.marketplace.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private long id;

    private String billingAddress;

    private long customerId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    private String orderStatus;

    private String paymentMethod;

    private String shippingAddress;

    private List<OrderItemResponse> orderItem;

    private double totalAmount;
}
