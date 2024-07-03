package com.phincon.backend.bootcamp.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Cannot be blank")
    private String name;

    @Min(1000)
    @NotBlank
    @NotEmpty
    private double price;

    @NotBlank
    private List<String> category;

    private String description;

    private String imageUrl;

    @Min(1)
    private int stockQuantity;
}
