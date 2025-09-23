package com.ecommerce.infrastructure.adapters.repositories.jpa;

import com.ecommerce.domain.entities.OrderStatus;
import com.ecommerce.infrastructure.adapters.repositories.entities.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository Spring Data JPA para pedidos.
 */
@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {

    /**
     * Busca pedidos de um cliente.
     */
    List<OrderJpaEntity> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);

    /**
     * Busca pedidos por status.
     */
    List<OrderJpaEntity> findByStatusOrderByCreatedAtDesc(OrderStatus status);

    /**
     * Busca pedidos de um cliente com status específico.
     */
    List<OrderJpaEntity> findByCustomerIdAndStatusOrderByCreatedAtDesc(UUID customerId, OrderStatus status);

    /**
     * Busca pedidos criados em um período.
     */
    @Query("SELECT o FROM OrderJpaEntity o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate ORDER BY o.createdAt DESC")
    List<OrderJpaEntity> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    /**
     * Busca pedidos atualizados após uma data específica.
     */
    List<OrderJpaEntity> findByUpdatedAtAfterOrderByUpdatedAtDesc(LocalDateTime date);

    /**
     * Conta pedidos por status.
     */
    long countByStatus(OrderStatus status);

    /**
     * Conta total de pedidos de um cliente.
     */
    long countByCustomerId(UUID customerId);
}