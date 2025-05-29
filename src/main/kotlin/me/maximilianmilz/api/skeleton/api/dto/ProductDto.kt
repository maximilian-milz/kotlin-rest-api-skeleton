package me.maximilianmilz.api.skeleton.api.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import me.maximilianmilz.api.skeleton.domain.model.Product
import java.time.LocalDateTime
import java.util.UUID

/**
 * DTO for Product responses.
 */
data class ProductResponseDto(
    val id: UUID,
    val name: String,
    val description: String,
    val price: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(product: Product): ProductResponseDto {
            return ProductResponseDto(
                id = product.id,
                name = product.name,
                description = product.description,
                price = product.price,
                createdAt = product.createdAt,
                updatedAt = product.updatedAt
            )
        }
    }
}

/**
 * DTO for creating a new Product.
 */
data class CreateProductRequestDto(
    @field:NotBlank(message = "Name is required")
    val name: String,
    
    @field:NotBlank(message = "Description is required")
    val description: String,
    
    @field:Positive(message = "Price must be positive")
    val price: Double
) {
    fun toDomain(): Product {
        return Product(
            name = name,
            description = description,
            price = price
        )
    }
}

/**
 * DTO for updating an existing Product.
 */
data class UpdateProductRequestDto(
    @field:NotBlank(message = "Name is required")
    val name: String,
    
    @field:NotBlank(message = "Description is required")
    val description: String,
    
    @field:Positive(message = "Price must be positive")
    val price: Double
) {
    fun toDomain(id: UUID): Product {
        return Product(
            id = id,
            name = name,
            description = description,
            price = price
        )
    }
}