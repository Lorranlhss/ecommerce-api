package com.ecommerce.application.usecases.product;

import com.ecommerce.application.dto.ProductDTO;
import com.ecommerce.application.exceptions.NotFoundException;
import com.ecommerce.domain.entities.Product;
import com.ecommerce.domain.repositories.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Use Case para busca de produtos.
 * Centraliza as diferentes formas de busca.
 */
public class FindProductsUseCase {

    private final ProductRepository productRepository;

    public FindProductsUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Busca um produto por ID.
     */
    public ProductDTO findById(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ProductDTO::from)
                .orElseThrow(() -> NotFoundException.product(id));
    }

    /**
     * Lista todos os produtos ativos.
     */
    public List<ProductDTO> findAllActive() {
        return productRepository.findAllActive()
                .stream()
                .map(ProductDTO::from)
                .toList();
    }

    /**
     * Busca produtos por categoria.
     */
    public List<ProductDTO> findByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }

        return productRepository.findByCategory(category.trim())
                .stream()
                .map(ProductDTO::from)
                .toList();
    }

    /**
     * Busca produtos por nome (busca parcial).
     */
    public List<ProductDTO> findByNameContaining(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        return productRepository.findByNameContaining(name.trim())
                .stream()
                .map(ProductDTO::from)
                .toList();
    }

    /**
     * Lista produtos disponíveis (ativos e com estoque).
     */
    public List<ProductDTO> findAvailableProducts() {
        return productRepository.findAvailableProducts()
                .stream()
                .map(ProductDTO::from)
                .toList();
    }

    /**
     * Lista todas as categorias disponíveis.
     */
    public List<String> findAllCategories() {
        return productRepository.findAllCategories();
    }
}