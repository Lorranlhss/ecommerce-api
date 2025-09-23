package com.ecommerce.application.usecases.order;

import com.ecommerce.application.dto.OrderDTO;
import com.ecommerce.application.exceptions.NotFoundException;
import com.ecommerce.domain.entities.Order;
import com.ecommerce.domain.entities.OrderItem;
import com.ecommerce.domain.entities.Product;
import com.ecommerce.domain.repositories.OrderRepository;
import com.ecommerce.domain.repositories.ProductRepository;
import java.util.UUID;

/**
 * Use Case para remover item de um pedido.
 */
public class RemoveItemFromOrderUseCase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public RemoveItemFromOrderUseCase(OrderRepository orderRepository,
                                      ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderDTO execute(UUID orderId, UUID itemId) {
        // Buscar pedido
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> NotFoundException.order(orderId));

        // Buscar o item que será removido para devolver ao estoque
        OrderItem itemToRemove = order.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("OrderItem", itemId));

        // Buscar produto para devolver estoque
        Product product = productRepository.findById(itemToRemove.getProductId())
                .orElseThrow(() -> NotFoundException.product(itemToRemove.getProductId()));

        // Remover item do pedido
        order.removeItem(itemId);

        // Devolver estoque ao produto
        product.addStock(itemToRemove.getQuantity());

        // Persistir mudanças
        Order savedOrder = orderRepository.save(order);
        productRepository.save(product);

        // Retornar DTO
        return OrderDTO.from(savedOrder);
    }
}