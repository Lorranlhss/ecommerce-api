package com.ecommerce.application.dto;

import com.ecommerce.domain.entities.Customer;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de resposta para clientes.
 */
public record CustomerDTO(
        UUID id,
        String firstName,
        String lastName,
        String fullName,
        String email,
        String phone,
        AddressDTO address,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static CustomerDTO from(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getFullName(),
                customer.getEmail().getValue(),
                customer.getPhone(),
                AddressDTO.from(customer.getAddress()),
                customer.isActive(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}