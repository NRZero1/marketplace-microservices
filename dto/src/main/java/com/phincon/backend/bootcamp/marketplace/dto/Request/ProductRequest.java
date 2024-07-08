package com.phincon.backend.bootcamp.marketplace.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Name cannot be null or blank")
    private String name;

    @Min(1000)
    @NotNull(message = "Price cannot be null")
    private double price;

    @NotEmpty(message = "Category cannot be empty")
    private List<String> category;

    private String description;

    @NotBlank(message = "Image url cannot be null or blank")
    private String imageUrl;

    @Min(value = 1, message = "Stock minimum 1")
    private int stockQuantity;
}
