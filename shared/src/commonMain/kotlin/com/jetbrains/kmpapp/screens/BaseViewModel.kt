package com.jetbrains.kmpapp.screens

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.ContainerHost


abstract class BaseViewModel<STATE : Any, SIDE_EFFECT : Any> : ViewModel(),
    ContainerHost<STATE, SIDE_EFFECT> {

    @NativeCoroutinesState
    public val stateFlow: StateFlow<STATE>
        get() = container.stateFlow

    @NativeCoroutinesState
    public val sideEffectFlow: Flow<SIDE_EFFECT>
        get() = container.sideEffectFlow

}
