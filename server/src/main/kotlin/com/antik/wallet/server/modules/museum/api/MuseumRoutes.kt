package com.antik.wallet.server.modules.museum.api

import com.antik.wallet.api.ApiRoutes
import com.antik.wallet.server.modules.museum.application.GetMuseumObjectUseCase
import com.antik.wallet.server.modules.museum.application.ListMuseumObjectsUseCase
import com.antik.wallet.server.modules.museum.application.ports.MuseumRepository
import com.antik.wallet.server.shared.errors.NotFoundException
import com.antik.wallet.server.shared.validation.requirePositiveId
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.configureMuseumRoutes(repository: MuseumRepository) {
    val listUseCase = ListMuseumObjectsUseCase(repository)
    val getUseCase = GetMuseumObjectUseCase(repository)

    get(ApiRoutes.OBJECTS) {
        val objects = listUseCase.execute().map { it.toDto() }
        call.respond(objects)
    }

    get(ApiRoutes.OBJECT) {
        val id = requirePositiveId(call.parameters[ApiRoutes.PARAM_ID])
        val obj = getUseCase.execute(id) ?: throw NotFoundException("Object not found")
        call.respond(obj.toDto())
    }
}
