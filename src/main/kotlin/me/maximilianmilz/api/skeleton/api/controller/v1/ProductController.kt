package me.maximilianmilz.api.skeleton.api.controller.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import me.maximilianmilz.api.skeleton.api.dto.CreateProductRequestDto
import me.maximilianmilz.api.skeleton.api.dto.ProductResponseDto
import me.maximilianmilz.api.skeleton.api.dto.UpdateProductRequestDto
import me.maximilianmilz.api.skeleton.api.exception.ResourceNotFoundException
import me.maximilianmilz.api.skeleton.application.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * REST controller for Product resource (API v1).
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Product management API")
class ProductController(private val productService: ProductService) {

    /**
     * Get all products.
     *
     * @return List of all products
     */
    @Operation(
        summary = "Get all products",
        description = "Retrieves a list of all available products"
    )
    @ApiResponse(
        responseCode = "200", description = "Successfully retrieved list of products", content = [Content(
            mediaType = "application/json", schema = Schema(implementation = Array<ProductResponseDto>::class)
        )]
    )
    @GetMapping
    fun getAllProducts(): ResponseEntity<List<ProductResponseDto>> {
        val products = productService.getAllProducts()
        return ResponseEntity.ok(products.map { ProductResponseDto.fromDomain(it) })
    }

    /**
     * Get a product by its ID.
     *
     * @param id The product ID
     * @return The product if found
     * @throws ResourceNotFoundException if the product is not found
     */
    @Operation(
        summary = "Get a product by ID",
        description = "Retrieves a specific product by its ID"
    )
    @ApiResponse(
        responseCode = "200", description = "Successfully retrieved product", content = [Content(
            mediaType = "application/json", schema = Schema(implementation = ProductResponseDto::class)
        )]
    )
    @ApiResponse(responseCode = "404", description = "Product not found", content = [Content()])
    @GetMapping("/{id}")
    fun getProductById(
        @Parameter(description = "ID of the product to retrieve", required = true)
        @PathVariable id: UUID
    ): ResponseEntity<ProductResponseDto> {
        val product = productService.getProductById(id)
            ?: throw ResourceNotFoundException("Product not found with id: $id")
        return ResponseEntity.ok(ProductResponseDto.fromDomain(product))
    }

    /**
     * Create a new product.
     *
     * @param createProductRequestDto The product data
     * @return The created product
     */
    @Operation(
        summary = "Create a new product",
        description = "Creates a new product with the provided data"
    )
    @ApiResponse(
        responseCode = "201", description = "Product successfully created", content = [Content(
            mediaType = "application/json", schema = Schema(implementation = ProductResponseDto::class)
        )]
    )
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = [Content()])
    @PostMapping
    fun createProduct(
        @Parameter(description = "Product data to create", required = true)
        @Valid @RequestBody createProductRequestDto: CreateProductRequestDto
    ): ResponseEntity<ProductResponseDto> {
        val product = productService.createProduct(createProductRequestDto.toDomain())
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ProductResponseDto.fromDomain(product))
    }

    /**
     * Update an existing product.
     *
     * @param id The product ID
     * @param updateProductRequestDto The updated product data
     * @return The updated product
     * @throws ResourceNotFoundException if the product is not found
     */
    @Operation(
        summary = "Update an existing product",
        description = "Updates an existing product with the provided data"
    )
    @ApiResponse(
        responseCode = "200", description = "Product successfully updated", content = [Content(
            mediaType = "application/json", schema = Schema(implementation = ProductResponseDto::class)
        )]
    )
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Product not found", content = [Content()])
    @PutMapping("/{id}")
    fun updateProduct(
        @Parameter(description = "ID of the product to update", required = true)
        @PathVariable id: UUID,
        @Parameter(description = "Updated product data", required = true)
        @Valid @RequestBody updateProductRequestDto: UpdateProductRequestDto
    ): ResponseEntity<ProductResponseDto> {
        val product = productService.updateProduct(id, updateProductRequestDto.toDomain(id))
            ?: throw ResourceNotFoundException("Product not found with id: $id")
        return ResponseEntity.ok(ProductResponseDto.fromDomain(product))
    }

    /**
     * Delete a product by its ID.
     *
     * @param id The product ID
     * @return No content if deleted successfully
     * @throws ResourceNotFoundException if the product is not found
     */
    @Operation(
        summary = "Delete a product",
        description = "Deletes a product by its ID"
    )
    @ApiResponse(responseCode = "204", description = "Product successfully deleted", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Product not found", content = [Content()])
    @DeleteMapping("/{id}")
    fun deleteProduct(
        @Parameter(description = "ID of the product to delete", required = true)
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        val deleted = productService.deleteProduct(id)
        if (!deleted) {
            throw ResourceNotFoundException("Product not found with id: $id")
        }
        return ResponseEntity.noContent().build()
    }
}
