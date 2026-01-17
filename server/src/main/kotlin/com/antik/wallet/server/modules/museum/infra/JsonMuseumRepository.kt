package com.antik.wallet.server.modules.museum.infra

import com.antik.wallet.dto.MuseumObject
import com.antik.wallet.server.modules.museum.application.ports.MuseumRepository
import com.antik.wallet.server.modules.museum.domain.MuseumItem
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class JsonMuseumRepository(
    private val resourceName: String = "list.json",
    private val json: Json = Json { ignoreUnknownKeys = true },
) : MuseumRepository {
    private val objects: List<MuseumItem> by lazy {
        val text = this::class.java.classLoader
            .getResource(resourceName)
            ?.readText()
            ?: error("$resourceName not found in resources")

        json.decodeFromString(ListSerializer(MuseumObject.serializer()), text)
            .map { it.toDomain() }
    }

    override fun all(): List<MuseumItem> = objects

    override fun byId(id: Int): MuseumItem? = objects.firstOrNull { it.id == id }
}
