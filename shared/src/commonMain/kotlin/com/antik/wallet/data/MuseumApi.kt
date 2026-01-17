package com.antik.wallet.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import com.antik.wallet.api.ApiConfig
import com.antik.wallet.api.ApiRoutes
import com.antik.wallet.dto.MuseumObject
import kotlin.coroutines.cancellation.CancellationException

interface MuseumApi {
    suspend fun getData(): List<MuseumObject>
}

class KtorMuseumApi(
    private val client: HttpClient,
    private val apiConfig: ApiConfig,
) : MuseumApi {
    private val objectsUrl: String =
        apiConfig.baseUrl.trimEnd('/') + ApiRoutes.OBJECTS

    override suspend fun getData(): List<MuseumObject> {
        return try {
            client.get(objectsUrl).body()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()

            emptyList()
        }
    }
}
