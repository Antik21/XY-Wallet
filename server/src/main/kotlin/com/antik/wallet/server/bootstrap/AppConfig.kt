package com.antik.wallet.server.bootstrap

import com.antik.wallet.api.ApiHeaders

data class AppConfig(
    val server: ServerConfig = ServerConfig(),
    val requestIdHeader: String = ApiHeaders.REQUEST_ID,
)

data class ServerConfig(
    val host: String = "0.0.0.0",
    val port: Int = 8080,
    val publicUrl: String? = null,
    val logPublicUrlOnStart: Boolean = true,
)

object AppConfigLoader {
    fun fromEnv(): AppConfig {
        val host = System.getenv("SERVER_HOST") ?: "0.0.0.0"
        val port = System.getenv("SERVER_PORT")?.toIntOrNull()
            ?: System.getenv("PORT")?.toIntOrNull()
            ?: 8080
        val publicUrl = System.getenv("SERVER_PUBLIC_URL")
        val logPublicUrlOnStart = parseBooleanEnv(System.getenv("SERVER_LOG_PUBLIC_URL"))
        return AppConfig(
            server = ServerConfig(
                host = host,
                port = port,
                publicUrl = publicUrl,
                logPublicUrlOnStart = logPublicUrlOnStart,
            ),
        )
    }

    private fun parseBooleanEnv(value: String?): Boolean {
        return value
            ?.trim()
            ?.lowercase()
            ?.let { it == "1" || it == "true" || it == "yes" || it == "y" || it == "on" }
            ?: false
    }
}

fun appConfigFromEnv(): AppConfig = AppConfigLoader.fromEnv()
