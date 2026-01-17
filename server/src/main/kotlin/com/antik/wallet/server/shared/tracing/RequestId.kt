package com.antik.wallet.server.shared.tracing

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callId
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Application.configureRequestId(headerName: String) {
    install(CallId) {
        retrieveFromHeader(headerName)
        generate { Uuid.random().toString() }
        verify { it.isNotBlank() }
        replyToHeader(headerName)
    }
}

fun io.ktor.server.application.ApplicationCall.requestId(): String? = callId
