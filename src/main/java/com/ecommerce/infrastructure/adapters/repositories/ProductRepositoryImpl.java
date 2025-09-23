package com.ecommerce.infrastructure.adapters.repositories;

import com.ecommerce.domain.entities.Product;
import com.ecommerce.domain.repositories.ProductRepository;
import com.ecommerce.infrastructure.adapters.repositories.entities.ProductJpaEntity;
import com.ecommerce.infrastructure.adapters.repositories.jpa.ProductJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação do repositório de produtos usando Spring Data JPA.
 * Adapta entre a interface do domain e a persistência JPA.
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;

    public ProductRepositoryImpl(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = ProductJpaEntity.fromDomain(product);
        ProductJpaEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(ProductJpaEntity::toDomain);
    }

    @Override
    public List<Product> findAllActive() {
        return jpaRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(ProductJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Product> findByCategory(String category) {
        return jpaRepository.findByCategoryIgnoreCaseOrderByNameAsc(category)
                .stream()
                .map(ProductJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Product> findByNameContaining(String name) {
        return jpaRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name)
                .stream()
                .map(ProductJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Product> findAvailableProducts() {
        return jpaRepository.findAvailableProducts()
                .stream()
                .map(ProductJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Product> findProductsWithLowStock(int threshold) {
        return jpaRepository.findProductsWithLowStock(threshold)
                .stream()
                .map(ProductJpaEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long countActiveProducts() {
        return jpaRepository.countByActiveTrue();
    }

    @Override
    public List<String> findAllCategories() {
        return jpaRepository.findAllDistinctCategories();
    }
}