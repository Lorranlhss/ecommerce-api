package com.ecommerce.domain.repositories;

import com.ecommerce.domain.entities.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface que define as operações de persistência para produtos.
 * Seguindo o padrão Repository do DDD.
 */
public interface ProductRepository {

    /**
     * Salva um produto (criação ou atualização).
     */
    Product save(Product product);

    /**
     * Busca um produto por ID.
     */
    Optional<Product> findById(UUID id);

    /**
     * Busca todos os produtos ativos.
     */
    List<Product> findAllActive();

    /**
     * Busca produtos por categoria.
     */
    List<Product> findByCategory(String category);

    /**
     * Busca produtos por nome (busca parcial, case-insensitive).
     */
    List<Product> findByNameContaining(String name);

    /**
     * Busca produtos disponíveis (ativos e com estoque).
     */
    List<Product> findAvailableProducts();

    /**
     * Busca produtos com estoque baixo (menor que quantidade especificada).
     */
    List<Product> findProductsWithLowStock(int threshold);

    /**
     * Verifica se existe um produto com o nome especificado.
     */
    boolean existsByName(String name);

    /**
     * Remove um produto por ID.
     */
    void deleteById(UUID id);

    /**
     * Conta o total de produtos ativos.
     */
    long countActiveProducts();

    /**
     * Busca todas as categorias distintas.
     */
    List<String> findAllCategories();
}