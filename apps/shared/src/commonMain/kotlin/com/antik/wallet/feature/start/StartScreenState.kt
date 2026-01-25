package com.antik.wallet.feature.start

import com.antik.wallet.feature.common.AppBarConfig

const val DEFAULT_WEB_URL = "https://kotlinlang.org"

data class ViewState(
    val appBar: AppBarConfig = startAppBar(),
    val webUrl: String = DEFAULT_WEB_URL,
)

sealed interface SideEffect {
    sealed interface Navigation : SideEffect {
        data object OpenListFlow : Navigation

        data class OpenWebFlow(val url: String) : Navigation
    }
}

private fun startAppBar(): AppBarConfig =
    AppBarConfig(
        title = "Start",
    )
