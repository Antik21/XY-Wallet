package com.antik.wallet.server.shared.errors

import io.ktor.http.HttpStatusCode

sealed class ApiException(
    val status: HttpStatusCode,
    val code: String,
    override val message: String,
    val details: String? = null,
) : RuntimeException(message)

class ValidationException(message: String, details: String? = null) : ApiException(
    status = HttpStatusCode.BadRequest,
    code = "validation_error",
    message = message,
    details = details,
)

class NotFoundException(message: String, details: String? = null) : ApiException(
    status = HttpStatusCode.NotFound,
    code = "not_found",
    message = message,
    details = details,
)

class ConflictException(message: String, details: String? = null) : ApiException(
    status = HttpStatusCode.Conflict,
    code = "conflict",
    message = message,
    details = details,
)
