package com.ecommerce.infrastructure.adapters.repositories.entities;

import com.ecommerce.domain.entities.OrderItem;
import com.ecommerce.domain.valueobjects.Money;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidade JPA para persistência de itens de pedido.
 */
@Entity
@Table(name = "order_items")
public class OrderItemJpaEntity {

    @Id
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "unit_price_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceAmount;

    @Column(name = "unit_price_currency", nullable = false, length = 3)
    private String unitPriceCurrency;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "total_price_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPriceAmount;

    @Column(name = "total_price_currency", nullable = false, length = 3)
    private String totalPriceCurrency;

    // Construtor padrão para JPA
    protected OrderItemJpaEntity() {}

    // Construtor completo
    public OrderItemJpaEntity(UUID id, UUID orderId, UUID productId, String productName,
                              BigDecimal unitPriceAmount, String unitPriceCurrency,
                              Integer quantity, BigDecimal totalPriceAmount, String totalPriceCurrency) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.unitPriceAmount = unitPriceAmount;
        this.unitPriceCurrency = unitPriceCurrency;
        this.quantity = quantity;
        this.totalPriceAmount = totalPriceAmount;
        this.totalPriceCurrency = totalPriceCurrency;
    }

    // Factory method para converter de Domain Entity
    public static OrderItemJpaEntity fromDomain(OrderItem orderItem, UUID orderId) {
        return new OrderItemJpaEntity(
                orderItem.getId(),
                orderId,
                orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getUnitPrice().getAmount(),
                orderItem.getUnitPrice().getCurrency(),
                orderItem.getQuantity(),
                orderItem.getTotalPrice().getAmount(),
                orderItem.getTotalPrice().getCurrency()
        );
    }

    // Método para converter para Domain Entity
    public OrderItem toDomain() {
        Money unitPrice = Money.of(unitPriceAmount, unitPriceCurrency);
        Money totalPrice = Money.of(totalPriceAmount, totalPriceCurrency);

        return OrderItem.reconstruct(
                id, productId, productName, unitPrice, quantity, totalPrice
        );
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getUnitPriceAmount() { return unitPriceAmount; }
    public void setUnitPriceAmount(BigDecimal unitPriceAmount) { this.unitPriceAmount = unitPriceAmount; }

    public String getUnitPriceCurrency() { return unitPriceCurrency; }
    public void setUnitPriceCurrency(String unitPriceCurrency) { this.unitPriceCurrency = unitPriceCurrency; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getTotalPriceAmount() { return totalPriceAmount; }
    public void setTotalPriceAmount(BigDecimal totalPriceAmount) { this.totalPriceAmount = totalPriceAmount; }

    public String getTotalPriceCurrency() { return totalPriceCurrency; }
    public void setTotalPriceCurrency(String totalPriceCurrency) { this.totalPriceCurrency = totalPriceCurrency; }
}