package me.maximilianmilz.api.skeleton.infrastructure.persistence.entity

import jakarta.persistence.*
import me.maximilianmilz.api.skeleton.domain.model.Product
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * JPA entity for Product.
 */
@Entity
@Table(name = "products")
class ProductEntity(
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "description", nullable = false, length = 1000)
    val description: String,

    @Column(name = "price", nullable = false, columnDefinition = "NUMERIC")
    val price: Double,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    /**
     * Convert entity to domain model.
     */
    fun toDomain(): Product = Product(
        id = id,
        name = name,
        description = description,
        price = price,
        createdAt = createdAt,
        updatedAt = updatedAt
    )


    companion object {
        /**
         * Create entity from a domain model.
         */
        fun fromDomain(product: Product): ProductEntity = ProductEntity(
            id = product.id,
            name = product.name,
            description = product.description,
            price = product.price,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )

    }
}