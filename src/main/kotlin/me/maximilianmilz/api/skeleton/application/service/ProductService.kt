package me.maximilianmilz.api.skeleton.application.service

import me.maximilianmilz.api.skeleton.domain.model.Product
import me.maximilianmilz.api.skeleton.domain.repository.ProductRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * Service for handling product-related business logic.
 */
@Service
class ProductService(private val productRepository: ProductRepository) {

    /**
     * Get all products.
     * This operation is cached.
     *
     * @return List of all products
     */
    @Cacheable(value = ["products"])
    fun getAllProducts(): List<Product> = productRepository.findAll()

    /**
     * Get a product by its ID.
     * This operation is cached.
     *
     * @param id The product ID
     * @return The product if found, null otherwise
     */
    @Cacheable(value = ["products"], key = "#id")
    fun getProductById(id: UUID): Product? = productRepository.findById(id)

    /**
     * Create a new product.
     * This operation evicts the products cache.
     *
     * @param product The product to create
     * @return The created product
     */
    @CacheEvict(value = ["products"], allEntries = true)
    fun createProduct(product: Product): Product = productRepository.save(product)

    /**
     * Update an existing product.
     * This operation evicts both the all-products cache
     * and the specific product cache entry.
     *
     * @param id The product ID
     * @param product The updated product data
     * @return The updated product if found, null otherwise
     */
    @Caching(
        evict = [
            CacheEvict(value = ["products"], allEntries = true),
            CacheEvict(value = ["products"], key = "#id")
        ]
    )
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
     * This operation evicts both the all-products cache
     * and the specific product cache entry.
     *
     * @param id The product ID
     * @return true if deleted, false otherwise
     */
    @Caching(
        evict = [
            CacheEvict(value = ["products"], allEntries = true),
            CacheEvict(value = ["products"], key = "#id")
        ]
    )
    fun deleteProduct(id: UUID): Boolean = productRepository.deleteById(id)
}
