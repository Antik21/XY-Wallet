package com.antik.wallet.screens

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.ContainerHost

/**
 * Base ViewModel for Orbit-based screens with shared Flow exposure.
 */
abstract class BaseViewModel<STATE : Any, SIDE_EFFECT : Any> :
    PlatformViewModel(),
    ContainerHost<STATE, SIDE_EFFECT> {

    val uiState: StateFlow<STATE>
        get() = container.stateFlow

    val sideEffects: Flow<SIDE_EFFECT>
        get() = container.sideEffectFlow
}
