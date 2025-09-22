package com.ecommerce.application.dto;

import com.ecommerce.domain.valueobjects.Money;
import java.math.BigDecimal;

/**
 * DTO para transferência de dados de valores monetários.
 */
public record MoneyDTO(
        BigDecimal amount,
        String currency
) {

    public static MoneyDTO from(Money money) {
        if (money == null) {
            return null;
        }
        return new MoneyDTO(money.getAmount(), money.getCurrency());
    }

    public Money toDomain() {
        return Money.of(amount, currency);
    }

    public static MoneyDTO ofBRL(BigDecimal amount) {
        return new MoneyDTO(amount, "BRL");
    }

    public static MoneyDTO ofBRL(double amount) {
        return new MoneyDTO(BigDecimal.valueOf(amount), "BRL");
    }
}