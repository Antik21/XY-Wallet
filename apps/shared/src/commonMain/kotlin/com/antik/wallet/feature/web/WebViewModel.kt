package com.antik.wallet.feature.web

import com.antik.wallet.feature.base.BaseViewModel
import org.orbitmvi.orbit.container

class WebViewModel(url: String) : BaseViewModel<ViewState, SideEffect>() {
    override val container = viewModelScope.container<ViewState, SideEffect>(
        initialState = ViewState(url = url),
    )
}
