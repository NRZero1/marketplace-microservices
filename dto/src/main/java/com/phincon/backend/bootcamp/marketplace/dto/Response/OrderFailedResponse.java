package com.phincon.backend.bootcamp.marketplace.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFailedResponse {
    private long id;

    private String billingAddress;

    private long customerId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    private String orderStatus;

    private String orderFailedMessage;

    private String paymentMethod;

    private String shippingAddress;

    private List<OrderItemResponse> orderItem;

    private double totalAmount;
}
