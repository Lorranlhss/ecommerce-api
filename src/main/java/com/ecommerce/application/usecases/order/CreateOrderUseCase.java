package com.ecommerce.application.usecases.order;

import com.ecommerce.application.dto.CreateOrderDTO;
import com.ecommerce.application.dto.OrderDTO;
import com.ecommerce.application.exceptions.NotFoundException;
import com.ecommerce.application.exceptions.ValidationException;
import com.ecommerce.domain.entities.Customer;
import com.ecommerce.domain.entities.Order;
import com.ecommerce.domain.repositories.CustomerRepository;
import com.ecommerce.domain.repositories.OrderRepository;
import com.ecommerce.domain.valueobjects.Address;

/**
 * Use Case para criação de pedidos.
 */
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public CreateOrderUseCase(OrderRepository orderRepository,
                              CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public OrderDTO execute(CreateOrderDTO dto) {
        // Validar se cliente existe e está ativo
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> NotFoundException.customer(dto.customerId()));

        if (!customer.canPlaceOrders()) {
            throw new ValidationException("Customer is inactive and cannot place orders");
        }

        // Converter endereço
        Address deliveryAddress = dto.deliveryAddress().toDomain();

        // Criar pedido
        Order order = Order.create(dto.customerId(), deliveryAddress);

        // Persistir
        Order savedOrder = orderRepository.save(order);

        // Retornar DTO
        return OrderDTO.from(savedOrder);
    }
}