package com.antik.wallet.server.shared.errors

import com.antik.wallet.dto.ApiError
import com.antik.wallet.server.shared.tracing.requestId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.plugins.statuspages.exception
import io.ktor.server.response.respond

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<ApiException> { call, cause ->
            call.respond(
                cause.status,
                ApiError(
                    code = cause.code,
                    message = cause.message,
                    details = cause.details,
                    requestId = call.requestId(),
                ),
            )
        }

        exception<Throwable> { call, cause ->
            call.application.environment.log.error("Unhandled error", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiError(
                    code = "unexpected_error",
                    message = "Unexpected error",
                    requestId = call.requestId(),
                ),
            )
        }
    }
}
