package com.ecommerce.application.exceptions;

import java.util.UUID;

/**
 * Exception para quando um recurso não é encontrado.
 */
public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }

    public NotFoundException(String resourceType, UUID id) {
        super("NOT_FOUND", String.format("%s with ID %s not found", resourceType, id));
    }

    public NotFoundException(String resourceType, String identifier) {
        super("NOT_FOUND", String.format("%s with identifier '%s' not found", resourceType, identifier));
    }

    public static NotFoundException product(UUID id) {
        return new NotFoundException("Product", id);
    }

    public static NotFoundException customer(UUID id) {
        return new NotFoundException("Customer", id);
    }

    public static NotFoundException customerByEmail(String email) {
        return new NotFoundException("Customer", email);
    }

    public static NotFoundException order(UUID id) {
        return new NotFoundException("Order", id);
    }
}