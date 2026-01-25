package com.antik.wallet.feature.common

enum class AppBarNavigationIcon {
    Back,
}

data class AppBarAction(
    val actionId: String,
    val iconKey: String,
    val contentDescription: String? = null,
)

data class AppBarConfig(
    val title: String,
    val navigationIcon: AppBarNavigationIcon? = null,
    val actions: List<AppBarAction> = emptyList(),
)

interface AppBarActionHandler {
    fun onAction(actionId: String)
}

interface AppBarNavigationHandler {
    fun onNavigation(navigationIcon: AppBarNavigationIcon)
}
