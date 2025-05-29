package me.maximilianmilz.api.skeleton.api.controller.v1

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
class ProductController(private val productService: ProductService) {

    /**
     * Get all products.
     *
     * @return List of all products
     */
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
    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: UUID): ResponseEntity<ProductResponseDto> {
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
    @PostMapping
    fun createProduct(
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
    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: UUID,
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
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: UUID): ResponseEntity<Void> {
        val deleted = productService.deleteProduct(id)
        if (!deleted) {
            throw ResourceNotFoundException("Product not found with id: $id")
        }
        return ResponseEntity.noContent().build()
    }
}