package com.phincon.backend.bootcamp.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank
    private String name;

    @Min(1000)
    private double price;

    @NotBlank
    private List<String> category;

    private String description;

    private String imageUrl;

    @Min(1)
    private int stockQuantity;
}
