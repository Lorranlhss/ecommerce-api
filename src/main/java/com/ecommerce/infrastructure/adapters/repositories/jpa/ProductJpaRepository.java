package com.ecommerce.infrastructure.adapters.repositories.jpa;

import com.ecommerce.infrastructure.adapters.repositories.entities.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

/**
 * Repository Spring Data JPA para produtos.
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {

    /**
     * Busca produtos ativos.
     */
    List<ProductJpaEntity> findByActiveTrueOrderByNameAsc();

    /**
     * Busca produtos por categoria.
     */
    List<ProductJpaEntity> findByCategoryIgnoreCaseOrderByNameAsc(String category);

    /**
     * Busca produtos por nome (busca parcial, case-insensitive).
     */
    List<ProductJpaEntity> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    /**
     * Busca produtos disponíveis (ativos e com estoque).
     */
    @Query("SELECT p FROM ProductJpaEntity p WHERE p.active = true AND p.stockQuantity > 0 ORDER BY p.name ASC")
    List<ProductJpaEntity> findAvailableProducts();

    /**
     * Busca produtos com estoque baixo.
     */
    @Query("SELECT p FROM ProductJpaEntity p WHERE p.active = true AND p.stockQuantity < :threshold ORDER BY p.stockQuantity ASC")
    List<ProductJpaEntity> findProductsWithLowStock(@Param("threshold") int threshold);

    /**
     * Verifica se existe produto com nome específico.
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Conta produtos ativos.
     */
    long countByActiveTrue();

    /**
     * Busca todas as categorias distintas.
     */
    @Query("SELECT DISTINCT p.category FROM ProductJpaEntity p WHERE p.active = true ORDER BY p.category ASC")
    List<String> findAllDistinctCategories();
}