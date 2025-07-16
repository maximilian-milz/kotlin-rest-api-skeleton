package me.maximilianmilz.api.skeleton.infrastructure.config

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

/**
 * Base configuration for tests using Testcontainers.
 * This abstract class provides a shared PostgreSQL container for all tests.
 */
abstract class TestContainersConfig {

    companion object {
        /**
         * Shared PostgreSQL container for all tests.
         */
        @Container
        @JvmStatic
        val postgresContainer = PostgreSQLContainer<Nothing>("postgres:15-alpine").apply {
            withDatabaseName("testdb")
            withUsername("testuser")
            withPassword("testpassword")
            // Start the container early
            start()
        }

        /**
         * Configure Spring properties to use the PostgreSQL container.
         */
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
}
