package me.maximilianmilz.api.skeleton.infrastructure.persistence.repository

import me.maximilianmilz.api.skeleton.domain.model.Product
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaProductRepository::class)
@ActiveProfiles("test")
class JpaProductRepositoryTest {

    companion object {
        @Container
        private val postgresContainer = PostgreSQLContainer<Nothing>("postgres:15-alpine").apply {
            withDatabaseName("testdb")
            withUsername("testuser")
            withPassword("testpassword")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
            registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
        }
    }

    @Autowired
    private lateinit var jpaProductRepository: JpaProductRepository

    @Test
    fun `should save and retrieve product`() {
        // Given
        val product = Product(
            name = "Test Product",
            description = "Test Description",
            price = 99.99,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When
        val savedProduct = jpaProductRepository.save(product)
        val retrievedProduct = jpaProductRepository.findById(savedProduct.id)

        // Then
        assertNotNull(retrievedProduct)
        assertEquals(savedProduct.id, retrievedProduct?.id)
        assertEquals(savedProduct.name, retrievedProduct?.name)
        assertEquals(savedProduct.description, retrievedProduct?.description)
        assertEquals(savedProduct.price, retrievedProduct?.price)
    }

    @Test
    fun `should find all products`() {
        // Given
        val product1 = Product(
            name = "Test Product 1",
            description = "Test Description 1",
            price = 99.99,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val product2 = Product(
            name = "Test Product 2",
            description = "Test Description 2",
            price = 199.99,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When
        jpaProductRepository.save(product1)
        jpaProductRepository.save(product2)
        val products = jpaProductRepository.findAll()

        // Then
        assertTrue(products.size >= 2)
        assertTrue(products.any { it.name == "Test Product 1" })
        assertTrue(products.any { it.name == "Test Product 2" })
    }

    @Test
    fun `should delete product`() {
        // Given
        val product = Product(
            name = "Test Product",
            description = "Test Description",
            price = 99.99,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val savedProduct = jpaProductRepository.save(product)

        // When
        val deleted = jpaProductRepository.deleteById(savedProduct.id)
        val retrievedProduct = jpaProductRepository.findById(savedProduct.id)

        // Then
        assertTrue(deleted)
        assertNull(retrievedProduct)
    }
}
