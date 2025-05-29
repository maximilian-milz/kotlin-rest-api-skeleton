package me.maximilianmilz.api.skeleton.infrastructure.persistence.repository

import me.maximilianmilz.api.skeleton.infrastructure.persistence.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * JPA repository for ProductEntity.
 */
@Repository
interface ProductJpaRepository : JpaRepository<ProductEntity, UUID>