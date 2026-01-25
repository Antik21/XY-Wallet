package com.antik.wallet.feature.detail

import com.antik.wallet.dto.MuseumObject
import com.antik.wallet.feature.common.AppBarConfig

sealed interface ViewState {
    val objectId: Int
    val appBar: AppBarConfig

    data class Loading(
        override val objectId: Int,
        override val appBar: AppBarConfig = detailAppBar("Details"),
    ) : ViewState

    data class Data(
        override val objectId: Int,
        val museumObject: MuseumObject,
        override val appBar: AppBarConfig = detailAppBar(museumObject.title),
    ) : ViewState

    data class NotFound(
        override val objectId: Int,
        override val appBar: AppBarConfig = detailAppBar("Details"),
    ) : ViewState

    data class Error(
        override val objectId: Int,
        val message: String,
        override val appBar: AppBarConfig = detailAppBar("Details"),
    ) : ViewState
}

sealed interface SideEffect {
    sealed interface ViewEffect : SideEffect {
        data class ShowMessage(val message: String) : ViewEffect
    }

    sealed interface Navigation : SideEffect {
        data object Back : Navigation
    }
}

private fun detailAppBar(title: String): AppBarConfig =
    AppBarConfig(
        title = title,
    )
