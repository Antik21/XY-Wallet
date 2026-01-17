package com.antik.wallet.server.shared.validation

import com.antik.wallet.server.shared.errors.ValidationException

fun requirePositiveId(id: String?, fieldName: String = "id"): Int {
    val parsed = id?.toIntOrNull() ?: throw ValidationException("Invalid $fieldName")
    if (parsed <= 0) {
        throw ValidationException("Invalid $fieldName")
    }
    return parsed
}
