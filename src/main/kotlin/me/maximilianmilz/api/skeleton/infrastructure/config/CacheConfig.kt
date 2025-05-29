package me.maximilianmilz.api.skeleton.infrastructure.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

/**
 * Cache configuration for the application.
 * This class enables caching for the application, which is used by:
 * - Rate limiting functionality
 * - Service-level caching for improved performance
 */
@Configuration
@EnableCaching
class CacheConfig {

    /**
     * Cache manager bean using Caffeine as the cache provider.
     */
    @Bean
    fun cacheManager(): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager()
        caffeineCacheManager.setCaffeine(
            Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()
        )
        return caffeineCacheManager
    }
}
