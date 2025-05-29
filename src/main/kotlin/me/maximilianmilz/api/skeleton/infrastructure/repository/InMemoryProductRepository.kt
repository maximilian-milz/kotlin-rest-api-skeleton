package me.maximilianmilz.api.skeleton.infrastructure.repository

import me.maximilianmilz.api.skeleton.domain.model.Product
import me.maximilianmilz.api.skeleton.domain.repository.ProductRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory implementation of ProductRepository.
 * This is a simple implementation for demonstration purposes.
 * In a real application, this would be replaced with a database-backed implementation.
 */
@Repository
class InMemoryProductRepository : ProductRepository {
    private val products = ConcurrentHashMap<UUID, Product>()

    override fun findAll(): List<Product> {
        return products.values.toList()
    }

    override fun findById(id: UUID): Product? {
        return products[id]
    }

    override fun save(product: Product): Product {
        val updatedProduct = if (products.containsKey(product.id)) {
            product.copy(updatedAt = LocalDateTime.now())
        } else {
            product
        }
        products[updatedProduct.id] = updatedProduct
        return updatedProduct
    }

    override fun deleteById(id: UUID): Boolean {
        return products.remove(id) != null
    }
}