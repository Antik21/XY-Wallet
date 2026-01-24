package com.antik.wallet.api

object ApiRoutes {
    const val BASE = "/api"
    const val OBJECTS = "$BASE/objects"
    const val OBJECT = "$OBJECTS/{id}"
    const val PARAM_ID = "id"
}

object ApiDefaults {
    const val BASE_URL = "http://188.169.217.8:8080"
}

data class ApiConfig(
    val baseUrl: String = ApiDefaults.BASE_URL,
)
