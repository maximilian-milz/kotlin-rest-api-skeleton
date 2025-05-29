package me.maximilianmilz.api.skeleton.infrastructure.persistence.repository

import me.maximilianmilz.api.skeleton.domain.model.Product
import me.maximilianmilz.api.skeleton.domain.repository.ProductRepository
import me.maximilianmilz.api.skeleton.infrastructure.persistence.entity.ProductEntity
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * JPA implementation of ProductRepository.
 */
@Repository
@Primary
class JpaProductRepository(private val productJpaRepository: ProductJpaRepository) : ProductRepository {

    /**
     * Find all products.
     *
     * @return List of all products
     */
    override fun findAll(): List<Product> {
        return productJpaRepository.findAll().map { it.toDomain() }
    }

    /**
     * Find a product by its ID.
     *
     * @param id The product ID
     * @return The product if found, null otherwise
     */
    override fun findById(id: UUID): Product? {
        return productJpaRepository.findById(id).map { it.toDomain() }.orElse(null)
    }

    /**
     * Save a product.
     *
     * @param product The product to save
     * @return The saved product
     */
    override fun save(product: Product): Product {
        val entity = ProductEntity.fromDomain(product)
        return productJpaRepository.save(entity).toDomain()
    }

    /**
     * Delete a product by its ID.
     *
     * @param id The product ID
     * @return true if deleted, false otherwise
     */
    override fun deleteById(id: UUID): Boolean {
        return if (productJpaRepository.existsById(id)) {
            productJpaRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}