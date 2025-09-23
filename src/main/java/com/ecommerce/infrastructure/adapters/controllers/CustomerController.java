package com.ecommerce.infrastructure.adapters.controllers;

import com.ecommerce.application.dto.CustomerDTO;
import com.ecommerce.application.dto.RegisterCustomerDTO;
import com.ecommerce.application.exceptions.NotFoundException;
import com.ecommerce.application.usecases.customer.RegisterCustomerUseCase;
import com.ecommerce.domain.entities.Customer;
import com.ecommerce.domain.repositories.CustomerRepository;
import com.ecommerce.domain.valueobjects.Email;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

/**
 * Controller REST para operações com clientes.
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final RegisterCustomerUseCase registerCustomerUseCase;
    private final CustomerRepository customerRepository;

    public CustomerController(RegisterCustomerUseCase registerCustomerUseCase,
                              CustomerRepository customerRepository) {
        this.registerCustomerUseCase = registerCustomerUseCase;
        this.customerRepository = customerRepository;
    }

    /**
     * Registrar um novo cliente.
     */
    @PostMapping
    public ResponseEntity<CustomerDTO> registerCustomer(@Valid @RequestBody RegisterCustomerDTO dto) {
        CustomerDTO customer = registerCustomerUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    /**
     * Buscar cliente por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> NotFoundException.customer(id));
        return ResponseEntity.ok(CustomerDTO.from(customer));
    }

    /**
     * Buscar cliente por email.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        Email emailVo = Email.of(email);
        Customer customer = customerRepository.findByEmail(emailVo)
                .orElseThrow(() -> NotFoundException.customerByEmail(email));
        return ResponseEntity.ok(CustomerDTO.from(customer));
    }

    /**
     * Listar todos os clientes ativos.
     */
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllActiveCustomers() {
        List<CustomerDTO> customers = customerRepository.findAllActive()
                .stream()
                .map(CustomerDTO::from)
                .toList();
        return ResponseEntity.ok(customers);
    }

    /**
     * Buscar clientes por nome.
     */
    @GetMapping("/search")
    public ResponseEntity<List<CustomerDTO>> searchCustomersByName(@RequestParam String name) {
        List<CustomerDTO> customers = customerRepository.findByNameContaining(name)
                .stream()
                .map(CustomerDTO::from)
                .toList();
        return ResponseEntity.ok(customers);
    }
}