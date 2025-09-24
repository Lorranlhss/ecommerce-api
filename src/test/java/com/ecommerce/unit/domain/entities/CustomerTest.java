package com.ecommerce.unit.domain.entities;

import com.ecommerce.domain.entities.Customer;
import com.ecommerce.domain.valueobjects.Address;
import com.ecommerce.domain.valueobjects.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Customer Entity Tests")
class CustomerTest {

    @Test
    @DisplayName("Deve criar cliente válido usando factory method")
    void shouldCreateValidCustomerUsingFactoryMethod() {
        // Given
        var firstName = "João";
        var lastName = "Silva";
        var email = Email.of("joao.silva@email.com");
        var phone = "+5511999999999";
        var address = createValidAddress();

        // When
        var customer = Customer.create(firstName, lastName, email, phone, address);

        // Then
        assertThat(customer.getId()).isNotNull();
        assertThat(customer.getFirstName()).isEqualTo(firstName);
        assertThat(customer.getLastName()).isEqualTo(lastName);
        assertThat(customer.getEmail()).isEqualTo(email);
        assertThat(customer.getPhone()).isEqualTo(phone);
        assertThat(customer.getAddress()).isEqualTo(address);
        assertThat(customer.isActive()).isTrue();
        assertThat(customer.getCreatedAt()).isNotNull();
        assertThat(customer.getUpdatedAt()).isNotNull();
        assertThat(customer.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve criar cliente com phone null (opcional)")
    void shouldCreateCustomerWithNullPhone() {
        // Given
        var email = Email.of("test@example.com");
        var address = createValidAddress();

        // When
        var customer = Customer.create("João", "Silva", email, null, address);

        // Then
        assertThat(customer.getPhone()).isNull();
        assertThat(customer.getFirstName()).isEqualTo("João");
    }

    @Test
    @DisplayName("Deve gerar nome completo corretamente")
    void shouldGenerateFullNameCorrectly() {
        // Given
        var customer = createValidCustomer();

        // When
        var fullName = customer.getFullName();

        // Then
        assertThat(fullName).isEqualTo("João Silva");
    }

    @Test
    @DisplayName("Deve verificar se pode fazer pedidos")
    void shouldCheckIfCanPlaceOrders() {
        // Given
        var customer = createValidCustomer();

        // When & Then - Cliente ativo pode fazer pedidos
        assertThat(customer.canPlaceOrders()).isTrue();

        // When - Desativar cliente
        customer.deactivate();

        // Then - Cliente inativo não pode fazer pedidos
        assertThat(customer.canPlaceOrders()).isFalse();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Deve falhar com firstName inválido")
    void shouldFailWithInvalidFirstName(String invalidFirstName) {
        // Given
        var email = Email.of("test@example.com");
        var address = createValidAddress();

        // When & Then
        assertThatThrownBy(() -> Customer.create(
                invalidFirstName, "Silva", email, "+5511999999999", address
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First name cannot be null or empty");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Deve falhar com lastName inválido")
    void shouldFailWithInvalidLastName(String invalidLastName) {
        // Given
        var email = Email.of("test@example.com");
        var address = createValidAddress();

        // When & Then
        assertThatThrownBy(() -> Customer.create(
                "João", invalidLastName, email, "+5511999999999", address
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last name cannot be null or empty");
    }

    @Test
    @DisplayName("Deve falhar com email null")
    void shouldFailWithNullEmail() {
        // Given
        var address = createValidAddress();

        // When & Then
        assertThatThrownBy(() -> Customer.create(
                "João", "Silva", null, "+5511999999999", address
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email cannot be null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("Deve falhar com phone vazio mas não null")
    void shouldFailWithEmptyPhone(String emptyPhone) {
        // Given
        var email = Email.of("test@example.com");
        var address = createValidAddress();

        // When & Then
        assertThatThrownBy(() -> Customer.create(
                "João", "Silva", email, emptyPhone, address
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Phone cannot be empty (but can be null)");
    }

    @Test
    @DisplayName("Deve atualizar informações completas do cliente")
    void shouldUpdateCompleteCustomerInformation() {
        // Given
        var customer = createValidCustomer();
        var originalUpdatedAt = customer.getUpdatedAt();

        var newFirstName = "José";
        var newLastName = "Santos";
        var newEmail = Email.of("jose.santos@email.com");
        var newPhone = "+5511888888888";
        var newAddress = createAlternativeAddress();

        // When
        customer.updateCustomerInfo(newFirstName, newLastName, newEmail, newPhone, newAddress);

        // Then
        assertThat(customer.getFirstName()).isEqualTo(newFirstName);
        assertThat(customer.getLastName()).isEqualTo(newLastName);
        assertThat(customer.getEmail()).isEqualTo(newEmail);
        assertThat(customer.getPhone()).isEqualTo(newPhone);
        assertThat(customer.getAddress()).isEqualTo(newAddress);
        assertThat(customer.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Deve atualizar endereço isoladamente")
    void shouldUpdateAddressOnly() {
        // Given
        var customer = createValidCustomer();
        var originalName = customer.getFirstName();
        var originalUpdatedAt = customer.getUpdatedAt();

        var newAddress = createAlternativeAddress();

        // When
        customer.updateAddress(newAddress);

        // Then
        assertThat(customer.getAddress()).isEqualTo(newAddress);
        assertThat(customer.getFirstName()).isEqualTo(originalName); // Outros campos não mudam
        assertThat(customer.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Deve atualizar email isoladamente")
    void shouldUpdateEmailOnly() {
        // Given
        var customer = createValidCustomer();
        var originalName = customer.getFirstName();
        var originalUpdatedAt = customer.getUpdatedAt();

        var newEmail = Email.of("novo.email@example.com");

        // When
        customer.updateEmail(newEmail);

        // Then
        assertThat(customer.getEmail()).isEqualTo(newEmail);
        assertThat(customer.getFirstName()).isEqualTo(originalName); // Outros campos não mudam
        assertThat(customer.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Deve falhar ao atualizar email com null")
    void shouldFailWhenUpdatingEmailWithNull() {
        // Given
        var customer = createValidCustomer();

        // When & Then
        assertThatThrownBy(() -> customer.updateEmail(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email cannot be null");
    }

    @Test
    @DisplayName("Deve atualizar phone isoladamente")
    void shouldUpdatePhoneOnly() {
        // Given
        var customer = createValidCustomer();
        var originalName = customer.getFirstName();
        var originalUpdatedAt = customer.getUpdatedAt();

        var newPhone = "+5511777777777";

        // When
        customer.updatePhone(newPhone);

        // Then
        assertThat(customer.getPhone()).isEqualTo(newPhone);
        assertThat(customer.getFirstName()).isEqualTo(originalName); // Outros campos não mudam
        assertThat(customer.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Deve aceitar phone null ao atualizar")
    void shouldAcceptNullPhoneWhenUpdating() {
        // Given
        var customer = createValidCustomer();

        // When
        customer.updatePhone(null);

        // Then
        assertThat(customer.getPhone()).isNull();
    }

    @Test
    @DisplayName("Deve converter phone vazio para null ao atualizar")
    void shouldConvertEmptyPhoneToNullWhenUpdating() {
        // Given
        var customer = createValidCustomer();

        // When
        customer.updatePhone("   ");

        // Then
        assertThat(customer.getPhone()).isNull();
    }

    @Test
    @DisplayName("Deve desativar cliente")
    void shouldDeactivateCustomer() {
        // Given
        var customer = createValidCustomer();
        assertThat(customer.isActive()).isTrue();

        // When
        customer.deactivate();

        // Then
        assertThat(customer.isActive()).isFalse();
        assertThat(customer.canPlaceOrders()).isFalse();
        assertThat(customer.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve ativar cliente")
    void shouldActivateCustomer() {
        // Given
        var customer = createValidCustomer();
        customer.deactivate();
        assertThat(customer.isActive()).isFalse();

        // When
        customer.activate();

        // Then
        assertThat(customer.isActive()).isTrue();
        assertThat(customer.canPlaceOrders()).isTrue();
        assertThat(customer.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve trimmar strings no input")
    void shouldTrimStringsInInput() {
        // Given
        var firstNameWithSpaces = "  João  ";
        var lastNameWithSpaces = "  Silva  ";
        var phoneWithSpaces = "  +5511999999999  ";

        // When
        var customer = Customer.create(
                firstNameWithSpaces,
                lastNameWithSpaces,
                Email.of("test@example.com"),
                phoneWithSpaces,
                createValidAddress()
        );

        // Then
        assertThat(customer.getFirstName()).isEqualTo("João");
        assertThat(customer.getLastName()).isEqualTo("Silva");
        assertThat(customer.getPhone()).isEqualTo("+5511999999999");
    }

    @Test
    @DisplayName("Deve manter consistência temporal")
    void shouldMaintainTemporalConsistency() {
        // Given
        var customer = createValidCustomer();
        var createdAt = customer.getCreatedAt();
        var initialUpdatedAt = customer.getUpdatedAt();

        // When - Fazer alguma alteração
        customer.updatePhone("+5511777777777");

        // Then
        assertThat(customer.getCreatedAt()).isEqualTo(createdAt); // CreatedAt não muda
        assertThat(customer.getUpdatedAt()).isAfter(initialUpdatedAt); // UpdatedAt muda
    }

    @Test
    @DisplayName("Deve implementar equals baseado no ID")
    void shouldImplementEqualsBasedOnId() {
        // Given
        var customer1 = createValidCustomer();
        var customer2 = createValidCustomer(); // Diferente ID

        // When recreating customer1 with same data
        var customer1Copy = Customer.reconstruct(
                customer1.getId(),
                customer1.getFirstName(),
                customer1.getLastName(),
                customer1.getEmail(),
                customer1.getPhone(),
                customer1.getAddress(),
                customer1.isActive(),
                customer1.getCreatedAt(),
                customer1.getUpdatedAt()
        );

        // Then
        assertThat(customer1).isEqualTo(customer1Copy); // Mesmo ID
        assertThat(customer1).isNotEqualTo(customer2);   // IDs diferentes
        assertThat(customer1.hashCode()).isEqualTo(customer1Copy.hashCode());
    }

    @Test
    @DisplayName("Deve criar representação string correta")
    void shouldCreateCorrectStringRepresentation() {
        // Given
        var customer = createValidCustomer();

        // When
        var stringRepresentation = customer.toString();

        // Then
        assertThat(stringRepresentation).contains(customer.getId().toString());
        assertThat(stringRepresentation).contains("João Silva");
        assertThat(stringRepresentation).contains("joao.silva@email.com");
        assertThat(stringRepresentation).contains("active=true");
    }

    @Test
    @DisplayName("Deve reconstruir cliente usando factory method")
    void shouldReconstructCustomerUsingFactoryMethod() {
        // Given
        var originalCustomer = createValidCustomer();

        // When
        var reconstructedCustomer = Customer.reconstruct(
                originalCustomer.getId(),
                "Novo Nome",
                "Novo Sobrenome",
                Email.of("novo@email.com"),
                "+5511888888888",
                createAlternativeAddress(),
                false, // Inativo
                originalCustomer.getCreatedAt(),
                originalCustomer.getUpdatedAt()
        );

        // Then
        assertThat(reconstructedCustomer.getId()).isEqualTo(originalCustomer.getId());
        assertThat(reconstructedCustomer.getFirstName()).isEqualTo("Novo Nome");
        assertThat(reconstructedCustomer.getLastName()).isEqualTo("Novo Sobrenome");
        assertThat(reconstructedCustomer.isActive()).isFalse();
        assertThat(reconstructedCustomer.getCreatedAt()).isEqualTo(originalCustomer.getCreatedAt());
    }

    private Customer createValidCustomer() {
        return Customer.create(
                "João",
                "Silva",
                Email.of("joao.silva@email.com"),
                "+5511999999999",
                createValidAddress()
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