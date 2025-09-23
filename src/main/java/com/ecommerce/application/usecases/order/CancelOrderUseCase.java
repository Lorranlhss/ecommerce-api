package com.ecommerce.application.usecases.order;

import com.ecommerce.application.dto.OrderDTO;
import com.ecommerce.application.exceptions.NotFoundException;
import com.ecommerce.application.exceptions.ValidationException;
import com.ecommerce.domain.entities.Order;
import com.ecommerce.domain.entities.OrderItem;
import com.ecommerce.domain.entities.Product;
import com.ecommerce.domain.repositories.OrderRepository;
import com.ecommerce.domain.repositories.ProductRepository;
import java.util.UUID;

/**
 * Use Case para cancelar um pedido.
 * Devolve o estoque dos produtos.
 */
public class CancelOrderUseCase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public CancelOrderUseCase(OrderRepository orderRepository,
                              ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderDTO execute(UUID orderId) {
        // Buscar pedido
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> NotFoundException.order(orderId));

        // Validar se pedido pode ser cancelado
        if (!order.getStatus().canBeCancelled()) {
            throw new ValidationException("Order cannot be cancelled in current status: " + order.getStatus());
        }

        // Devolver estoque de todos os itens
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> NotFoundException.product(item.getProductId()));

            product.addStock(item.getQuantity());
            productRepository.save(product);
        }

        // Cancelar pedido
        try {
            order.cancel();
        } catch (IllegalStateException e) {
            throw new ValidationException(e.getMessage());
        }

        // Persistir
        Order savedOrder = orderRepository.save(order);

        // Retornar DTO
        return OrderDTO.from(savedOrder);
    }
}