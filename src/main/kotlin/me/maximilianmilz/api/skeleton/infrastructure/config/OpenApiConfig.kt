package me.maximilianmilz.api.skeleton.infrastructure.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for OpenAPI/Swagger documentation.
 */
@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .info(apiInfo())
        .servers(listOf(
            Server().url("/").description("Default Server URL")
        ))

    private fun apiInfo(): Info {
        return Info()
            .title("Kotlin REST API Skeleton")
            .description("REST API example")
            .version("v1.0.0")
            .contact(
                Contact()
                    .name("Maximilian Milz")
                    .url("https://maximilian-milz.me")
                    .email("contact@maximilian-milz.me")
            )
            .license(
                License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")
            )
    }
}