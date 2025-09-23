package com.ecommerce.infrastructure.adapters.repositories.jpa;

import com.ecommerce.infrastructure.adapters.repositories.entities.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository Spring Data JPA para clientes.
 */
@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, UUID> {

    /**
     * Busca cliente por email.
     */
    Optional<CustomerJpaEntity> findByEmailIgnoreCase(String email);

    /**
     * Busca clientes ativos.
     */
    List<CustomerJpaEntity> findByActiveTrueOrderByFirstNameAsc();

    /**
     * Busca clientes por nome (busca parcial, case-insensitive).
     */
    @Query("SELECT c FROM CustomerJpaEntity c WHERE " +
            "(LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "ORDER BY c.firstName ASC, c.lastName ASC")
    List<CustomerJpaEntity> findByNameContaining(@Param("name") String name);

    /**
     * Verifica se existe cliente com email específico.
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Conta clientes ativos.
     */
    long countByActiveTrue();
}