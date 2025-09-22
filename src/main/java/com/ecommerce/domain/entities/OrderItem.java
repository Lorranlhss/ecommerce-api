package com.ecommerce.domain.entities;

import com.ecommerce.domain.valueobjects.Money;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade que representa um item dentro de um pedido.
 * Contém produto, quantidade e preço no momento da compra.
 */
public class OrderItem {

    private final UUID id;
    private final UUID productId;
    private final String productName;
    private final Money unitPrice;
    private final Integer quantity;
    private final Money totalPrice;

    private OrderItem(UUID productId, String productName, Money unitPrice, Integer quantity) {
        this.id = UUID.randomUUID();

        validateOrderItem(productId, productName, unitPrice, quantity);

        this.productId = productId;
        this.productName = productName.trim();
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = unitPrice.multiply(quantity);
    }

    // Construtor para reconstrução
    private OrderItem(UUID id, UUID productId, String productName, Money unitPrice,
                      Integer quantity, Money totalPrice) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Factory methods
    public static OrderItem create(UUID productId, String productName,
                                   Money unitPrice, Integer quantity) {
        return new OrderItem(productId, productName, unitPrice, quantity);
    }

    public static OrderItem createFromProduct(Product product, Integer quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!product.isAvailable()) {
            throw new IllegalStateException("Product is not available: " + product.getName());
        }
        if (!product.hasStock(quantity)) {
            throw new IllegalStateException("Insufficient stock for product: " + product.getName());
        }

        return new OrderItem(product.getId(), product.getName(), product.getPrice(), quantity);
    }

    public static OrderItem reconstruct(UUID id, UUID productId, String productName,
                                        Money unitPrice, Integer quantity, Money totalPrice) {
        return new OrderItem(id, productId, productName, unitPrice, quantity, totalPrice);
    }

    // Métodos de negócio
    public boolean isForProduct(UUID productId) {
        return this.productId.equals(productId);
    }

    public Money calculateSubtotal() {
        return unitPrice.multiply(quantity);
    }

    private void validateOrderItem(UUID productId, String productName, Money unitPrice, Integer quantity) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (unitPrice == null || unitPrice.isNegative()) {
            throw new IllegalArgumentException("Unit price must be positive");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Money getUnitPrice() { return unitPrice; }
    public Integer getQuantity() { return quantity; }
    public Money getTotalPrice() { return totalPrice; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        OrderItem orderItem = (OrderItem) obj;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("OrderItem{id=%s, product='%s', quantity=%d, unitPrice=%s, total=%s}",
                id, productName, quantity, unitPrice, totalPrice);
    }
}