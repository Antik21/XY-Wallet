package com.antik.wallet.feature.list

import com.antik.wallet.dto.MuseumObject
import com.antik.wallet.feature.common.AppBarAction
import com.antik.wallet.feature.common.AppBarConfig

data class ViewState(
    val objects: List<MuseumObject> = emptyList(),
    val isLoading: Boolean = false,
    val appBar: AppBarConfig = listAppBar(),
)

sealed interface SideEffect {
    sealed interface ViewEffect : SideEffect {
        data class ShowMessage(val message: String) : ViewEffect
    }

    sealed interface Navigation : SideEffect {
        data class OpenDetail(val objectId: Int) : Navigation
    }
}

internal const val ACTION_REFRESH = "list:refresh"
internal const val ICON_REFRESH = "refresh"

private fun listAppBar(): AppBarConfig =
    AppBarConfig(
        title = "XY-Wallet",
        actions = listOf(
            AppBarAction(
                actionId = ACTION_REFRESH,
                iconKey = ICON_REFRESH,
                contentDescription = "Refresh",
            ),
        ),
    )
