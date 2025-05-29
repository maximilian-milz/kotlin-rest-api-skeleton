package me.maximilianmilz.api.skeleton.domain.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * Product entity representing a product in the system.
 */
data class Product(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String,
    val price: Double,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)