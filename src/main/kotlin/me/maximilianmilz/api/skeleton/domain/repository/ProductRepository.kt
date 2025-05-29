package me.maximilianmilz.api.skeleton.domain.repository

import me.maximilianmilz.api.skeleton.domain.model.Product
import java.util.UUID

/**
 * Repository interface for Product entity.
 */
interface ProductRepository {
    /**
     * Find all products.
     *
     * @return List of all products
     */
    fun findAll(): List<Product>

    /**
     * Find a product by its ID.
     *
     * @param id The product ID
     * @return The product if found, null otherwise
     */
    fun findById(id: UUID): Product?

    /**
     * Save a product.
     *
     * @param product The product to save
     * @return The saved product
     */
    fun save(product: Product): Product

    /**
     * Delete a product by its ID.
     *
     * @param id The product ID
     * @return true if deleted, false otherwise
     */
    fun deleteById(id: UUID): Boolean
}