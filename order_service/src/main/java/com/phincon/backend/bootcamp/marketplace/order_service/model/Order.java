package com.phincon.backend.bootcamp.marketplace.order_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    private long id;

    private String billingAddress;

    @Column("customer_id")
    private long customerId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    @Column("order_status")
    private String orderStatus;

    @Column("payment_method")
    private String paymentMethod;

    @Column("shipping_address")
    private String shippingAddress;

    @Column("total_amount")
    private double totalAmount;
}
