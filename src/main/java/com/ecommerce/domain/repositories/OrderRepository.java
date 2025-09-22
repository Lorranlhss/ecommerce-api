package com.ecommerce.domain.repositories;

import com.ecommerce.domain.entities.Order;
import com.ecommerce.domain.entities.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface que define as operações de persistência para pedidos.
 */
public interface OrderRepository {

    /**
     * Salva um pedido (criação ou atualização).
     */
    Order save(Order order);

    /**
     * Busca um pedido por ID.
     */
    Optional<Order> findById(UUID id);

    /**
     * Busca todos os pedidos de um cliente.
     */
    List<Order> findByCustomerId(UUID customerId);

    /**
     * Busca pedidos por status.
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Busca pedidos de um cliente com status específico.
     */
    List<Order> findByCustomerIdAndStatus(UUID customerId, OrderStatus status);

    /**
     * Busca pedidos criados em um período.
     */
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca pedidos atualizados após uma data específica.
     */
    List<Order> findByUpdatedAtAfter(LocalDateTime date);

    /**
     * Remove um pedido por ID.
     */
    void deleteById(UUID id);

    /**
     * Conta pedidos por status.
     */
    long countByStatus(OrderStatus status);

    /**
     * Conta total de pedidos de um cliente.
     */
    long countByCustomerId(UUID customerId);
}