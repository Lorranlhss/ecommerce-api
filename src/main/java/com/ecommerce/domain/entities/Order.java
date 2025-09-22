package com.ecommerce.domain.entities;

import com.ecommerce.domain.valueobjects.Address;
import com.ecommerce.domain.valueobjects.Money;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade agregada que representa um pedido.
 * Contém itens, cliente, endereço de entrega e controla o ciclo de vida do pedido.
 */
public class Order {

    private final UUID id;
    private final UUID customerId;
    private final List<OrderItem> items;
    private Address deliveryAddress;
    private OrderStatus status;
    private Money totalAmount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Construtor para criação de novos pedidos
    private Order(UUID customerId, Address deliveryAddress) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.items = new ArrayList<>();
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.PENDING;
        this.totalAmount = Money.zeroBRL();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        validateOrderCreation(customerId);
    }

    // Construtor para reconstrução
    private Order(UUID id, UUID customerId, List<OrderItem> items, Address deliveryAddress,
                  OrderStatus status, Money totalAmount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory methods
    public static Order create(UUID customerId, Address deliveryAddress) {
        return new Order(customerId, deliveryAddress);
    }

    public static Order reconstruct(UUID id, UUID customerId, List<OrderItem> items,
                                    Address deliveryAddress, OrderStatus status, Money totalAmount,
                                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Order(id, customerId, items, deliveryAddress, status, totalAmount, createdAt, updatedAt);
    }

    // Métodos de negócio para gerenciar itens
    public void addItem(OrderItem item) {
        validateOrderCanBeModified();

        if (item == null) {
            throw new IllegalArgumentException("OrderItem cannot be null");
        }

        // Verifica se já existe um item para o mesmo produto
        for (OrderItem existingItem : items) {
            if (existingItem.isForProduct(item.getProductId())) {
                throw new IllegalStateException("Product already exists in order. Use updateItemQuantity instead.");
            }
        }

        items.add(item);
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    public void removeItem(UUID itemId) {
        validateOrderCanBeModified();

        boolean removed = items.removeIf(item -> item.getId().equals(itemId));

        if (!removed) {
            throw new IllegalArgumentException("Item not found in order: " + itemId);
        }

        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    public void clearItems() {
        validateOrderCanBeModified();
        items.clear();
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    // Métodos de negócio para status
    public void confirm() {
        validateStatusTransition(OrderStatus.CONFIRMED);
        validateOrderHasItems();

        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void startPreparing() {
        validateStatusTransition(OrderStatus.PREPARING);
        this.status = OrderStatus.PREPARING;
        this.updatedAt = LocalDateTime.now();
    }

    public void ship() {
        validateStatusTransition(OrderStatus.SHIPPED);
        this.status = OrderStatus.SHIPPED;
        this.updatedAt = LocalDateTime.now();
    }

    public void deliver() {
        validateStatusTransition(OrderStatus.DELIVERED);
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (!status.canBeCancelled()) {
            throw new IllegalStateException("Order cannot be cancelled in status: " + status);
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    // Métodos de negócio auxiliares
    public void updateDeliveryAddress(Address newAddress) {
        validateOrderCanBeModified();

        if (newAddress == null) {
            throw new IllegalArgumentException("Delivery address cannot be null");
        }

        this.deliveryAddress = newAddress;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalItems() {
        return items.size();
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    public boolean containsProduct(UUID productId) {
        return items.stream().anyMatch(item -> item.isForProduct(productId));
    }

    public boolean canBeModified() {
        return status == OrderStatus.PENDING;
    }

    public boolean isCompleted() {
        return status.isFinalStatus();
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Money.zeroBRL(), Money::add);
    }

    private void validateOrderCreation(UUID customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
    }

    private void validateOrderCanBeModified() {
        if (!canBeModified()) {
            throw new IllegalStateException("Order cannot be modified in status: " + status);
        }
    }

    private void validateStatusTransition(OrderStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("Cannot transition from %s to %s", status, newStatus)
            );
        }
    }

    private void validateOrderHasItems() {
        if (isEmpty()) {
            throw new IllegalStateException("Order must have at least one item");
        }
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public Address getDeliveryAddress() { return deliveryAddress; }
    public OrderStatus getStatus() { return status; }
    public Money getTotalAmount() { return totalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Order order = (Order) obj;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Order{id=%s, customerId=%s, status=%s, totalAmount=%s, items=%d}",
                id, customerId, status, totalAmount, items.size());
    }
}