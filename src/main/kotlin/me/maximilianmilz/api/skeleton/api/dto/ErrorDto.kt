package me.maximilianmilz.api.skeleton.api.dto

import java.time.LocalDateTime

/**
 * DTO for API error responses.
 */
data class ErrorResponseDto(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val details: List<ValidationErrorDto> = emptyList()
)

/**
 * DTO for validation error details.
 */
data class ValidationErrorDto(
    val field: String,
    val message: String
)