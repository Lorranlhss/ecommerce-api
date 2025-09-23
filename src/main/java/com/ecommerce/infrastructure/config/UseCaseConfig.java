package com.ecommerce.infrastructure.config;

import com.ecommerce.application.usecases.customer.RegisterCustomerUseCase;
import com.ecommerce.application.usecases.order.AddItemToOrderUseCase;
import com.ecommerce.application.usecases.order.CancelOrderUseCase;
import com.ecommerce.application.usecases.order.ConfirmOrderUseCase;
import com.ecommerce.application.usecases.order.CreateOrderUseCase;
import com.ecommerce.application.usecases.order.FindOrdersUseCase;
import com.ecommerce.application.usecases.order.RemoveItemFromOrderUseCase;
import com.ecommerce.application.usecases.product.CreateProductUseCase;
import com.ecommerce.application.usecases.product.FindProductsUseCase;
import com.ecommerce.application.usecases.product.UpdateProductUseCase;
import com.ecommerce.domain.repositories.CustomerRepository;
import com.ecommerce.domain.repositories.OrderRepository;
import com.ecommerce.domain.repositories.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração dos beans dos Use Cases.
 * Responsável por injetar as dependências dos use cases.
 */
@Configuration
public class UseCaseConfig {

    // ===== PRODUCT USE CASES =====

    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepository productRepository) {
        return new CreateProductUseCase(productRepository);
    }

    @Bean
    public FindProductsUseCase findProductsUseCase(ProductRepository productRepository) {
        return new FindProductsUseCase(productRepository);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(ProductRepository productRepository) {
        return new UpdateProductUseCase(productRepository);
    }

    // ===== CUSTOMER USE CASES =====

    @Bean
    public RegisterCustomerUseCase registerCustomerUseCase(CustomerRepository customerRepository) {
        return new RegisterCustomerUseCase(customerRepository);
    }

    // ===== ORDER USE CASES =====

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepository orderRepository,
                                                 CustomerRepository customerRepository) {
        return new CreateOrderUseCase(orderRepository, customerRepository);
    }

    @Bean
    public AddItemToOrderUseCase addItemToOrderUseCase(OrderRepository orderRepository,
                                                       ProductRepository productRepository) {
        return new AddItemToOrderUseCase(orderRepository, productRepository);
    }

    @Bean
    public RemoveItemFromOrderUseCase removeItemFromOrderUseCase(OrderRepository orderRepository,
                                                                 ProductRepository productRepository) {
        return new RemoveItemFromOrderUseCase(orderRepository, productRepository);
    }

    @Bean
    public ConfirmOrderUseCase confirmOrderUseCase(OrderRepository orderRepository) {
        return new ConfirmOrderUseCase(orderRepository);
    }

    @Bean
    public CancelOrderUseCase cancelOrderUseCase(OrderRepository orderRepository,
                                                 ProductRepository productRepository) {
        return new CancelOrderUseCase(orderRepository, productRepository);
    }

    @Bean
    public FindOrdersUseCase findOrdersUseCase(OrderRepository orderRepository,
                                               CustomerRepository customerRepository) {
        return new FindOrdersUseCase(orderRepository, customerRepository);
    }
}