package com.ecommerce.application.usecases.product;

import com.ecommerce.application.dto.CreateProductDTO;
import com.ecommerce.application.dto.ProductDTO;
import com.ecommerce.application.exceptions.ValidationException;
import com.ecommerce.domain.entities.Product;
import com.ecommerce.domain.repositories.ProductRepository;
import com.ecommerce.domain.valueobjects.Money;

/**
 * Use Case para criação de produtos.
 * Aplica validações de negócio antes de persistir.
 */
public class CreateProductUseCase {

    private final ProductRepository productRepository;

    public CreateProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO execute(CreateProductDTO dto) {
        // Validar se produto com mesmo nome já existe
        if (productRepository.existsByName(dto.name())) {
            throw ValidationException.duplicateProductName(dto.name());
        }

        // Criar Money com validação
        Money price = Money.of(dto.price(), dto.currency());

        // Criar produto
        Product product = Product.create(
                dto.name(),
                dto.description(),
                price,
                dto.stockQuantity(),
                dto.category()
        );

        // Persistir
        Product savedProduct = productRepository.save(product);

        // Retornar DTO
        return ProductDTO.from(savedProduct);
    }
}