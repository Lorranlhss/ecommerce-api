package com.ecommerce.unit.domain.valueobjects;

import com.ecommerce.domain.valueobjects.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Money Value Object Tests")
class MoneyTest {

    @Test
    @DisplayName("Deve criar Money válido usando factory method")
    void shouldCreateValidMoneyUsingFactoryMethod() {
        // Given
        var amount = new BigDecimal("99.99");
        var currency = "brl"; // Testando lowercase

        // When
        var money = Money.of(amount, currency);

        // Then
        assertThat(money.getAmount()).isEqualTo(new BigDecimal("99.99"));
        assertThat(money.getCurrency()).isEqualTo("BRL"); // Deve converter para uppercase
    }

    @Test
    @DisplayName("Deve criar Money usando factory methods específicos")
    void shouldCreateMoneyUsingSpecificFactoryMethods() {
        // Money.ofBRL
        var moneyBRL = Money.ofBRL(150.75);
        assertThat(moneyBRL.getCurrency()).isEqualTo("BRL");
        assertThat(moneyBRL.getAmount()).isEqualTo(new BigDecimal("150.75"));

        // Money.zero
        var zeroUSD = Money.zero("USD");
        assertThat(zeroUSD.isZero()).isTrue();
        assertThat(zeroUSD.getCurrency()).isEqualTo("USD");

        // Money.zeroBRL
        var zeroBRL = Money.zeroBRL();
        assertThat(zeroBRL.isZero()).isTrue();
        assertThat(zeroBRL.getCurrency()).isEqualTo("BRL");
    }

    @Test
    @DisplayName("Deve falhar com amount null")
    void shouldFailWithNullAmount() {
        // When & Then
        assertThatThrownBy(() -> Money.of(null, "BRL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount cannot be null");
    }

    @Test
    @DisplayName("Deve falhar com currency null ou vazia")
    void shouldFailWithNullOrEmptyCurrency() {
        var amount = new BigDecimal("99.99");

        // Null currency
        assertThatThrownBy(() -> Money.of(amount, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Currency cannot be null or empty");

        // Empty currency
        assertThatThrownBy(() -> Money.of(amount, ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Currency cannot be null or empty");
    }

    @Test
    @DisplayName("Deve aceitar valores negativos e zero")
    void shouldAcceptNegativeAndZeroValues() {
        // Zero
        var zeroMoney = Money.of(BigDecimal.ZERO, "BRL");
        assertThat(zeroMoney.isZero()).isTrue();
        assertThat(zeroMoney.isPositive()).isFalse();
        assertThat(zeroMoney.isNegative()).isFalse();

        // Positivo
        var positiveMoney = Money.of(new BigDecimal("10.00"), "BRL");
        assertThat(positiveMoney.isPositive()).isTrue();
        assertThat(positiveMoney.isZero()).isFalse();
        assertThat(positiveMoney.isNegative()).isFalse();

        // Negativo
        var negativeMoney = Money.of(new BigDecimal("-5.00"), "BRL");
        assertThat(negativeMoney.isNegative()).isTrue();
        assertThat(negativeMoney.isPositive()).isFalse();
        assertThat(negativeMoney.isZero()).isFalse();
    }

    @Test
    @DisplayName("Deve somar Money corretamente")
    void shouldAddMoneyCorrectly() {
        // Given
        var money1 = Money.of(new BigDecimal("10.00"), "BRL");
        var money2 = Money.of(new BigDecimal("5.50"), "BRL");

        // When
        var result = money1.add(money2);

        // Then
        assertThat(result.getAmount()).isEqualTo(new BigDecimal("15.50"));
        assertThat(result.getCurrency()).isEqualTo("BRL");
    }

    @Test
    @DisplayName("Deve subtrair Money corretamente")
    void shouldSubtractMoneyCorrectly() {
        // Given
        var money1 = Money.of(new BigDecimal("20.00"), "BRL");
        var money2 = Money.of(new BigDecimal("8.50"), "BRL");

        // When
        var result = money1.subtract(money2);

        // Then
        assertThat(result.getAmount()).isEqualTo(new BigDecimal("11.50"));
        assertThat(result.getCurrency()).isEqualTo("BRL");
    }

    @Test
    @DisplayName("Deve falhar ao operar Money com currencies diferentes")
    void shouldFailWhenOperatingDifferentCurrencies() {
        // Given
        var moneyBRL = Money.of(new BigDecimal("10.00"), "BRL");
        var moneyUSD = Money.of(new BigDecimal("5.00"), "USD");

        // When & Then - Add
        assertThatThrownBy(() -> moneyBRL.add(moneyUSD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot operate on different currencies");

        // When & Then - Subtract
        assertThatThrownBy(() -> moneyBRL.subtract(moneyUSD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot operate on different currencies");

        // When & Then - Comparação
        assertThatThrownBy(() -> moneyBRL.isGreaterThan(moneyUSD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot operate on different currencies");
    }

    @Test
    @DisplayName("Deve multiplicar Money corretamente")
    void shouldMultiplyMoneyCorrectly() {
        // Given
        var money = Money.of(new BigDecimal("10.00"), "BRL");

        // When & Then - Por inteiro
        var result1 = money.multiply(3);
        assertThat(result1.getAmount()).isEqualTo(new BigDecimal("30.00"));

        // When & Then - Por double
        var result2 = money.multiply(2.5);
        assertThat(result2.getAmount()).isEqualTo(new BigDecimal("25.00"));

        // When & Then - Por BigDecimal
        var result3 = money.multiply(new BigDecimal("1.5"));
        assertThat(result3.getAmount()).isEqualTo(new BigDecimal("15.00"));
    }

    @Test
    @DisplayName("Deve comparar Money corretamente")
    void shouldCompareMoneyCorrectly() {
        // Given
        var money10 = Money.of(new BigDecimal("10.00"), "BRL");
        var money20 = Money.of(new BigDecimal("20.00"), "BRL");
        var money10Copy = Money.of(new BigDecimal("10.00"), "BRL");

        // Then - Comparações
        assertThat(money20.isGreaterThan(money10)).isTrue();
        assertThat(money10.isLessThan(money20)).isTrue();
        assertThat(money10.isGreaterThan(money20)).isFalse();

        // Then - Equals e hashCode
        assertThat(money10).isEqualTo(money10Copy);
        assertThat(money10).isNotEqualTo(money20);
        assertThat(money10.hashCode()).isEqualTo(money10Copy.hashCode());
    }

    @Test
    @DisplayName("Deve arredondar valores corretamente")
    void shouldRoundValuesCorrectly() {
        // Given
        var money = Money.of(new BigDecimal("10.999"), "BRL");

        // Then
        assertThat(money.getAmount()).isEqualTo(new BigDecimal("11.00"));

        // Given
        var money2 = Money.of(new BigDecimal("10.994"), "BRL");

        // Then
        assertThat(money2.getAmount()).isEqualTo(new BigDecimal("10.99"));
    }
}