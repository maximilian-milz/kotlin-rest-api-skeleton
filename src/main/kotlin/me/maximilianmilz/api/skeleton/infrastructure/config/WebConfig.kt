package me.maximilianmilz.api.skeleton.infrastructure.config

import me.maximilianmilz.api.skeleton.infrastructure.interceptor.RateLimitingInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Web configuration for the application.
 * This class is responsible for configuring CORS (Cross-Origin Resource Sharing)
 * and registering interceptors like rate limiting.
 */
@Configuration
class WebConfig(
    private val rateLimitingInterceptor: RateLimitingInterceptor
) : WebMvcConfigurer {

    /**
     * Configure CORS for the application.
     * This allows frontend applications to access the API from different origins.
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*") // Allow all origins - in production, this should be restricted to specific domains
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .maxAge(3600) // 1 hour
    }

    /**
     * Add interceptors to the application.
     * This registers the rate-limiting interceptor for all API endpoints.
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        // Apply rate limiting to all API endpoints
        registry.addInterceptor(rateLimitingInterceptor)
            .addPathPatterns("/api/**") // Apply to all API endpoints
    }
}
