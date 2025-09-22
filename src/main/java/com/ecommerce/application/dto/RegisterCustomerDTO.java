package com.ecommerce.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para registro de novos clientes.
 */
public record RegisterCustomerDTO(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        String phone,

        @NotNull(message = "Address is required")
        @Valid
        AddressDTO address
) {}