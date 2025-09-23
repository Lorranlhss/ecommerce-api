package com.ecommerce.application.usecases.product;

import com.ecommerce.application.dto.ProductDTO;
import com.ecommerce.application.dto.UpdateProductDTO;
import com.ecommerce.application.exceptions.NotFoundException;
import com.ecommerce.application.exceptions.ValidationException;
import com.ecommerce.domain.entities.Product;
import com.ecommerce.domain.repositories.ProductRepository;
import com.ecommerce.domain.valueobjects.Money;
import java.util.UUID;

/**
 * Use Case para atualização de produtos.
 */
public class UpdateProductUseCase {

    private final ProductRepository productRepository;

    public UpdateProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO execute(UUID productId, UpdateProductDTO dto) {
        // Buscar produto existente
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> NotFoundException.product(productId));

        // Validar se nome não duplica com outro produto
        if (productRepository.existsByName(dto.name()) &&
                !product.getName().equals(dto.name())) {
            throw ValidationException.duplicateProductName(dto.name());
        }

        // Criar Money com validação
        Money price = Money.of(dto.price(), dto.currency());

        // Atualizar produto
        try {
            product.updateProductInfo(
                    dto.name(),
                    dto.description(),
                    price,
                    dto.stockQuantity(),
                    dto.category()
            );
        } catch (IllegalArgumentException e) {
            throw new ValidationException(e.getMessage(), e);
        }

        // Persistir
        Product savedProduct = productRepository.save(product);

        // Retornar DTO
        return ProductDTO.from(savedProduct);
    }
}