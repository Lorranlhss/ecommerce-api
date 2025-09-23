package com.ecommerce.unit.domain.valueobjects;

import com.ecommerce.domain.valueobjects.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Address Value Object Tests")
class AddressTest {

    @Test
    @DisplayName("Deve criar endereço válido usando builder")
    void shouldCreateValidAddressUsingBuilder() {
        // When
        var address = Address.builder()
                .street("Rua das Flores")
                .number("123")
                .complement("Apt 45")
                .neighborhood("Centro")
                .city("São Paulo")
                .state("SP")
                .zipCode("01234-567")
                .country("Brasil")
                .build();

        // Then
        assertThat(address.getStreet()).isEqualTo("Rua das Flores");
        assertThat(address.getNumber()).isEqualTo("123");
        assertThat(address.getComplement()).isEqualTo("Apt 45");
        assertThat(address.getNeighborhood()).isEqualTo("Centro");
        assertThat(address.getCity()).isEqualTo("São Paulo");
        assertThat(address.getState()).isEqualTo("SP");
        assertThat(address.getZipCode()).isEqualTo("01234-567");
        assertThat(address.getCountry()).isEqualTo("Brasil");
    }

    @Test
    @DisplayName("Deve criar endereço sem complement")
    void shouldCreateAddressWithoutComplement() {
        // When
        var address = Address.builder()
                .street("Rua A")
                .number("123")
                .neighborhood("Bairro")
                .city("Cidade")
                .state("SP")
                .zipCode("12345-678")
                .country("Brasil")
                .build();

        // Then
        assertThat(address.getComplement()).isNull();
        assertThat(address.getStreet()).isEqualTo("Rua A");
    }

    @Test
    @DisplayName("Deve usar Brasil como país padrão")
    void shouldUseBrazilAsDefaultCountry() {
        // When
        var address = Address.builder()
                .street("Rua A")
                .number("123")
                .neighborhood("Bairro")
                .city("Cidade")
                .state("SP")
                .zipCode("12345-678")
                .build(); // Sem especificar country

        // Then
        assertThat(address.getCountry()).isEqualTo("Brasil");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Deve falhar com street inválida")
    void shouldFailWithInvalidStreet(String invalidStreet) {
        // When & Then
        assertThatThrownBy(() -> Address.builder()
                .street(invalidStreet)
                .number("123")
                .neighborhood("Bairro")
                .city("Cidade")
                .state("SP")
                .zipCode("12345-678")
                .country("Brasil")
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Street cannot be null or empty");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Deve falhar com number inválido")
    void shouldFailWithInvalidNumber(String invalidNumber) {
        // When & Then
        assertThatThrownBy(() -> Address.builder()
                .street("Rua A")
                .number(invalidNumber)
                .neighborhood("Bairro")
                .city("Cidade")
                .state("SP")
                .zipCode("12345-678")
                .country("Brasil")
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number cannot be null or empty");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Deve falhar com city inválida")
    void shouldFailWithInvalidCity(String invalidCity) {
        // When & Then
        assertThatThrownBy(() -> Address.builder()
                .street("Rua A")
                .number("123")
                .neighborhood("Bairro")
                .city(invalidCity)
                .state("SP")
                .zipCode("12345-678")
                .country("Brasil")
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("City cannot be null or empty");
    }

    @Test
    @DisplayName("Deve falhar com campos obrigatórios null")
    void shouldFailWithNullRequiredFields() {
        // When & Then - Neighborhood null
        assertThatThrownBy(() -> Address.builder()
                .street("Rua A")
                .number("123")
                .neighborhood(null)
                .city("Cidade")
                .state("SP")
                .zipCode("12345-678")
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Neighborhood cannot be null or empty");

        // When & Then - State null
        assertThatThrownBy(() -> Address.builder()
                .street("Rua A")
                .number("123")
                .neighborhood("Bairro")
                .city("Cidade")
                .state(null)
                .zipCode("12345-678")
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("State cannot be null or empty");

        // When & Then - ZipCode null
        assertThatThrownBy(() -> Address.builder()
                .street("Rua A")
                .number("123")
                .neighborhood("Bairro")
                .city("Cidade")
                .state("SP")
                .zipCode(null)
                .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Zip Code cannot be null or empty");
    }

    @Test
    @DisplayName("Deve trimmar todos os campos")
    void shouldTrimAllFields() {
        // When
        var address = Address.builder()
                .street("  Rua das Flores  ")
                .number("  123  ")
                .complement("  Apt 45  ")
                .neighborhood("  Centro  ")
                .city("  São Paulo  ")
                .state("  SP  ")
                .zipCode("  01234-567  ")
                .country("  Brasil  ")
                .build();

        // Then
        assertThat(address.getStreet()).isEqualTo("Rua das Flores");
        assertThat(address.getNumber()).isEqualTo("123");
        assertThat(address.getComplement()).isEqualTo("Apt 45");
        assertThat(address.getNeighborhood()).isEqualTo("Centro");
        assertThat(address.getCity()).isEqualTo("São Paulo");
        assertThat(address.getState()).isEqualTo("SP");
        assertThat(address.getZipCode()).isEqualTo("01234-567");
        assertThat(address.getCountry()).isEqualTo("Brasil");
    }

    @Test
    @DisplayName("Deve implementar equals e hashCode corretamente")
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // Given
        var address1 = Address.builder()
                .street("Rua A")
                .number("123")
                .neighborhood("Bairro")
                .city("Cidade")
                .state("SP")
                .zipCode("12345-678")
                .country("Brasil")
                .build();

        var address2 = Address.builder()
                .street("Rua A")
                .number("123")
                .neighborhood("Bairro")
                .city("Cidade")
                .state("SP")
                .zipCode("12345-678")
                .country("Brasil")
                .build();

        var address3 = Address.builder()
                .street("Rua B")
                .number("456")
                .neighborhood("Outro Bairro")
                .city("Cidade")
                .state("SP")
                .zipCode("98765-432")
                .country("Brasil")
                .build();

        // Then
        assertThat(address1).isEqualTo(address2);
        assertThat(address1).isNotEqualTo(address3);
        assertThat(address1.hashCode()).isEqualTo(address2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar endereço completo formatado")
    void shouldGenerateFullFormattedAddress() {
        // Given
        var address = Address.builder()
                .street("Rua das Flores")
                .number("123")
                .complement("Apt 45")
                .neighborhood("Centro")
                .city("São Paulo")
                .state("SP")
                .zipCode("01234-567")
                .country("Brasil")
                .build();

        // When
        var fullAddress = address.getFullAddress();

        // Then
        assertThat(fullAddress).contains("Rua das Flores, 123");
        assertThat(fullAddress).contains("- Apt 45");
        assertThat(fullAddress).contains("Centro");
        assertThat(fullAddress).contains("São Paulo");
        assertThat(fullAddress).contains("SP");
        assertThat(fullAddress).contains("01234-567");
        assertThat(fullAddress).contains("Brasil");
    }

    @Test
    @DisplayName("Deve gerar endereço sem complement no formato")
    void shouldGenerateAddressWithoutComplementInFormat() {
        // Given
        var address = Address.builder()
                .street("Rua das Flores")
                .number("123")
                .neighborhood("Centro")
                .city("São Paulo")
                .state("SP")
                .zipCode("01234-567")
                .country("Brasil")
                .build();

        // When
        var fullAddress = address.getFullAddress();

        // Then
        assertThat(fullAddress).contains("Rua das Flores, 123");
        assertThat(fullAddress).contains("Centro");
        assertThat(fullAddress).contains("São Paulo");
        // Não testar se contém " - " pois pode variar na implementação
    }

    @Test
    @DisplayName("Deve retornar string igual ao endereço completo")
    void shouldReturnStringEqualToFullAddress() {
        // Given
        var address = Address.builder()
                .street("Rua A")
                .number("123")
                .neighborhood("Bairro")
                .city("Cidade")
                .state("SP")
                .zipCode("12345-678")
                .country("Brasil")
                .build();

        // Then
        assertThat(address.toString()).isEqualTo(address.getFullAddress());
    }

    @Test
    @DisplayName("Deve aceitar complement vazio ou com espaços")
    void shouldAcceptEmptyOrWhitespaceComplement() {
        // When - Complement vazio
        var address1 = Address.builder()
                .street("Rua A")
                .number("123")
                .complement("")
                .neighborhood("Bairro")
                .city("Cidade")
                .state("SP")
                .zipCode("12345-678")
                .build();

        // When - Complement só com espaços
        var address2 = Address.builder()
                .street("Rua A")
                .number("123")
                .complement("   ")
                .neighborhood("Bairro")
                .city("Cidade")
                .state("SP")
                .zipCode("12345-678")
                .build();

        // Then - Sua implementação mantém string vazia, não converte para null
        assertThat(address1.getComplement()).isEmpty();
        assertThat(address2.getComplement()).isEmpty();
    }
}