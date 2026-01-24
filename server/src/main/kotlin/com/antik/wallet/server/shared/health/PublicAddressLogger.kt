package com.antik.wallet.server.shared.health

import com.antik.wallet.server.bootstrap.AppConfig
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

private val logger = LoggerFactory.getLogger("Server")

fun logPublicAddressOnStart(appConfig: AppConfig) {
    val binding = "http://${appConfig.server.host}:${appConfig.server.port}"
    logger.info("Server binding: {}", binding)

    val publicAddress = resolvePublicAddress(appConfig)
    if (publicAddress != null) {
        logger.info("Public address for mobile app: {}", publicAddress)
    } else {
        logger.warn("Public address not resolved. Set SERVER_PUBLIC_URL to override.")
    }
}

private fun resolvePublicAddress(appConfig: AppConfig): String? {
    appConfig.server.publicUrl?.let { return it.trim().ifEmpty { null } }
    val ip = fetchPublicIp() ?: return null
    return "http://$ip:${appConfig.server.port}"
}

private fun fetchPublicIp(): String? {
    return try {
        val client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build()
        val request = HttpRequest.newBuilder(URI("https://api.ipify.org"))
            .timeout(Duration.ofSeconds(3))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 200) return null
        val ip = response.body().trim()
        if (ip.matches(Regex("""\A\d{1,3}(\.\d{1,3}){3}\z"""))) ip else null
    } catch (_: Exception) {
        null
    }
}
