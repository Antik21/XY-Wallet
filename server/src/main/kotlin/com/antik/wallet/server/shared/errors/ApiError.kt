package com.antik.wallet.server.shared.errors

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val code: String,
    val message: String,
    val details: String? = null,
    val requestId: String? = null,
)
