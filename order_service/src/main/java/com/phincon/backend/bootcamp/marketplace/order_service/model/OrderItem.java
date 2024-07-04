package com.phincon.backend.bootcamp.marketplace.order_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("order_item")
public class OrderItem {
    @Id
    private long id;

    private double price;

    @Column("product_id")
    private long productid;

    private int quantity;

    @Column("order_id")
    private long orderId;
}
