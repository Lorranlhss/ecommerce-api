package com.ecommerce.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 * DTO para criação de produtos.
 */
public record CreateProductDTO(
        @NotBlank(message = "Product name is required")
        String name,

        @NotBlank(message = "Product description is required")
        String description,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @NotNull(message = "Stock quantity is required")
        @PositiveOrZero(message = "Stock quantity cannot be negative")
        Integer stockQuantity,

        @NotBlank(message = "Category is required")
        String category,

        String currency
) {

    // Construtor que assume BRL como moeda padrão
    public CreateProductDTO(String name, String description, BigDecimal price,
                            Integer stockQuantity, String category) {
        this(name, description, price, stockQuantity, category, "BRL");
    }

    public String currency() {
        return currency != null ? currency : "BRL";
    }
}