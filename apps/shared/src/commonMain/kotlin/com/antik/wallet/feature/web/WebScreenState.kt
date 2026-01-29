package com.antik.wallet.feature.web

import com.antik.wallet.feature.common.AppBarConfig
import com.antik.wallet.feature.common.AppBarNavigationIcon

data class ViewState(
    val url: String,
    val appBar: AppBarConfig = webAppBar(),
)

sealed interface SideEffect {
    data object None : SideEffect
}

private fun webAppBar(): AppBarConfig =
    AppBarConfig(
        title = "Web",
        navigationIcon = AppBarNavigationIcon.Back,
    )
