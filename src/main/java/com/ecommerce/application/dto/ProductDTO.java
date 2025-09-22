package com.ecommerce.application.dto;

import com.ecommerce.domain.entities.Product;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de resposta para produtos.
 */
public record ProductDTO(
        UUID id,
        String name,
        String description,
        MoneyDTO price,
        Integer stockQuantity,
        String category,
        boolean active,
        boolean available,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ProductDTO from(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                MoneyDTO.from(product.getPrice()),
                product.getStockQuantity(),
                product.getCategory(),
                product.isActive(),
                product.isAvailable(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}