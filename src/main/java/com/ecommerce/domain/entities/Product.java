package com.ecommerce.domain.entities;

import com.ecommerce.domain.valueobjects.Money;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade que representa um produto no catálogo.
 * Rica em comportamentos e regras de negócio.
 */
public class Product {

    private final UUID id;
    private String name;
    private String description;
    private Money price;
    private Integer stockQuantity;
    private String category;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Construtor para criação de novos produtos
    private Product(String name, String description, Money price,
                    Integer stockQuantity, String category) {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;

        updateProductInfo(name, description, price, stockQuantity, category);
    }

    // Construtor para reconstrução (ex: vindo do banco de dados)
    private Product(UUID id, String name, String description, Money price,
                    Integer stockQuantity, String category, boolean active,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory methods
    public static Product create(String name, String description, Money price,
                                 Integer stockQuantity, String category) {
        return new Product(name, description, price, stockQuantity, category);
    }

    public static Product reconstruct(UUID id, String name, String description,
                                      Money price, Integer stockQuantity, String category,
                                      boolean active, LocalDateTime createdAt,
                                      LocalDateTime updatedAt) {
        return new Product(id, name, description, price, stockQuantity,
                category, active, createdAt, updatedAt);
    }

    // Métodos de negócio
    public void updateProductInfo(String name, String description, Money price,
                                  Integer stockQuantity, String category) {
        validateProductData(name, description, price, stockQuantity, category);

        this.name = name.trim();
        this.description = description.trim();
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category.trim();
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePrice(Money newPrice) {
        if (newPrice == null || newPrice.isNegative()) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.price = newPrice;
        this.updatedAt = LocalDateTime.now();
    }

    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to add must be positive");
        }
        this.stockQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void removeStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to remove must be positive");
        }
        if (this.stockQuantity < quantity) {
            throw new IllegalStateException("Insufficient stock. Available: " + this.stockQuantity);
        }
        this.stockQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAvailable() {
        return active && stockQuantity > 0;
    }

    public boolean hasStock(int requestedQuantity) {
        return stockQuantity >= requestedQuantity;
    }

    public Money calculateTotalPrice(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        return price.multiply(quantity);
    }

    private void validateProductData(String name, String description, Money price,
                                     Integer stockQuantity, String category) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }
        if (price == null || price.isNegative()) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        if (stockQuantity == null || stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Product category cannot be null or empty");
        }
    }

    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Money getPrice() { return price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public String getCategory() { return category; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Product product = (Product) obj;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Product{id=%s, name='%s', price=%s, stock=%d, active=%s}",
                id, name, price, stockQuantity, active);
    }
}