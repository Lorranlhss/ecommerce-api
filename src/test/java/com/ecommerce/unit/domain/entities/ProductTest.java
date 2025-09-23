package com.ecommerce.unit.domain.entities;

import com.ecommerce.domain.entities.Product;
import com.ecommerce.domain.valueobjects.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Product Entity Tests")
class ProductTest {

    @Test
    @DisplayName("Deve criar produto válido usando factory method")
    void shouldCreateValidProductUsingFactoryMethod() {
        // Given
        var name = "iPhone 15 Pro";
        var description = "Smartphone Apple";
        var price = Money.ofBRL(7999.99);
        var stockQuantity = 50;
        var category = "Smartphones";

        // When
        var product = Product.create(name, description, price, stockQuantity, category);

        // Then
        assertThat(product.getId()).isNotNull();
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getDescription()).isEqualTo(description);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getStockQuantity()).isEqualTo(stockQuantity);
        assertThat(product.getCategory()).isEqualTo(category);
        assertThat(product.isActive()).isTrue();
        assertThat(product.isAvailable()).isTrue();
        assertThat(product.getCreatedAt()).isNotNull();
        assertThat(product.getUpdatedAt()).isNotNull();
        assertThat(product.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("Deve falhar com nome inválido")
    void shouldFailWithInvalidName(String invalidName) {
        // Given
        var price = Money.ofBRL(99.99);

        // When & Then
        assertThatThrownBy(() -> Product.create(
                invalidName, "Description", price, 10, "Category"
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name cannot be null or empty");
    }

    @Test
    @DisplayName("Deve falhar com nome null")
    void shouldFailWithNullName() {
        // Given
        var price = Money.ofBRL(99.99);

        // When & Then
        assertThatThrownBy(() -> Product.create(
                null, "Description", price, 10, "Category"
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name cannot be null or empty");
    }

    @Test
    @DisplayName("Deve falhar com description null ou vazia")
    void shouldFailWithInvalidDescription() {
        // Given
        var price = Money.ofBRL(99.99);

        // When & Then - Null description
        assertThatThrownBy(() -> Product.create(
                "Product", null, price, 10, "Category"
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product description cannot be null or empty");

        // When & Then - Empty description
        assertThatThrownBy(() -> Product.create(
                "Product", "", price, 10, "Category"
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product description cannot be null or empty");
    }

    @Test
    @DisplayName("Deve falhar com price null")
    void shouldFailWithNullPrice() {
        // When & Then
        assertThatThrownBy(() -> Product.create(
                "Product", "Description", null, 10, "Category"
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product price must be positive");
    }

    @Test
    @DisplayName("Deve falhar com price negativo")
    void shouldFailWithNegativePrice() {
        // Given
        var negativePrice = Money.ofBRL(-50.0);

        // When & Then
        assertThatThrownBy(() -> Product.create(
                "Product", "Description", negativePrice, 10, "Category"
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product price must be positive");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    @DisplayName("Deve falhar com stockQuantity negativo")
    void shouldFailWithNegativeStockQuantity(int negativeStock) {
        // Given
        var price = Money.ofBRL(99.99);

        // When & Then
        assertThatThrownBy(() -> Product.create(
                "Product", "Description", price, negativeStock, "Category"
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Stock quantity cannot be negative");
    }

    @Test
    @DisplayName("Deve aceitar stockQuantity zero")
    void shouldAcceptZeroStockQuantity() {
        // Given
        var price = Money.ofBRL(99.99);

        // When
        var product = Product.create("Product", "Description", price, 0, "Category");

        // Then
        assertThat(product.getStockQuantity()).isZero();
        assertThat(product.isActive()).isTrue();
        assertThat(product.isAvailable()).isFalse(); // Sem estoque = não disponível
    }

    @Test
    @DisplayName("Deve falhar com category null ou vazia")
    void shouldFailWithInvalidCategory() {
        // Given
        var price = Money.ofBRL(99.99);

        // When & Then - Null category
        assertThatThrownBy(() -> Product.create(
                "Product", "Description", price, 10, null
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product category cannot be null or empty");

        // When & Then - Empty category
        assertThatThrownBy(() -> Product.create(
                "Product", "Description", price, 10, ""
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product category cannot be null or empty");
    }

    @Test
    @DisplayName("Deve adicionar estoque corretamente")
    void shouldAddStockCorrectly() {
        // Given
        var product = createValidProduct();
        var initialStock = product.getStockQuantity();
        var addition = 10;

        // When
        product.addStock(addition);

        // Then
        assertThat(product.getStockQuantity()).isEqualTo(initialStock + addition);
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve falhar ao adicionar estoque com quantidade inválida")
    void shouldFailWhenAddingInvalidStockQuantity() {
        // Given
        var product = createValidProduct();

        // When & Then - Zero
        assertThatThrownBy(() -> product.addStock(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity to add must be positive");

        // When & Then - Negativo
        assertThatThrownBy(() -> product.addStock(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity to add must be positive");
    }

    @Test
    @DisplayName("Deve remover estoque corretamente")
    void shouldRemoveStockCorrectly() {
        // Given
        var product = createValidProduct();
        var initialStock = product.getStockQuantity();
        var removal = 3;

        // When
        product.removeStock(removal);

        // Then
        assertThat(product.getStockQuantity()).isEqualTo(initialStock - removal);
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve falhar ao remover estoque mais que disponível")
    void shouldFailWhenRemovingMoreStockThanAvailable() {
        // Given
        var product = createValidProduct();
        var excessiveRemoval = product.getStockQuantity() + 1;

        // When & Then
        assertThatThrownBy(() -> product.removeStock(excessiveRemoval))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Insufficient stock");
    }

    @Test
    @DisplayName("Deve falhar ao remover estoque com quantidade inválida")
    void shouldFailWhenRemovingInvalidStockQuantity() {
        // Given
        var product = createValidProduct();

        // When & Then - Zero
        assertThatThrownBy(() -> product.removeStock(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity to remove must be positive");

        // When & Then - Negativo
        assertThatThrownBy(() -> product.removeStock(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity to remove must be positive");
    }

    @Test
    @DisplayName("Deve atualizar informações do produto")
    void shouldUpdateProductInformation() {
        // Given
        var product = createValidProduct();
        var originalUpdatedAt = product.getUpdatedAt();

        var newName = "iPhone 15 Pro Max";
        var newDescription = "Smartphone Apple - Versão Max";
        var newPrice = Money.ofBRL(8999.99);
        var newStockQuantity = 25;
        var newCategory = "Premium Smartphones";

        // When
        product.updateProductInfo(newName, newDescription, newPrice, newStockQuantity, newCategory);

        // Then
        assertThat(product.getName()).isEqualTo(newName);
        assertThat(product.getDescription()).isEqualTo(newDescription);
        assertThat(product.getPrice()).isEqualTo(newPrice);
        assertThat(product.getStockQuantity()).isEqualTo(newStockQuantity);
        assertThat(product.getCategory()).isEqualTo(newCategory);
        assertThat(product.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Deve atualizar apenas o preço")
    void shouldUpdatePriceOnly() {
        // Given
        var product = createValidProduct();
        var originalName = product.getName();
        var originalUpdatedAt = product.getUpdatedAt();
        var newPrice = Money.ofBRL(9999.99);

        // When
        product.updatePrice(newPrice);

        // Then
        assertThat(product.getPrice()).isEqualTo(newPrice);
        assertThat(product.getName()).isEqualTo(originalName); // Outros campos não mudam
        assertThat(product.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Deve falhar ao atualizar preço com valor inválido")
    void shouldFailWhenUpdatingWithInvalidPrice() {
        // Given
        var product = createValidProduct();

        // When & Then - Price null
        assertThatThrownBy(() -> product.updatePrice(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Price must be positive");

        // When & Then - Price negativo
        var negativePrice = Money.ofBRL(-100.0);
        assertThatThrownBy(() -> product.updatePrice(negativePrice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Price must be positive");
    }

    @Test
    @DisplayName("Deve desativar produto")
    void shouldDeactivateProduct() {
        // Given
        var product = createValidProduct();
        assertThat(product.isActive()).isTrue();

        // When
        product.deactivate();

        // Then
        assertThat(product.isActive()).isFalse();
        assertThat(product.isAvailable()).isFalse();
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve ativar produto")
    void shouldActivateProduct() {
        // Given
        var product = createValidProduct();
        product.deactivate();
        assertThat(product.isActive()).isFalse();

        // When
        product.activate();

        // Then
        assertThat(product.isActive()).isTrue();
        assertThat(product.isAvailable()).isTrue(); // Ativo + com estoque = disponível
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve verificar disponibilidade de estoque")
    void shouldCheckStockAvailability() {
        // Given
        var product = createValidProduct(); // Criado com 50 de estoque

        // Then
        assertThat(product.hasStock(10)).isTrue();
        assertThat(product.hasStock(50)).isTrue();
        assertThat(product.hasStock(51)).isFalse();
        assertThat(product.hasStock(100)).isFalse();
    }

    @Test
    @DisplayName("Deve calcular preço total corretamente")
    void shouldCalculateTotalPriceCorrectly() {
        // Given
        var product = createValidProduct(); // Preço R$ 7999.99
        var quantity = 3;

        // When
        var totalPrice = product.calculateTotalPrice(quantity);

        // Then
        var expectedTotal = Money.ofBRL(7999.99 * 3);
        assertThat(totalPrice).isEqualTo(expectedTotal);
    }

    @Test
    @DisplayName("Deve falhar ao calcular preço total com quantidade inválida")
    void shouldFailWhenCalculatingTotalPriceWithInvalidQuantity() {
        // Given
        var product = createValidProduct();

        // When & Then - Zero
        assertThatThrownBy(() -> product.calculateTotalPrice(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be positive");

        // When & Then - Negativo
        assertThatThrownBy(() -> product.calculateTotalPrice(-2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be positive");
    }

    @Test
    @DisplayName("Produto sem estoque não deve estar disponível mesmo ativo")
    void productWithoutStockShouldNotBeAvailable() {
        // Given
        var product = createValidProduct();

        // When
        product.removeStock(product.getStockQuantity()); // Remover todo o estoque

        // Then
        assertThat(product.getStockQuantity()).isZero();
        assertThat(product.isActive()).isTrue();
        assertThat(product.isAvailable()).isFalse(); // Sem estoque = não disponível
    }

    @Test
    @DisplayName("Produto inativo não deve estar disponível mesmo com estoque")
    void inactiveProductShouldNotBeAvailable() {
        // Given
        var product = createValidProduct();
        assertThat(product.getStockQuantity()).isGreaterThan(0);

        // When
        product.deactivate();

        // Then
        assertThat(product.getStockQuantity()).isGreaterThan(0);
        assertThat(product.isActive()).isFalse();
        assertThat(product.isAvailable()).isFalse(); // Inativo = não disponível
    }

    @Test
    @DisplayName("Deve calcular disponibilidade corretamente")
    void shouldCalculateAvailabilityCorrectly() {
        // Ativo + Com estoque = Disponível
        var product1 = createValidProduct();
        assertThat(product1.isAvailable()).isTrue();

        // Ativo + Sem estoque = Não disponível
        var product2 = Product.create("Product", "Desc", Money.ofBRL(100), 0, "Cat");
        assertThat(product2.isAvailable()).isFalse();

        // Inativo + Com estoque = Não disponível
        var product3 = createValidProduct();
        product3.deactivate();
        assertThat(product3.isAvailable()).isFalse();

        // Inativo + Sem estoque = Não disponível
        var product4 = Product.create("Product", "Desc", Money.ofBRL(100), 0, "Cat");
        product4.deactivate();
        assertThat(product4.isAvailable()).isFalse();
    }

    @Test
    @DisplayName("Deve manter consistência temporal")
    void shouldMaintainTemporalConsistency() {
        // Given
        var product = createValidProduct();
        var createdAt = product.getCreatedAt();
        var initialUpdatedAt = product.getUpdatedAt();

        // When - Fazer alguma alteração
        product.addStock(5);

        // Then
        assertThat(product.getCreatedAt()).isEqualTo(createdAt); // CreatedAt não muda
        assertThat(product.getUpdatedAt()).isAfter(initialUpdatedAt); // UpdatedAt muda
    }

    @Test
    @DisplayName("Deve trimmar strings no input")
    void shouldTrimStringsInInput() {
        // Given
        var nameWithSpaces = "  iPhone 15 Pro  ";
        var descriptionWithSpaces = "  Smartphone Apple  ";
        var categoryWithSpaces = "  Smartphones  ";

        // When
        var product = Product.create(nameWithSpaces, descriptionWithSpaces,
                Money.ofBRL(100), 10, categoryWithSpaces);

        // Then
        assertThat(product.getName()).isEqualTo("iPhone 15 Pro");
        assertThat(product.getDescription()).isEqualTo("Smartphone Apple");
        assertThat(product.getCategory()).isEqualTo("Smartphones");
    }

    private Product createValidProduct() {
        return Product.create(
                "iPhone 15 Pro",
                "Smartphone Apple",
                Money.ofBRL(7999.99),
                50,
                "Smartphones"
        );
    }
}