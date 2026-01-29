package com.antik.wallet.utils

import kotlinx.coroutines.CancellationException

inline fun <T> Result<T>.onFailureCancellable(action: (Throwable) -> Unit): Result<T> =
    onFailure { throwable ->
        if (throwable is CancellationException) throw throwable
        action(throwable)
    }
