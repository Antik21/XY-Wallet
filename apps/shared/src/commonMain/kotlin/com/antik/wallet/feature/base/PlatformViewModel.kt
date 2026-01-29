package com.antik.wallet.feature.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class PlatformViewModel : ViewModel() {
    private val job = SupervisorJob()

    val viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate + job)

    override fun onCleared() {
        job.cancel()
    }
}
