package com.antik.wallet.feature.start

import com.antik.wallet.feature.base.BaseViewModel
import org.orbitmvi.orbit.container

class StartViewModel : BaseViewModel<ViewState, SideEffect>() {
    override val container = viewModelScope.container<ViewState, SideEffect>(
        initialState = ViewState(),
    )

    fun onListFlowClick() = intent {
        postSideEffect(SideEffect.Navigation.OpenListFlow)
    }

    fun onWebFlowClick() = intent {
        postSideEffect(SideEffect.Navigation.OpenWebFlow(state.webUrl))
    }
}
