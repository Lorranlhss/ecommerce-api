package com.ecommerce.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO para criação de pedidos.
 */
public record CreateOrderDTO(
        @NotNull(message = "Customer ID is required")
        UUID customerId,

        @NotNull(message = "Delivery address is required")
        @Valid
        AddressDTO deliveryAddress
) {}