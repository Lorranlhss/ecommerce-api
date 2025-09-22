package com.ecommerce.application.dto;

import com.ecommerce.domain.entities.OrderItem;
import java.util.UUID;

/**
 * DTO de resposta para itens de pedido.
 */
public record OrderItemDTO(
        UUID id,
        UUID productId,
        String productName,
        MoneyDTO unitPrice,
        Integer quantity,
        MoneyDTO totalPrice
) {

    public static OrderItemDTO from(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getProductId(),
                orderItem.getProductName(),
                MoneyDTO.from(orderItem.getUnitPrice()),
                orderItem.getQuantity(),
                MoneyDTO.from(orderItem.getTotalPrice())
        );
    }
}