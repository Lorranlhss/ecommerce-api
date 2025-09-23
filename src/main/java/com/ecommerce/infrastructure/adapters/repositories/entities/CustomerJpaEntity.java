package com.ecommerce.infrastructure.adapters.repositories.entities;

import com.ecommerce.domain.entities.Customer;
import com.ecommerce.domain.valueobjects.Address;
import com.ecommerce.domain.valueobjects.Email;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade JPA para persistência de clientes.
 */
@Entity
@Table(name = "customers")
public class CustomerJpaEntity {

    @Id
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(name = "address_street")
    private String addressStreet;

    @Column(name = "address_number")
    private String addressNumber;

    @Column(name = "address_complement")
    private String addressComplement;

    @Column(name = "address_neighborhood")
    private String addressNeighborhood;

    @Column(name = "address_city")
    private String addressCity;

    @Column(name = "address_state")
    private String addressState;

    @Column(name = "address_zip_code")
    private String addressZipCode;

    @Column(name = "address_country")
    private String addressCountry;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Construtor padrão para JPA
    protected CustomerJpaEntity() {}

    // Factory method para converter de Domain Entity
    public static CustomerJpaEntity fromDomain(Customer customer) {
        CustomerJpaEntity entity = new CustomerJpaEntity();
        entity.id = customer.getId();
        entity.firstName = customer.getFirstName();
        entity.lastName = customer.getLastName();
        entity.email = customer.getEmail().getValue();
        entity.phone = customer.getPhone();
        entity.active = customer.isActive();
        entity.createdAt = customer.getCreatedAt();
        entity.updatedAt = customer.getUpdatedAt();

        // Mapear endereço
        if (customer.getAddress() != null) {
            Address address = customer.getAddress();
            entity.addressStreet = address.getStreet();
            entity.addressNumber = address.getNumber();
            entity.addressComplement = address.getComplement();
            entity.addressNeighborhood = address.getNeighborhood();
            entity.addressCity = address.getCity();
            entity.addressState = address.getState();
            entity.addressZipCode = address.getZipCode();
            entity.addressCountry = address.getCountry();
        }

        return entity;
    }

    // Método para converter para Domain Entity
    public Customer toDomain() {
        Email emailVo = Email.of(email);

        Address address = null;
        if (addressStreet != null) {
            address = Address.builder()
                    .street(addressStreet)
                    .number(addressNumber)
                    .complement(addressComplement)
                    .neighborhood(addressNeighborhood)
                    .city(addressCity)
                    .state(addressState)
                    .zipCode(addressZipCode)
                    .country(addressCountry)
                    .build();
        }

        return Customer.reconstruct(
                id, firstName, lastName, emailVo, phone,
                address, active, createdAt, updatedAt
        );
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Getters e Setters do endereço
    public String getAddressStreet() { return addressStreet; }
    public void setAddressStreet(String addressStreet) { this.addressStreet = addressStreet; }

    public String getAddressNumber() { return addressNumber; }
    public void setAddressNumber(String addressNumber) { this.addressNumber = addressNumber; }

    public String getAddressComplement() { return addressComplement; }
    public void setAddressComplement(String addressComplement) { this.addressComplement = addressComplement; }

    public String getAddressNeighborhood() { return addressNeighborhood; }
    public void setAddressNeighborhood(String addressNeighborhood) { this.addressNeighborhood = addressNeighborhood; }

    public String getAddressCity() { return addressCity; }
    public void setAddressCity(String addressCity) { this.addressCity = addressCity; }

    public String getAddressState() { return addressState; }
    public void setAddressState(String addressState) { this.addressState = addressState; }

    public String getAddressZipCode() { return addressZipCode; }
    public void setAddressZipCode(String addressZipCode) { this.addressZipCode = addressZipCode; }

    public String getAddressCountry() { return addressCountry; }
    public void setAddressCountry(String addressCountry) { this.addressCountry = addressCountry; }
}