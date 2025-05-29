package me.maximilianmilz.api.skeleton.infrastructure.config

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration

/**
 * Cache configuration for the application.
 * This class enables caching for the application, which is used by the rate limiting functionality.
 */
@Configuration
@EnableCaching
class CacheConfig