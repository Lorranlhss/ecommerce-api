package com.ecommerce.application.usecases.customer;

import com.ecommerce.application.dto.CustomerDTO;
import com.ecommerce.application.dto.RegisterCustomerDTO;
import com.ecommerce.application.exceptions.ValidationException;
import com.ecommerce.domain.entities.Customer;
import com.ecommerce.domain.repositories.CustomerRepository;
import com.ecommerce.domain.valueobjects.Address;
import com.ecommerce.domain.valueobjects.Email;

/**
 * Use Case para registro de novos clientes.
 */
public class RegisterCustomerUseCase {

    private final CustomerRepository customerRepository;

    public RegisterCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO execute(RegisterCustomerDTO dto) {
        // Criar Email com validação
        Email email = Email.of(dto.email());

        // Validar se email já existe
        if (customerRepository.existsByEmail(email)) {
            throw ValidationException.duplicateEmail(dto.email());
        }

        // Criar Address
        Address address = dto.address().toDomain();

        // Criar Customer
        Customer customer = Customer.create(
                dto.firstName(),
                dto.lastName(),
                email,
                dto.phone(),
                address
        );

        // Persistir
        Customer savedCustomer = customerRepository.save(customer);

        // Retornar DTO
        return CustomerDTO.from(savedCustomer);
    }
}