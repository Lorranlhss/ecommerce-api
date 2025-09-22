package com.ecommerce.application.usecases.order;

import com.ecommerce.application.dto.AddItemToOrderDTO;
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
 * Use Case para adicionar item a um pedido.
 */
public class AddItemToOrderUseCase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public AddItemToOrderUseCase(OrderRepository orderRepository,
                                 ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderDTO execute(UUID orderId, AddItemToOrderDTO dto) {
        // Buscar pedido
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> NotFoundException.order(orderId));

        // Buscar produto
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> NotFoundException.product(dto.productId()));

        // Validações de negócio
        validateProductAvailability(product, dto.quantity());

        // Verificar se produto já existe no pedido
        if (order.containsProduct(dto.productId())) {
            throw new ValidationException("Product already exists in this order. Use update quantity instead.");
        }

        // Criar item do pedido
        OrderItem orderItem = OrderItem.createFromProduct(product, dto.quantity());

        // Adicionar item ao pedido
        order.addItem(orderItem);

        // Atualizar estoque do produto
        product.removeStock(dto.quantity());

        // Persistir mudanças
        Order savedOrder = orderRepository.save(order);
        productRepository.save(product);

        // Retornar DTO
        return OrderDTO.from(savedOrder);
    }

    private void validateProductAvailability(Product product, Integer quantity) {
        if (!product.isActive()) {
            throw ValidationException.inactiveProduct(product.getName());
        }

        if (!product.isAvailable()) {
            throw new ValidationException("Product is not available: " + product.getName());
        }

        if (!product.hasStock(quantity)) {
            throw ValidationException.insufficientStock(
                    product.getName(),
                    product.getStockQuantity(),
                    quantity
            );
        }
    }
}