package com.ecommerce.infrastructure.adapters.repositories;

import com.ecommerce.domain.entities.Order;
import com.ecommerce.domain.entities.OrderStatus;
import com.ecommerce.domain.repositories.OrderRepository;
import com.ecommerce.infrastructure.adapters.repositories.entities.OrderJpaEntity;
import com.ecommerce.infrastructure.adapters.repositories.jpa.OrderJpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação do repositório de pedidos usando Spring Data JPA.
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = OrderJpaEntity.fromDomain(order);
        OrderJpaEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(OrderJpaEntity::toDomain);
    }

    @Override
    public List<Order> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(OrderJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return jpaRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(OrderJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Order> findByCustomerIdAndStatus(UUID customerId, OrderStatus status) {
        return jpaRepository.findByCustomerIdAndStatusOrderByCreatedAtDesc(customerId, status)
                .stream()
                .map(OrderJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByCreatedAtBetween(startDate, endDate)
                .stream()
                .map(OrderJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Order> findByUpdatedAtAfter(LocalDateTime date) {
        return jpaRepository.findByUpdatedAtAfterOrderByUpdatedAtDesc(date)
                .stream()
                .map(OrderJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long countByStatus(OrderStatus status) {
        return jpaRepository.countByStatus(status);
    }

    @Override
    public long countByCustomerId(UUID customerId) {
        return jpaRepository.countByCustomerId(customerId);
    }
}