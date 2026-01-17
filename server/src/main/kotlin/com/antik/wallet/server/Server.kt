package com.antik.wallet.server

import com.antik.wallet.server.bootstrap.appConfigFromEnv
import com.antik.wallet.server.bootstrap.ModuleDependencies
import com.antik.wallet.server.bootstrap.module
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val appConfig = appConfigFromEnv()
    embeddedServer(Netty, host = appConfig.server.host, port = appConfig.server.port) {
        module(appConfig, ModuleDependencies())
    }.start(wait = true)
}
