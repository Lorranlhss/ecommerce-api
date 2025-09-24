package com.ecommerce.unit.domain.entities;

import com.ecommerce.domain.entities.OrderItem;
import com.ecommerce.domain.entities.Product;
import com.ecommerce.domain.valueobjects.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OrderItem Entity Tests")
class OrderItemTest {

    @Test
    @DisplayName("Deve criar item de pedido válido")
    void shouldCreateValidOrderItem() {
        // Given
        var productId = UUID.randomUUID();
        var productName = "iPhone 15 Pro";
        var unitPrice = Money.ofBRL(7999.99);
        var quantity = 2;

        // When
        var orderItem = OrderItem.create(productId, productName, unitPrice, quantity);

        // Then
        assertThat(orderItem.getId()).isNotNull();
        assertThat(orderItem.getProductId()).isEqualTo(productId);
        assertThat(orderItem.getProductName()).isEqualTo(productName);
        assertThat(orderItem.getUnitPrice()).isEqualTo(unitPrice);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);

        var expectedTotal = unitPrice.multiply(quantity);
        assertThat(orderItem.getTotalPrice()).isEqualTo(expectedTotal);
    }

    @Test
    @DisplayName("Deve criar item a partir de produto")
    void shouldCreateItemFromProduct() {
        // Given
        var product = createValidProduct();
        var quantity = 3;

        // When
        var orderItem = OrderItem.createFromProduct(product, quantity);

        // Then
        assertThat(orderItem.getProductId()).isEqualTo(product.getId());
        assertThat(orderItem.getProductName()).isEqualTo(product.getName());
        assertThat(orderItem.getUnitPrice()).isEqualTo(product.getPrice());
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);

        var expectedTotal = product.getPrice().multiply(quantity);
        assertThat(orderItem.getTotalPrice()).isEqualTo(expectedTotal);
    }

    @Test
    @DisplayName("Deve falhar ao criar item com produto null")
    void shouldFailWhenCreatingItemWithNullProduct() {
        // When & Then
        assertThatThrownBy(() -> OrderItem.createFromProduct(null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product cannot be null");
    }

    @Test
    @DisplayName("Deve falhar ao criar item com produto indisponível")
    void shouldFailWhenCreatingItemWithUnavailableProduct() {
        // Given
        var product = createValidProduct();
        product.deactivate(); // Torna indisponível
        assertThat(product.isAvailable()).isFalse();

        // When & Then
        assertThatThrownBy(() -> OrderItem.createFromProduct(product, 1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Product is not available");
    }

    @Test
    @DisplayName("Deve falhar ao criar item com estoque insuficiente")
    void shouldFailWhenCreatingItemWithInsufficientStock() {
        // Given
        var product = createValidProduct();
        var excessiveQuantity = product.getStockQuantity() + 1;

        // When & Then
        assertThatThrownBy(() -> OrderItem.createFromProduct(product, excessiveQuantity))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Insufficient stock for product");
    }

    @Test
    @DisplayName("Deve falhar com productId null")
    void shouldFailWithNullProductId() {
        // When & Then
        assertThatThrownBy(() -> OrderItem.create(
                null, "Product", Money.ofBRL(100), 1
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product ID cannot be null");
    }

    @Test
    @DisplayName("Deve falhar com productName null ou vazio")
    void shouldFailWithNullOrEmptyProductName() {
        var productId = UUID.randomUUID();
        var price = Money.ofBRL(100);

        // When & Then - Null
        assertThatThrownBy(() -> OrderItem.create(
                productId, null, price, 1
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name cannot be null or empty");

        // When & Then - Empty
        assertThatThrownBy(() -> OrderItem.create(
                productId, "", price, 1
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name cannot be null or empty");

        // When & Then - Whitespace
        assertThatThrownBy(() -> OrderItem.create(
                productId, "   ", price, 1
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name cannot be null or empty");
    }

    @Test
    @DisplayName("Deve falhar com unitPrice null ou negativo")
    void shouldFailWithNullOrNegativeUnitPrice() {
        var productId = UUID.randomUUID();

        // When & Then - Null
        assertThatThrownBy(() -> OrderItem.create(
                productId, "Product", null, 1
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unit price must be positive");

        // When & Then - Negative
        var negativePrice = Money.ofBRL(-100);
        assertThatThrownBy(() -> OrderItem.create(
                productId, "Product", negativePrice, 1
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unit price must be positive");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    @DisplayName("Deve falhar com quantity inválida")
    void shouldFailWithInvalidQuantity(int invalidQuantity) {
        // Given
        var productId = UUID.randomUUID();
        var price = Money.ofBRL(100);

        // When & Then
        assertThatThrownBy(() -> OrderItem.create(
                productId, "Product", price, invalidQuantity
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be positive");
    }

    @Test
    @DisplayName("Deve falhar com quantity null")
    void shouldFailWithNullQuantity() {
        // Given
        var productId = UUID.randomUUID();
        var price = Money.ofBRL(100);

        // When & Then
        assertThatThrownBy(() -> OrderItem.create(
                productId, "Product", price, null
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be positive");
    }

    @Test
    @DisplayName("Deve verificar se é para um produto específico")
    void shouldCheckIfIsForSpecificProduct() {
        // Given
        var productId = UUID.randomUUID();
        var orderItem = OrderItem.create(productId, "Product", Money.ofBRL(100), 1);
        var otherProductId = UUID.randomUUID();

        // When & Then
        assertThat(orderItem.isForProduct(productId)).isTrue();
        assertThat(orderItem.isForProduct(otherProductId)).isFalse();
    }

    @Test
    @DisplayName("Deve calcular subtotal corretamente")
    void shouldCalculateSubtotalCorrectly() {
        // Given
        var unitPrice = Money.ofBRL(99.99);
        var quantity = 3;
        var orderItem = OrderItem.create(UUID.randomUUID(), "Product", unitPrice, quantity);

        // When
        var subtotal = orderItem.calculateSubtotal();

        // Then
        var expectedSubtotal = unitPrice.multiply(quantity);
        assertThat(subtotal).isEqualTo(expectedSubtotal);
        assertThat(subtotal).isEqualTo(orderItem.getTotalPrice()); // Deve ser igual ao totalPrice
    }

    @Test
    @DisplayName("Deve trimmar nome do produto")
    void shouldTrimProductName() {
        // Given
        var productNameWithSpaces = "  iPhone 15 Pro  ";

        // When
        var orderItem = OrderItem.create(
                UUID.randomUUID(),
                productNameWithSpaces,
                Money.ofBRL(100),
                1
        );

        // Then
        assertThat(orderItem.getProductName()).isEqualTo("iPhone 15 Pro");
    }

    @Test
    @DisplayName("Deve implementar equals baseado no ID")
    void shouldImplementEqualsBasedOnId() {
        // Given
        var orderItem1 = createValidOrderItem();
        var orderItem2 = createValidOrderItem(); // Diferente ID

        // When - Reconstruir orderItem1 com mesmo ID
        var orderItem1Copy = OrderItem.reconstruct(
                orderItem1.getId(),
                orderItem1.getProductId(),
                orderItem1.getProductName(),
                orderItem1.getUnitPrice(),
                orderItem1.getQuantity(),
                orderItem1.getTotalPrice()
        );

        // Then
        assertThat(orderItem1).isEqualTo(orderItem1Copy); // Mesmo ID
        assertThat(orderItem1).isNotEqualTo(orderItem2);   // IDs diferentes
        assertThat(orderItem1.hashCode()).isEqualTo(orderItem1Copy.hashCode());
    }

    @Test
    @DisplayName("Deve criar representação string correta")
    void shouldCreateCorrectStringRepresentation() {
        // Given
        var orderItem = createValidOrderItem();

        // When
        var stringRepresentation = orderItem.toString();

        // Then
        assertThat(stringRepresentation).contains(orderItem.getId().toString());
        assertThat(stringRepresentation).contains(orderItem.getProductName());
        assertThat(stringRepresentation).contains("quantity=" + orderItem.getQuantity());
        assertThat(stringRepresentation).contains("unitPrice=" + orderItem.getUnitPrice());
        assertThat(stringRepresentation).contains("total=" + orderItem.getTotalPrice());
    }

    @Test
    @DisplayName("Deve reconstruir item usando factory method")
    void shouldReconstructItemUsingFactoryMethod() {
        // Given
        var originalItem = createValidOrderItem();

        // When
        var reconstructedItem = OrderItem.reconstruct(
                originalItem.getId(),
                originalItem.getProductId(),
                "Novo Nome do Produto",
                Money.ofBRL(999.99),
                5,
                Money.ofBRL(4999.95)
        );

        // Then
        assertThat(reconstructedItem.getId()).isEqualTo(originalItem.getId());
        assertThat(reconstructedItem.getProductName()).isEqualTo("Novo Nome do Produto");
        assertThat(reconstructedItem.getUnitPrice()).isEqualTo(Money.ofBRL(999.99));
        assertThat(reconstructedItem.getQuantity()).isEqualTo(5);
        assertThat(reconstructedItem.getTotalPrice()).isEqualTo(Money.ofBRL(4999.95));
    }

    private OrderItem createValidOrderItem() {
        return OrderItem.create(
                UUID.randomUUID(),
                "iPhone 15 Pro",
                Money.ofBRL(7999.99),
                1
        );
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