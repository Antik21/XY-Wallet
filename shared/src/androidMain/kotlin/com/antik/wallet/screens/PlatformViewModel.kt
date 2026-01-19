package com.antik.wallet.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope as lifecycleViewModelScope
import kotlinx.coroutines.CoroutineScope

actual open class PlatformViewModel actual constructor() : ViewModel() {
    actual val viewModelScope: CoroutineScope
        get() = lifecycleViewModelScope

    actual open fun clear() {
        // Android lifecycle manages ViewModel cleanup.
    }
}
