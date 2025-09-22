package com.ecommerce.domain.repositories;

import com.ecommerce.domain.entities.Customer;
import com.ecommerce.domain.valueobjects.Email;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface que define as operações de persistência para clientes.
 */
public interface CustomerRepository {

    /**
     * Salva um cliente (criação ou atualização).
     */
    Customer save(Customer customer);

    /**
     * Busca um cliente por ID.
     */
    Optional<Customer> findById(UUID id);

    /**
     * Busca um cliente por email.
     */
    Optional<Customer> findByEmail(Email email);

    /**
     * Busca todos os clientes ativos.
     */
    List<Customer> findAllActive();

    /**
     * Busca clientes por nome (busca parcial, case-insensitive).
     */
    List<Customer> findByNameContaining(String name);

    /**
     * Verifica se existe um cliente com o email especificado.
     */
    boolean existsByEmail(Email email);

    /**
     * Remove um cliente por ID.
     */
    void deleteById(UUID id);

    /**
     * Conta o total de clientes ativos.
     */
    long countActiveCustomers();
}