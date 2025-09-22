package com.ecommerce.application.exceptions;

/**
 * Exception para erros de validação.
 */
public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super("VALIDATION_ERROR", message);
    }

    public ValidationException(String message, Throwable cause) {
        super("VALIDATION_ERROR", message, cause);
    }

    public static ValidationException duplicateEmail(String email) {
        return new ValidationException("Email already exists: " + email);
    }

    public static ValidationException duplicateProductName(String name) {
        return new ValidationException("Product name already exists: " + name);
    }

    public static ValidationException insufficientStock(String productName, int available, int requested) {
        return new ValidationException(
                String.format("Insufficient stock for product '%s'. Available: %d, Requested: %d",
                        productName, available, requested)
        );
    }

    public static ValidationException inactiveProduct(String productName) {
        return new ValidationException("Product is inactive: " + productName);
    }

    public static ValidationException invalidOrderStatus(String currentStatus, String requestedStatus) {
        return new ValidationException(
                String.format("Cannot transition from status '%s' to '%s'", currentStatus, requestedStatus)
        );
    }
}