package me.maximilianmilz.api.skeleton.application.service

import me.maximilianmilz.api.skeleton.domain.model.Product
import me.maximilianmilz.api.skeleton.domain.repository.ProductRepository
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * Service for handling product-related business logic.
 */
@Service
class ProductService(private val productRepository: ProductRepository) {

    /**
     * Get all products.
     *
     * @return List of all products
     */
    fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    /**
     * Get a product by its ID.
     *
     * @param id The product ID
     * @return The product if found, null otherwise
     */
    fun getProductById(id: UUID): Product? {
        return productRepository.findById(id)
    }

    /**
     * Create a new product.
     *
     * @param product The product to create
     * @return The created product
     */
    fun createProduct(product: Product): Product {
        return productRepository.save(product)
    }

    /**
     * Update an existing product.
     *
     * @param id The product ID
     * @param product The updated product data
     * @return The updated product if found, null otherwise
     */
    fun updateProduct(id: UUID, product: Product): Product? {
        val existingProduct = productRepository.findById(id) ?: return null
        val updatedProduct = product.copy(
            id = existingProduct.id,
            createdAt = existingProduct.createdAt
        )
        return productRepository.save(updatedProduct)
    }

    /**
     * Delete a product by its ID.
     *
     * @param id The product ID
     * @return true if deleted, false otherwise
     */
    fun deleteProduct(id: UUID): Boolean {
        return productRepository.deleteById(id)
    }
}