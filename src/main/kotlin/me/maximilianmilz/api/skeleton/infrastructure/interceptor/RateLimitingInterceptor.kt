package me.maximilianmilz.api.skeleton.infrastructure.interceptor

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

/**
 * Interceptor for rate limiting API requests.
 * This class uses the bucket4j library to implement a token bucket algorithm for rate limiting.
 */
@Component
class RateLimitingInterceptor : HandlerInterceptor {

    // Cache to store rate limiters for each IP address
    private val buckets = ConcurrentHashMap<String, Bucket>()

    // Rate limit configuration: 20 requests per minute
    private val limit = 20
    private val refillPeriod = Duration.ofMinutes(1)

    /**
     * Create a new bucket for rate limiting.
     * The bucket is configured with a token refill rate of [limit] tokens per [refillPeriod].
     */
    private fun createBucket(): Bucket {
        val refill = Refill.intervally(limit.toLong(), refillPeriod)
        val bandwidth = Bandwidth.classic(limit.toLong(), refill)
        return Bucket.builder().addLimit(bandwidth).build()
    }

    /**
     * Get the client's IP address from the request.
     * This method handles cases where the request might be coming through a proxy.
     */
    private fun getClientIP(request: HttpServletRequest): String {
        var ip = request.getHeader("X-Forwarded-For")
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.remoteAddr
        }
        return ip
    }

    /**
     * Pre-handle method that is called before the actual handler is executed.
     * This method checks if the request should be rate-limited.
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val ip = getClientIP(request)
        val bucket = buckets.computeIfAbsent(ip) { createBucket() }

        // Try to consume a token from the bucket
        val probe = bucket.tryConsumeAndReturnRemaining(1)
        
        // If the request is allowed, add rate limit headers
        response.addHeader("X-Rate-Limit-Limit", limit.toString())
        response.addHeader("X-Rate-Limit-Remaining", probe.remainingTokens.toString())
        response.addHeader("X-Rate-Limit-Reset", (probe.nanosToWaitForRefill / 1_000_000_000).toString())

        // If the request is not allowed, return 429 Too Many Requests
        if (!probe.isConsumed) {
            response.status = HttpStatus.TOO_MANY_REQUESTS.value()
            response.contentType = "application/json"
            response.writer.write("{\"error\":\"Rate limit exceeded. Please try again later.\"}")
            return false
        }

        return true
    }
}