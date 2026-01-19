package com.antik.wallet.screens

import kotlinx.coroutines.CoroutineScope

expect open class PlatformViewModel() {
    val viewModelScope: CoroutineScope

    open fun clear()
}
