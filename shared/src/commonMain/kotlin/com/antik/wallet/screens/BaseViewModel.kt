package com.antik.wallet.screens

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.ContainerHost

/**
 * Base ViewModel для Orbit в KMP.
 *
 * - `uiState` помечаем `@NativeCoroutinesState`, чтобы iOS мог удобно подписываться на StateFlow.
 * - `sideEffects` помечаем `@NativeCoroutines` (НЕ `State`), т.к. это обычный Flow.
 */
abstract class BaseViewModel<STATE : Any, SIDE_EFFECT : Any> :
    ViewModel(),
    ContainerHost<STATE, SIDE_EFFECT> {

    @NativeCoroutinesState
    val uiState: StateFlow<STATE>
        get() = container.stateFlow

    @NativeCoroutines
    val sideEffects: Flow<SIDE_EFFECT>
        get() = container.sideEffectFlow
}
