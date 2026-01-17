package com.antik.wallet.server.bootstrap

import com.antik.wallet.server.modules.museum.api.configureMuseumRoutes
import com.antik.wallet.server.shared.health.configureHealthRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(dependencies: ModuleDependencies) {
    routing {
        configureHealthRoutes()
        configureMuseumRoutes(dependencies.museumRepository)
    }
}
