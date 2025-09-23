package com.ecommerce.infrastructure.adapters.controllers;

import com.ecommerce.application.dto.AddItemToOrderDTO;
import com.ecommerce.application.dto.CreateOrderDTO;
import com.ecommerce.application.dto.OrderDTO;
import com.ecommerce.application.usecases.order.AddItemToOrderUseCase;
import com.ecommerce.application.usecases.order.CancelOrderUseCase;
import com.ecommerce.application.usecases.order.ConfirmOrderUseCase;
import com.ecommerce.application.usecases.order.CreateOrderUseCase;
import com.ecommerce.application.usecases.order.FindOrdersUseCase;
import com.ecommerce.application.usecases.order.RemoveItemFromOrderUseCase;
import com.ecommerce.domain.entities.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

/**
 * Controller REST para operações com pedidos.
 * Gerencia todo o ciclo de vida dos pedidos: criação, modificação, confirmação e cancelamento.
 */
@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "API para gerenciamento de pedidos")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final AddItemToOrderUseCase addItemToOrderUseCase;
    private final RemoveItemFromOrderUseCase removeItemFromOrderUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final FindOrdersUseCase findOrdersUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           AddItemToOrderUseCase addItemToOrderUseCase,
                           RemoveItemFromOrderUseCase removeItemFromOrderUseCase,
                           ConfirmOrderUseCase confirmOrderUseCase,
                           CancelOrderUseCase cancelOrderUseCase,
                           FindOrdersUseCase findOrdersUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.addItemToOrderUseCase = addItemToOrderUseCase;
        this.removeItemFromOrderUseCase = removeItemFromOrderUseCase;
        this.confirmOrderUseCase = confirmOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.findOrdersUseCase = findOrdersUseCase;
    }

    /**
     * Criar um novo pedido.
     */
    @PostMapping
    @Operation(
            summary = "Criar pedido",
            description = "Cria um novo pedido vazio no status PENDING. Após criar, adicione itens usando o endpoint de itens."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "422", description = "Cliente inativo")
    })
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderDTO dto) {
        OrderDTO order = createOrderUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    /**
     * Buscar pedido por ID.
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar pedido por ID",
            description = "Retorna um pedido específico com todos os seus itens e informações"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<OrderDTO> getOrderById(
            @Parameter(description = "ID do pedido") @PathVariable UUID id) {
        OrderDTO order = findOrdersUseCase.findById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Adicionar item ao pedido.
     */
    @PostMapping("/{id}/items")
    @Operation(
            summary = "Adicionar item ao pedido",
            description = "Adiciona um produto ao pedido. Só é possível adicionar itens a pedidos no status PENDING."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Pedido ou produto não encontrado"),
            @ApiResponse(responseCode = "422", description = "Pedido não pode ser modificado ou estoque insuficiente")
    })
    public ResponseEntity<OrderDTO> addItemToOrder(
            @Parameter(description = "ID do pedido") @PathVariable UUID id,
            @Valid @RequestBody AddItemToOrderDTO dto) {
        OrderDTO order = addItemToOrderUseCase.execute(id, dto);
        return ResponseEntity.ok(order);
    }

    /**
     * Remover item do pedido.
     */
    @DeleteMapping("/{orderId}/items/{itemId}")
    @Operation(
            summary = "Remover item do pedido",
            description = "Remove um item específico do pedido e devolve o estoque ao produto. Só é possível remover itens de pedidos no status PENDING."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido ou item não encontrado"),
            @ApiResponse(responseCode = "422", description = "Pedido não pode ser modificado")
    })
    public ResponseEntity<OrderDTO> removeItemFromOrder(
            @Parameter(description = "ID do pedido") @PathVariable UUID orderId,
            @Parameter(description = "ID do item a ser removido") @PathVariable UUID itemId) {
        OrderDTO order = removeItemFromOrderUseCase.execute(orderId, itemId);
        return ResponseEntity.ok(order);
    }

    /**
     * Confirmar pedido.
     */
    @PutMapping("/{id}/confirm")
    @Operation(
            summary = "Confirmar pedido",
            description = "Confirma o pedido, alterando seu status de PENDING para CONFIRMED. Após confirmado, não é mais possível modificar itens."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido confirmado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "422", description = "Pedido não pode ser confirmado (vazio ou status inválido)")
    })
    public ResponseEntity<OrderDTO> confirmOrder(
            @Parameter(description = "ID do pedido") @PathVariable UUID id) {
        OrderDTO order = confirmOrderUseCase.execute(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Cancelar pedido.
     */
    @PutMapping("/{id}/cancel")
    @Operation(
            summary = "Cancelar pedido",
            description = "Cancela o pedido e devolve todo o estoque dos produtos. Só é possível cancelar pedidos nos status PENDING, CONFIRMED ou PREPARING."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "422", description = "Pedido não pode ser cancelado no status atual")
    })
    public ResponseEntity<OrderDTO> cancelOrder(
            @Parameter(description = "ID do pedido") @PathVariable UUID id) {
        OrderDTO order = cancelOrderUseCase.execute(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Buscar pedidos de um cliente.
     */
    @GetMapping("/customer/{customerId}")
    @Operation(
            summary = "Buscar pedidos do cliente",
            description = "Retorna todos os pedidos de um cliente específico, ordenados por data de criação (mais recente primeiro)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(
            @Parameter(description = "ID do cliente") @PathVariable UUID customerId) {
        List<OrderDTO> orders = findOrdersUseCase.findByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Buscar pedidos por status.
     */
    @GetMapping("/status/{status}")
    @Operation(
            summary = "Buscar pedidos por status",
            description = "Retorna todos os pedidos com um status específico. Status possíveis: PENDING, CONFIRMED, PREPARING, SHIPPED, DELIVERED, CANCELLED"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Status inválido")
    })
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(
            @Parameter(
                    description = "Status do pedido",
                    example = "PENDING"
            ) @PathVariable OrderStatus status) {
        List<OrderDTO> orders = findOrdersUseCase.findByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Buscar pedidos de um cliente com status específico.
     */
    @GetMapping("/customer/{customerId}/status/{status}")
    @Operation(
            summary = "Buscar pedidos do cliente por status",
            description = "Retorna pedidos de um cliente específico que estejam em um determinado status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Status inválido")
    })
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerAndStatus(
            @Parameter(description = "ID do cliente") @PathVariable UUID customerId,
            @Parameter(description = "Status do pedido") @PathVariable OrderStatus status) {
        List<OrderDTO> orders = findOrdersUseCase.findByCustomerIdAndStatus(customerId, status);
        return ResponseEntity.ok(orders);
    }
}