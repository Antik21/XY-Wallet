package com.antik.wallet.screens

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

actual open class PlatformViewModel actual constructor() {
    private val job = SupervisorJob()

    actual val viewModelScope: CoroutineScope =
        CoroutineScope(Dispatchers.Main.immediate + job)

    actual open fun clear() {
        job.cancel()
    }
}
