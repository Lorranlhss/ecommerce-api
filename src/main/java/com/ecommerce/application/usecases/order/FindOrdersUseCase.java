package com.ecommerce.application.usecases.order;

import com.ecommerce.application.dto.OrderDTO;
import com.ecommerce.application.exceptions.NotFoundException;
import com.ecommerce.domain.entities.Order;
import com.ecommerce.domain.entities.OrderStatus;
import com.ecommerce.domain.repositories.CustomerRepository;
import com.ecommerce.domain.repositories.OrderRepository;
import java.util.List;
import java.util.UUID;

/**
 * Use Case para busca de pedidos.
 */
public class FindOrdersUseCase {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public FindOrdersUseCase(OrderRepository orderRepository,
                             CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Busca um pedido por ID.
     */
    public OrderDTO findById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> NotFoundException.order(orderId));

        return OrderDTO.from(order);
    }

    /**
     * Busca todos os pedidos de um cliente.
     */
    public List<OrderDTO> findByCustomerId(UUID customerId) {
        // Validar se cliente existe
        customerRepository.findById(customerId)
                .orElseThrow(() -> NotFoundException.customer(customerId));

        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(OrderDTO::from)
                .toList();
    }

    /**
     * Busca pedidos por status.
     */
    public List<OrderDTO> findByStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        return orderRepository.findByStatus(status)
                .stream()
                .map(OrderDTO::from)
                .toList();
    }

    /**
     * Busca pedidos de um cliente com status específico.
     */
    public List<OrderDTO> findByCustomerIdAndStatus(UUID customerId, OrderStatus status) {
        // Validar se cliente existe
        customerRepository.findById(customerId)
                .orElseThrow(() -> NotFoundException.customer(customerId));

        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        return orderRepository.findByCustomerIdAndStatus(customerId, status)
                .stream()
                .map(OrderDTO::from)
                .toList();
    }
}