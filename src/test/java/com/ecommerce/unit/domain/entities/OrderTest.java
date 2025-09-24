package com.ecommerce.unit.domain.entities;

import com.ecommerce.domain.entities.Order;
import com.ecommerce.domain.entities.OrderItem;
import com.ecommerce.domain.entities.OrderStatus;
import com.ecommerce.domain.entities.Product;
import com.ecommerce.domain.valueobjects.Address;
import com.ecommerce.domain.valueobjects.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Order Entity Tests")
class OrderTest {

    @Test
    @DisplayName("Deve criar pedido válido usando factory method")
    void shouldCreateValidOrderUsingFactoryMethod() {
        // Given
        var customerId = UUID.randomUUID();
        var deliveryAddress = createValidAddress();

        // When
        var order = Order.create(customerId, deliveryAddress);

        // Then
        assertThat(order.getId()).isNotNull();
        assertThat(order.getCustomerId()).isEqualTo(customerId);
        assertThat(order.getDeliveryAddress()).isEqualTo(deliveryAddress);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getItems()).isEmpty();
        assertThat(order.getTotalAmount()).isEqualTo(Money.zeroBRL());
        assertThat(order.getCreatedAt()).isNotNull();
        assertThat(order.getUpdatedAt()).isNotNull();
        assertThat(order.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(order.isEmpty()).isTrue();
        assertThat(order.canBeModified()).isTrue();
        assertThat(order.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("Deve falhar ao criar pedido com customerId null")
    void shouldFailWhenCreatingOrderWithNullCustomerId() {
        // Given
        var deliveryAddress = createValidAddress();

        // When & Then
        assertThatThrownBy(() -> Order.create(null, deliveryAddress))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Customer ID cannot be null");
    }

    @Test
    @DisplayName("Deve adicionar item ao pedido")
    void shouldAddItemToOrder() {
        // Given
        var order = createValidOrder();
        var orderItem = createValidOrderItem();

        // When
        order.addItem(orderItem);

        // Then
        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getItems().get(0)).isEqualTo(orderItem);
        assertThat(order.getTotalAmount()).isEqualTo(orderItem.getTotalPrice());
        assertThat(order.isEmpty()).isFalse();
        assertThat(order.getTotalItems()).isEqualTo(1);
        assertThat(order.getTotalQuantity()).isEqualTo(orderItem.getQuantity());
        assertThat(order.containsProduct(orderItem.getProductId())).isTrue();
    }

    @Test
    @DisplayName("Deve falhar ao adicionar item null")
    void shouldFailWhenAddingNullItem() {
        // Given
        var order = createValidOrder();

        // When & Then
        assertThatThrownBy(() -> order.addItem(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("OrderItem cannot be null");
    }

    @Test
    @DisplayName("Deve falhar ao adicionar produto duplicado")
    void shouldFailWhenAddingDuplicateProduct() {
        // Given
        var order = createValidOrder();
        var productId = UUID.randomUUID();
        var item1 = OrderItem.create(productId, "Product", Money.ofBRL(100), 1);
        var item2 = OrderItem.create(productId, "Product", Money.ofBRL(100), 2);

        // When
        order.addItem(item1);

        // Then
        assertThatThrownBy(() -> order.addItem(item2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Product already exists in order");
    }

    @Test
    @DisplayName("Deve remover item do pedido")
    void shouldRemoveItemFromOrder() {
        // Given
        var order = createValidOrder();
        var orderItem = createValidOrderItem();
        order.addItem(orderItem);
        assertThat(order.getItems()).hasSize(1);

        // When
        order.removeItem(orderItem.getId());

        // Then
        assertThat(order.getItems()).isEmpty();
        assertThat(order.getTotalAmount()).isEqualTo(Money.zeroBRL());
        assertThat(order.isEmpty()).isTrue();
        assertThat(order.containsProduct(orderItem.getProductId())).isFalse();
    }

    @Test
    @DisplayName("Deve falhar ao remover item inexistente")
    void shouldFailWhenRemovingNonExistentItem() {
        // Given
        var order = createValidOrder();
        var nonExistentItemId = UUID.randomUUID();

        // When & Then
        assertThatThrownBy(() -> order.removeItem(nonExistentItemId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Item not found in order");
    }

    @Test
    @DisplayName("Deve limpar todos os itens")
    void shouldClearAllItems() {
        // Given
        var order = createValidOrder();
        order.addItem(createValidOrderItem());
        order.addItem(createValidOrderItem2());
        assertThat(order.getItems()).hasSize(2);

        // When
        order.clearItems();

        // Then
        assertThat(order.getItems()).isEmpty();
        assertThat(order.getTotalAmount()).isEqualTo(Money.zeroBRL());
        assertThat(order.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve confirmar pedido com itens")
    void shouldConfirmOrderWithItems() {
        // Given
        var order = createValidOrder();
        order.addItem(createValidOrderItem());
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);

        // When
        order.confirm();

        // Then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(order.canBeModified()).isFalse();
        assertThat(order.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve falhar ao confirmar pedido vazio")
    void shouldFailWhenConfirmingEmptyOrder() {
        // Given
        var order = createValidOrder();
        assertThat(order.isEmpty()).isTrue();

        // When & Then
        assertThatThrownBy(() -> order.confirm())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Order must have at least one item");
    }

    @Test
    @DisplayName("Deve seguir fluxo completo de status")
    void shouldFollowCompleteStatusFlow() {
        // Given
        var order = createValidOrder();
        order.addItem(createValidOrderItem());

        // When & Then - Confirmar
        order.confirm();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CONFIRMED);

        // When & Then - Preparar
        order.startPreparing();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PREPARING);

        // When & Then - Enviar
        order.ship();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.SHIPPED);

        // When & Then - Entregar
        order.deliver();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
        assertThat(order.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("Deve cancelar pedido quando permitido")
    void shouldCancelOrderWhenAllowed() {
        // Given
        var order = createValidOrder();
        order.addItem(createValidOrderItem());

        // When & Then - Cancelar de PENDING
        order.cancel();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(order.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("Deve falhar ao cancelar pedido quando não permitido")
    void shouldFailWhenCancellingOrderWhenNotAllowed() {
        // Given
        var order = createValidOrder();
        order.addItem(createValidOrderItem());
        order.confirm();
        order.startPreparing();
        order.ship();
        order.deliver(); // DELIVERED não pode ser cancelado

        // When & Then
        assertThatThrownBy(() -> order.cancel())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Order cannot be cancelled in status");
    }

    @Test
    @DisplayName("Deve falhar ao modificar pedido confirmado")
    void shouldFailWhenModifyingConfirmedOrder() {
        // Given
        var order = createValidOrder();
        order.addItem(createValidOrderItem());
        order.confirm();
        assertThat(order.canBeModified()).isFalse();

        // When & Then - Tentar adicionar item
        assertThatThrownBy(() -> order.addItem(createValidOrderItem2()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Order cannot be modified in status");

        // When & Then - Tentar remover item
        var itemId = order.getItems().get(0).getId();
        assertThatThrownBy(() -> order.removeItem(itemId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Order cannot be modified in status");
    }

    @Test
    @DisplayName("Deve atualizar endereço de entrega")
    void shouldUpdateDeliveryAddress() {
        // Given
        var order = createValidOrder();
        var newAddress = createAlternativeAddress();
        var originalUpdatedAt = order.getUpdatedAt();

        // When
        order.updateDeliveryAddress(newAddress);

        // Then
        assertThat(order.getDeliveryAddress()).isEqualTo(newAddress);
        assertThat(order.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Deve falhar ao atualizar endereço para null")
    void shouldFailWhenUpdatingAddressToNull() {
        // Given
        var order = createValidOrder();

        // When & Then
        assertThatThrownBy(() -> order.updateDeliveryAddress(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Delivery address cannot be null");
    }

    @Test
    @DisplayName("Deve recalcular total ao adicionar múltiplos itens")
    void shouldRecalculateTotalWhenAddingMultipleItems() {
        // Given
        var order = createValidOrder();
        var item1 = OrderItem.create(UUID.randomUUID(), "Product 1", Money.ofBRL(100), 2); // 200
        var item2 = OrderItem.create(UUID.randomUUID(), "Product 2", Money.ofBRL(50), 3);  // 150

        // When
        order.addItem(item1);
        order.addItem(item2);

        // Then
        var expectedTotal = Money.ofBRL(350); // 200 + 150
        assertThat(order.getTotalAmount()).isEqualTo(expectedTotal);
        assertThat(order.getTotalItems()).isEqualTo(2);
        assertThat(order.getTotalQuantity()).isEqualTo(5); // 2 + 3
    }

    @Test
    @DisplayName("Deve falhar transição de status inválida")
    void shouldFailInvalidStatusTransition() {
        // Given
        var order = createValidOrder();
        order.addItem(createValidOrderItem());

        // When & Then - Tentar pular de PENDING para SHIPPED
        assertThatThrownBy(() -> order.ship())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot transition from PENDING to SHIPPED");
    }

    @Test
    @DisplayName("Deve implementar equals baseado no ID")
    void shouldImplementEqualsBasedOnId() {
        // Given
        var order1 = createValidOrder();
        var order2 = createValidOrder(); // Diferente ID

        // When - Reconstruir order1 com mesmo ID
        var order1Copy = Order.reconstruct(
                order1.getId(),
                order1.getCustomerId(),
                order1.getItems(),
                order1.getDeliveryAddress(),
                order1.getStatus(),
                order1.getTotalAmount(),
                order1.getCreatedAt(),
                order1.getUpdatedAt()
        );

        // Then
        assertThat(order1).isEqualTo(order1Copy); // Mesmo ID
        assertThat(order1).isNotEqualTo(order2);   // IDs diferentes
        assertThat(order1.hashCode()).isEqualTo(order1Copy.hashCode());
    }

    @Test
    @DisplayName("Deve criar representação string correta")
    void shouldCreateCorrectStringRepresentation() {
        // Given
        var order = createValidOrder();
        order.addItem(createValidOrderItem());

        // When
        var stringRepresentation = order.toString();

        // Then
        assertThat(stringRepresentation).contains(order.getId().toString());
        assertThat(stringRepresentation).contains(order.getCustomerId().toString());
        assertThat(stringRepresentation).contains("PENDING");
        assertThat(stringRepresentation).contains("items=1");
    }

    private Order createValidOrder() {
        return Order.create(UUID.randomUUID(), createValidAddress());
    }

    private OrderItem createValidOrderItem() {
        return OrderItem.create(
                UUID.randomUUID(),
                "iPhone 15 Pro",
                Money.ofBRL(7999.99),
                1
        );
    }

    private OrderItem createValidOrderItem2() {
        return OrderItem.create(
                UUID.randomUUID(),
                "MacBook Pro",
                Money.ofBRL(12999.99),
                1
        );
    }

    private Address createValidAddress() {
        return Address.builder()
                .street("Rua das Flores")
                .number("123")
                .complement("Apt 45")
                .neighborhood("Centro")
                .city("São Paulo")
                .state("SP")
                .zipCode("01234-567")
                .country("Brasil")
                .build();
    }

    private Address createAlternativeAddress() {
        return Address.builder()
                .street("Rua Nova")
                .number("456")
                .neighborhood("Copacabana")
                .city("Rio de Janeiro")
                .state("RJ")
                .zipCode("20000-000")
                .country("Brasil")
                .build();
    }
}