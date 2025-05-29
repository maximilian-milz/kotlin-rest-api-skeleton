package me.maximilianmilz.api.skeleton.application.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.maximilianmilz.api.skeleton.domain.model.Product
import me.maximilianmilz.api.skeleton.domain.repository.ProductRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * Service for handling product-related business logic.
 * Implements caching and asynchronous processing with Kotlin Coroutines.
 */
@Service
class ProductService(private val productRepository: ProductRepository) {

    /**
     * Get all products.
     * This operation is cached and executed asynchronously.
     *
     * @return List of all products
     */
    @Cacheable(value = ["products"])
    suspend fun getAllProducts(): List<Product> = withContext(Dispatchers.IO) {
        productRepository.findAll()
    }

    /**
     * Get a product by its ID.
     * This operation is cached and executed asynchronously.
     *
     * @param id The product ID
     * @return The product if found, null otherwise
     */
    @Cacheable(value = ["products"], key = "#id")
    suspend fun getProductById(id: UUID): Product? = withContext(Dispatchers.IO) {
        productRepository.findById(id)
    }

    /**
     * Create a new product.
     * This operation is executed asynchronously and evicts the products cache.
     *
     * @param product The product to create
     * @return The created product
     */
    @CacheEvict(value = ["products"], allEntries = true)
    suspend fun createProduct(product: Product): Product = withContext(Dispatchers.IO) {
        productRepository.save(product)
    }

    /**
     * Update an existing product.
     * This operation is executed asynchronously and evicts both the all products cache
     * and the specific product cache entry.
     *
     * @param id The product ID
     * @param product The updated product data
     * @return The updated product if found, null otherwise
     */
    @Caching(evict = [
        CacheEvict(value = ["products"], allEntries = true),
        CacheEvict(value = ["products"], key = "#id")
    ])
    suspend fun updateProduct(id: UUID, product: Product): Product? = withContext(Dispatchers.IO) {
        val existingProduct = productRepository.findById(id) ?: return@withContext null
        val updatedProduct = product.copy(
            id = existingProduct.id,
            createdAt = existingProduct.createdAt
        )
        productRepository.save(updatedProduct)
    }

    /**
     * Delete a product by its ID.
     * This operation is executed asynchronously and evicts both the all products cache
     * and the specific product cache entry.
     *
     * @param id The product ID
     * @return true if deleted, false otherwise
     */
    @Caching(evict = [
        CacheEvict(value = ["products"], allEntries = true),
        CacheEvict(value = ["products"], key = "#id")
    ])
    suspend fun deleteProduct(id: UUID): Boolean = withContext(Dispatchers.IO) {
        productRepository.deleteById(id)
    }
}
