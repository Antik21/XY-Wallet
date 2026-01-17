package com.antik.wallet.server.shared.health

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable

@Serializable
private data class HealthStatus(val status: String)

fun Route.configureHealthRoutes() {
    get("/health/live") {
        call.respond(HttpStatusCode.OK, HealthStatus("live"))
    }
    get("/health/ready") {
        call.respond(HttpStatusCode.OK, HealthStatus("ready"))
    }
}
