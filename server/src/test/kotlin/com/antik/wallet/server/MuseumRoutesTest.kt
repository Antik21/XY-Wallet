package com.antik.wallet.server

import com.antik.wallet.api.ApiRoutes
import com.antik.wallet.dto.MuseumObject
import com.antik.wallet.server.bootstrap.AppConfig
import com.antik.wallet.server.bootstrap.ModuleDependencies
import com.antik.wallet.server.bootstrap.module
import com.antik.wallet.server.modules.museum.application.ports.MuseumRepository
import com.antik.wallet.server.modules.museum.domain.MuseumItem
import com.antik.wallet.server.shared.errors.ApiError
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MuseumRoutesTest {
    private val repository = InMemoryMuseumRepository(
        listOf(
            MuseumItem(
                id = 1,
                title = "First",
                artist = "Artist",
                medium = "Oil",
                dimensions = "10x10",
                url = "https://example.com/1",
                objectDate = "2020",
                primaryImage = "https://example.com/1.jpg",
                primaryImageSmall = "https://example.com/1_small.jpg",
                repository = "Repo",
                department = "Dept",
                creditLine = "Credit",
            ),
            MuseumItem(
                id = 2,
                title = "Second",
                artist = "Artist 2",
                medium = "Ink",
                dimensions = "20x20",
                url = "https://example.com/2",
                objectDate = "2021",
                primaryImage = "https://example.com/2.jpg",
                primaryImageSmall = "https://example.com/2_small.jpg",
                repository = "Repo",
                department = "Dept",
                creditLine = "Credit",
            ),
        ),
    )

    @Test
    fun `list objects returns all items`() = testApplication {
        application {
            module(AppConfig(), ModuleDependencies(museumRepository = repository))
        }
        val client = createClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val response = client.get(ApiRoutes.OBJECTS)
        assertEquals(HttpStatusCode.OK, response.status)

        val body = response.body<List<MuseumObject>>()
        assertEquals(2, body.size)
    }

    @Test
    fun `get object returns item`() = testApplication {
        application {
            module(AppConfig(), ModuleDependencies(museumRepository = repository))
        }
        val client = createClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val response = client.get("${ApiRoutes.OBJECTS}/1")
        assertEquals(HttpStatusCode.OK, response.status)

        val body = response.body<MuseumObject>()
        assertEquals(1, body.objectID)
    }

    @Test
    fun `invalid id returns validation error`() = testApplication {
        application {
            module(AppConfig(), ModuleDependencies(museumRepository = repository))
        }
        val client = createClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val response = client.get("${ApiRoutes.OBJECTS}/invalid")
        assertEquals(HttpStatusCode.BadRequest, response.status)

        val body = response.body<ApiError>()
        assertEquals("validation_error", body.code)
    }

    @Test
    fun `missing item returns not found`() = testApplication {
        application {
            module(AppConfig(), ModuleDependencies(museumRepository = repository))
        }
        val client = createClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val response = client.get("${ApiRoutes.OBJECTS}/999")
        assertEquals(HttpStatusCode.NotFound, response.status)

        val body = response.body<ApiError>()
        assertEquals("not_found", body.code)
    }

    @Test
    fun `health endpoints respond`() = testApplication {
        application {
            module(AppConfig(), ModuleDependencies(museumRepository = repository))
        }
        val client = createClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val live = client.get("/health/live")
        val ready = client.get("/health/ready")

        assertEquals(HttpStatusCode.OK, live.status)
        assertEquals(HttpStatusCode.OK, ready.status)
        assertNotNull(live.body<String>())
        assertNotNull(ready.body<String>())
    }
}

private class InMemoryMuseumRepository(
    private val items: List<MuseumItem>,
) : MuseumRepository {
    override fun all(): List<MuseumItem> = items

    override fun byId(id: Int): MuseumItem? = items.firstOrNull { it.id == id }
}
