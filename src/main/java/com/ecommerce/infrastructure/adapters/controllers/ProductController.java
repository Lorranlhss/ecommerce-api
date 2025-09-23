package com.ecommerce.infrastructure.adapters.controllers;

import com.ecommerce.application.dto.CreateProductDTO;
import com.ecommerce.application.dto.ProductDTO;
import com.ecommerce.application.dto.UpdateProductDTO;
import com.ecommerce.application.usecases.product.CreateProductUseCase;
import com.ecommerce.application.usecases.product.FindProductsUseCase;
import com.ecommerce.application.usecases.product.UpdateProductUseCase;
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
 * Controller REST para operações com produtos.
 * Responsável apenas por coordenar chamadas aos use cases.
 */
@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "API para gerenciamento de produtos")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final FindProductsUseCase findProductsUseCase;
    private final UpdateProductUseCase updateProductUseCase;

    public ProductController(CreateProductUseCase createProductUseCase,
                             FindProductsUseCase findProductsUseCase,
                             UpdateProductUseCase updateProductUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.findProductsUseCase = findProductsUseCase;
        this.updateProductUseCase = updateProductUseCase;
    }

    /**
     * Criar um novo produto.
     */
    @PostMapping
    @Operation(summary = "Criar produto", description = "Cria um novo produto no catálogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "422", description = "Produto já existe")
    })
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody CreateProductDTO dto) {
        ProductDTO product = createProductUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    /**
     * Buscar produto por ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "ID do produto") @PathVariable UUID id) {
        ProductDTO product = findProductsUseCase.findById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Listar todos os produtos ativos.
     */
    @GetMapping
    @Operation(summary = "Listar produtos ativos", description = "Retorna todos os produtos ativos do catálogo")
    public ResponseEntity<List<ProductDTO>> getAllActiveProducts() {
        List<ProductDTO> products = findProductsUseCase.findAllActive();
        return ResponseEntity.ok(products);
    }

    /**
     * Buscar produtos por categoria.
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Buscar produtos por categoria", description = "Retorna produtos de uma categoria específica")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(
            @Parameter(description = "Nome da categoria") @PathVariable String category) {
        List<ProductDTO> products = findProductsUseCase.findByCategory(category);
        return ResponseEntity.ok(products);
    }

    /**
     * Buscar produtos por nome (busca parcial).
     */
    @GetMapping("/search")
    @Operation(summary = "Buscar produtos por nome", description = "Busca produtos que contenham o termo no nome")
    public ResponseEntity<List<ProductDTO>> searchProductsByName(
            @Parameter(description = "Termo de busca") @RequestParam String name) {
        List<ProductDTO> products = findProductsUseCase.findByNameContaining(name);
        return ResponseEntity.ok(products);
    }

    /**
     * Listar produtos disponíveis (ativos e com estoque).
     */
    @GetMapping("/available")
    @Operation(summary = "Listar produtos disponíveis", description = "Retorna produtos ativos e com estoque")
    public ResponseEntity<List<ProductDTO>> getAvailableProducts() {
        List<ProductDTO> products = findProductsUseCase.findAvailableProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Listar todas as categorias.
     */
    @GetMapping("/categories")
    @Operation(summary = "Listar categorias", description = "Retorna todas as categorias disponíveis")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = findProductsUseCase.findAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Atualizar um produto.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza as informações de um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "ID do produto") @PathVariable UUID id,
            @Valid @RequestBody UpdateProductDTO dto) {
        ProductDTO product = updateProductUseCase.execute(id, dto);
        return ResponseEntity.ok(product);
    }
}