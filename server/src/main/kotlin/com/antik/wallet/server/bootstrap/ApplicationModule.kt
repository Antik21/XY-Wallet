package com.antik.wallet.server.bootstrap

import com.antik.wallet.server.shared.errors.configureErrorHandling
import com.antik.wallet.server.shared.tracing.configureRequestId
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

private val jsonConfig = Json { ignoreUnknownKeys = true }

fun Application.module(
    appConfig: AppConfig = AppConfig(),
    dependencies: ModuleDependencies = ModuleDependencies(),
) {
    install(ContentNegotiation) {
        json(jsonConfig)
    }
    install(Compression)
    configureRequestId(appConfig.requestIdHeader)
    install(CallLogging) {
        level = Level.INFO
    }
    configureErrorHandling()
    configureRouting(dependencies)
}
