package com.ecommerce.application.dto;

import com.ecommerce.domain.entities.Order;
import com.ecommerce.domain.entities.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de resposta para pedidos.
 */
public record OrderDTO(
        UUID id,
        UUID customerId,
        List<OrderItemDTO> items,
        AddressDTO deliveryAddress,
        OrderStatus status,
        String statusDescription,
        MoneyDTO totalAmount,
        Integer totalItems,
        Integer totalQuantity,
        boolean canBeModified,
        boolean isCompleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static OrderDTO from(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems()
                .stream()
                .map(OrderItemDTO::from)
                .toList();

        return new OrderDTO(
                order.getId(),
                order.getCustomerId(),
                itemDTOs,
                AddressDTO.from(order.getDeliveryAddress()),
                order.getStatus(),
                order.getStatus().getDescription(),
                MoneyDTO.from(order.getTotalAmount()),
                order.getTotalItems(),
                order.getTotalQuantity(),
                order.canBeModified(),
                order.isCompleted(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}