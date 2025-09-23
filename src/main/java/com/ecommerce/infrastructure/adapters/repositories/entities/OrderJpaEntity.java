package com.ecommerce.infrastructure.adapters.repositories.entities;

import com.ecommerce.domain.entities.Order;
import com.ecommerce.domain.entities.OrderItem;
import com.ecommerce.domain.entities.OrderStatus;
import com.ecommerce.domain.valueobjects.Address;
import com.ecommerce.domain.valueobjects.Money;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidade JPA para persistência de pedidos.
 */
@Entity
@Table(name = "orders")
public class OrderJpaEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private List<OrderItemJpaEntity> items = new ArrayList<>();

    @Column(name = "delivery_address_street")
    private String deliveryAddressStreet;

    @Column(name = "delivery_address_number")
    private String deliveryAddressNumber;

    @Column(name = "delivery_address_complement")
    private String deliveryAddressComplement;

    @Column(name = "delivery_address_neighborhood")
    private String deliveryAddressNeighborhood;

    @Column(name = "delivery_address_city")
    private String deliveryAddressCity;

    @Column(name = "delivery_address_state")
    private String deliveryAddressState;

    @Column(name = "delivery_address_zip_code")
    private String deliveryAddressZipCode;

    @Column(name = "delivery_address_country")
    private String deliveryAddressCountry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "total_currency", nullable = false, length = 3)
    private String totalCurrency;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Construtor padrão para JPA
    protected OrderJpaEntity() {}

    // Factory method para converter de Domain Entity
    public static OrderJpaEntity fromDomain(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.id = order.getId();
        entity.customerId = order.getCustomerId();
        entity.status = order.getStatus();
        entity.totalAmount = order.getTotalAmount().getAmount();
        entity.totalCurrency = order.getTotalAmount().getCurrency();
        entity.createdAt = order.getCreatedAt();
        entity.updatedAt = order.getUpdatedAt();

        // Mapear endereço de entrega
        if (order.getDeliveryAddress() != null) {
            Address address = order.getDeliveryAddress();
            entity.deliveryAddressStreet = address.getStreet();
            entity.deliveryAddressNumber = address.getNumber();
            entity.deliveryAddressComplement = address.getComplement();
            entity.deliveryAddressNeighborhood = address.getNeighborhood();
            entity.deliveryAddressCity = address.getCity();
            entity.deliveryAddressState = address.getState();
            entity.deliveryAddressZipCode = address.getZipCode();
            entity.deliveryAddressCountry = address.getCountry();
        }

        // Mapear itens
        entity.items = order.getItems().stream()
                .map(item -> OrderItemJpaEntity.fromDomain(item, order.getId()))
                .toList();

        return entity;
    }

    // Método para converter para Domain Entity
    public Order toDomain() {
        // Criar endereço de entrega
        Address deliveryAddress = null;
        if (deliveryAddressStreet != null) {
            deliveryAddress = Address.builder()
                    .street(deliveryAddressStreet)
                    .number(deliveryAddressNumber)
                    .complement(deliveryAddressComplement)
                    .neighborhood(deliveryAddressNeighborhood)
                    .city(deliveryAddressCity)
                    .state(deliveryAddressState)
                    .zipCode(deliveryAddressZipCode)
                    .country(deliveryAddressCountry)
                    .build();
        }

        // Converter itens
        List<OrderItem> orderItems = items.stream()
                .map(OrderItemJpaEntity::toDomain)
                .toList();

        // Criar Money
        Money totalAmountMoney = Money.of(totalAmount, totalCurrency);

        // Reconstruir Order
        return Order.reconstruct(
                id, customerId, orderItems, deliveryAddress,
                status, totalAmountMoney, createdAt, updatedAt
        );
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public List<OrderItemJpaEntity> getItems() { return items; }
    public void setItems(List<OrderItemJpaEntity> items) { this.items = items; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getTotalCurrency() { return totalCurrency; }
    public void setTotalCurrency(String totalCurrency) { this.totalCurrency = totalCurrency; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Getters e Setters do endereço de entrega
    public String getDeliveryAddressStreet() { return deliveryAddressStreet; }
    public void setDeliveryAddressStreet(String deliveryAddressStreet) { this.deliveryAddressStreet = deliveryAddressStreet; }

    public String getDeliveryAddressNumber() { return deliveryAddressNumber; }
    public void setDeliveryAddressNumber(String deliveryAddressNumber) { this.deliveryAddressNumber = deliveryAddressNumber; }

    public String getDeliveryAddressComplement() { return deliveryAddressComplement; }
    public void setDeliveryAddressComplement(String deliveryAddressComplement) { this.deliveryAddressComplement = deliveryAddressComplement; }

    public String getDeliveryAddressNeighborhood() { return deliveryAddressNeighborhood; }
    public void setDeliveryAddressNeighborhood(String deliveryAddressNeighborhood) { this.deliveryAddressNeighborhood = deliveryAddressNeighborhood; }

    public String getDeliveryAddressCity() { return deliveryAddressCity; }
    public void setDeliveryAddressCity(String deliveryAddressCity) { this.deliveryAddressCity = deliveryAddressCity; }

    public String getDeliveryAddressState() { return deliveryAddressState; }
    public void setDeliveryAddressState(String deliveryAddressState) { this.deliveryAddressState = deliveryAddressState; }

    public String getDeliveryAddressZipCode() { return deliveryAddressZipCode; }
    public void setDeliveryAddressZipCode(String deliveryAddressZipCode) { this.deliveryAddressZipCode = deliveryAddressZipCode; }

    public String getDeliveryAddressCountry() { return deliveryAddressCountry; }
    public void setDeliveryAddressCountry(String deliveryAddressCountry) { this.deliveryAddressCountry = deliveryAddressCountry; }
}