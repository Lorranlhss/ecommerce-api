package com.ecommerce.infrastructure.adapters.repositories;

import com.ecommerce.domain.entities.Customer;
import com.ecommerce.domain.repositories.CustomerRepository;
import com.ecommerce.domain.valueobjects.Email;
import com.ecommerce.infrastructure.adapters.repositories.entities.CustomerJpaEntity;
import com.ecommerce.infrastructure.adapters.repositories.jpa.CustomerJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação do repositório de clientes usando Spring Data JPA.
 */
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;

    public CustomerRepositoryImpl(CustomerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerJpaEntity entity = CustomerJpaEntity.fromDomain(customer);
        CustomerJpaEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(CustomerJpaEntity::toDomain);
    }

    @Override
    public Optional<Customer> findByEmail(Email email) {
        return jpaRepository.findByEmailIgnoreCase(email.getValue())
                .map(CustomerJpaEntity::toDomain);
    }

    @Override
    public List<Customer> findAllActive() {
        return jpaRepository.findByActiveTrueOrderByFirstNameAsc()
                .stream()
                .map(CustomerJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Customer> findByNameContaining(String name) {
        return jpaRepository.findByNameContaining(name)
                .stream()
                .map(CustomerJpaEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmailIgnoreCase(email.getValue());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long countActiveCustomers() {
        return jpaRepository.countByActiveTrue();
    }
}