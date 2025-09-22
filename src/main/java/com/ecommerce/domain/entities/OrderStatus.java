package com.ecommerce.domain.entities;

/**
 * Enum que representa os possíveis status de um pedido.
 */
public enum OrderStatus {
    PENDING("Pendente"),
    CONFIRMED("Confirmado"),
    PREPARING("Preparando"),
    SHIPPED("Enviado"),
    DELIVERED("Entregue"),
    CANCELLED("Cancelado");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canTransitionTo(OrderStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == CONFIRMED || newStatus == CANCELLED;
            case CONFIRMED -> newStatus == PREPARING || newStatus == CANCELLED;
            case PREPARING -> newStatus == SHIPPED || newStatus == CANCELLED;
            case SHIPPED -> newStatus == DELIVERED;
            case DELIVERED, CANCELLED -> false; // Status finais
        };
    }

    public boolean isFinalStatus() {
        return this == DELIVERED || this == CANCELLED;
    }

    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED || this == PREPARING;
    }
}