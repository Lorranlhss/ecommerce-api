package com.ecommerce.application.usecases.order;

import com.ecommerce.application.dto.OrderDTO;
import com.ecommerce.application.exceptions.NotFoundException;
import com.ecommerce.application.exceptions.ValidationException;
import com.ecommerce.domain.entities.Order;
import com.ecommerce.domain.repositories.OrderRepository;
import java.util.UUID;

/**
 * Use Case para confirmar um pedido.
 * Move o pedido do status PENDING para CONFIRMED.
 */
public class ConfirmOrderUseCase {

    private final OrderRepository orderRepository;

    public ConfirmOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDTO execute(UUID orderId) {
        // Buscar pedido
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> NotFoundException.order(orderId));

        // Validar se pedido pode ser confirmado
        validateOrderCanBeConfirmed(order);

        // Confirmar pedido
        try {
            order.confirm();
        } catch (IllegalStateException e) {
            throw ValidationException.invalidOrderStatus(
                    order.getStatus().name(),
                    "CONFIRMED"
            );
        }

        // Persistir
        Order savedOrder = orderRepository.save(order);

        // Retornar DTO
        return OrderDTO.from(savedOrder);
    }

    private void validateOrderCanBeConfirmed(Order order) {
        if (order.isEmpty()) {
            throw new ValidationException("Cannot confirm an empty order");
        }

        if (order.isCompleted()) {
            throw new ValidationException("Order is already completed: " + order.getStatus());
        }
    }
}