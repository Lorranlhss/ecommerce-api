package com.ecommerce.domain.entities;

import com.ecommerce.domain.valueobjects.Address;
import com.ecommerce.domain.valueobjects.Email;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade que representa um cliente.
 * Contém informações pessoais e comportamentos relacionados.
 */
public class Customer {

    private final UUID id;
    private String firstName;
    private String lastName;
    private Email email;
    private String phone;
    private Address address;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Construtor para criação de novos clientes
    private Customer(String firstName, String lastName, Email email,
                     String phone, Address address) {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;

        updateCustomerInfo(firstName, lastName, email, phone, address);
    }

    // Construtor para reconstrução (ex: vindo do banco de dados)
    private Customer(UUID id, String firstName, String lastName, Email email,
                     String phone, Address address, boolean active,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory methods
    public static Customer create(String firstName, String lastName, Email email,
                                  String phone, Address address) {
        return new Customer(firstName, lastName, email, phone, address);
    }

    public static Customer reconstruct(UUID id, String firstName, String lastName,
                                       Email email, String phone, Address address,
                                       boolean active, LocalDateTime createdAt,
                                       LocalDateTime updatedAt) {
        return new Customer(id, firstName, lastName, email, phone, address,
                active, createdAt, updatedAt);
    }

    // Métodos de negócio
    public void updateCustomerInfo(String firstName, String lastName, Email email,
                                   String phone, Address address) {
        validateCustomerData(firstName, lastName, email, phone);

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email;
        this.phone = phone != null ? phone.trim() : null;
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateEmail(Email newEmail) {
        if (newEmail == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateAddress(Address newAddress) {
        this.address = newAddress;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePhone(String newPhone) {
        if (newPhone != null && newPhone.trim().isEmpty()) {
            newPhone = null;
        }
        this.phone = newPhone;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean canPlaceOrders() {
        return active;
    }

    private void validateCustomerData(String firstName, String lastName, Email email, String phone) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        // Phone é opcional, mas se fornecido não pode ser vazio
        if (phone != null && phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be empty (but can be null)");
        }
    }

    // Getters
    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Email getEmail() { return email; }
    public String getPhone() { return phone; }
    public Address getAddress() { return address; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Customer customer = (Customer) obj;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Customer{id=%s, name='%s', email='%s', active=%s}",
                id, getFullName(), email, active);
    }
}