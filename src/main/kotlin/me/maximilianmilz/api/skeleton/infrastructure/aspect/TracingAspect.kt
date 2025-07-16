package me.maximilianmilz.api.skeleton.infrastructure.aspect

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 * Aspect for tracing method execution times and recording metrics.
 * This aspect will log the execution time of all public methods in the application
 * and record metrics for them using Micrometer.
 */
@Aspect
@Component
class TracingAspect(private val meterRegistry: MeterRegistry) {

    private val logger = LoggerFactory.getLogger(TracingAspect::class.java)

    /**
     * Around advice that measures and logs the execution time of methods
     * in the application, domain, and api packages.
     */
    @Around(
        "execution(public * me.maximilianmilz.api.skeleton.application..*(..))" +
                " || execution(public * me.maximilianmilz.api.skeleton.domain..*(..))" +
                " || execution(public * me.maximilianmilz.api.skeleton.api..*(..))"
    )
    fun traceMethodExecution(joinPoint: ProceedingJoinPoint): Any? {
        val start = System.nanoTime()
        val signature = joinPoint.signature as MethodSignature
        val className = signature.declaringType.simpleName
        val methodName = signature.name

        return runCatching {
            joinPoint.proceed()
        }.onSuccess { result ->
            val executionTime = System.nanoTime() - start

            // Log the execution time
            logger.debug(
                "Method {}.{} executed in {} ms",
                className, methodName, TimeUnit.NANOSECONDS.toMillis(executionTime)
            )

            // Record the execution time as a metric
            Timer.builder("method.execution.time")
                .tag("class", className)
                .tag("method", methodName)
                .register(meterRegistry)
                .record(executionTime, TimeUnit.NANOSECONDS)

            return result
        }.onFailure { e ->
            // Record the exception as a metric
            meterRegistry.counter(
                "method.execution.error",
                "class", className,
                "method", methodName,
                "exception", e.javaClass.simpleName
            ).increment()
        }.getOrThrow()
    }
}