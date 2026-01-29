package com.antik.wallet.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.antik.wallet.feature.common.AppBarAction
import com.antik.wallet.feature.common.AppBarConfig
import com.antik.wallet.feature.common.AppBarNavigationIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    config: AppBarConfig,
    onNavigationClick: (AppBarNavigationIcon) -> Unit,
    onActionClick: (String) -> Unit,
) {
    TopAppBar(
        title = { Text(config.title) },
        navigationIcon = {
            if (config.navigationIcon == AppBarNavigationIcon.Back) {
                IconButton(onClick = { onNavigationClick(AppBarNavigationIcon.Back) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        },
        actions = {
            config.actions.forEach { action ->
                val icon = actionIcon(action)
                if (icon != null) {
                    IconButton(onClick = { onActionClick(action.actionId) }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = action.contentDescription ?: action.actionId,
                        )
                    }
                }
            }
        },
    )
}

private const val ICON_REFRESH = "refresh"
private const val ICON_SHARE = "share"
private const val ICON_SEARCH = "search"
private const val ICON_SETTINGS = "settings"

private fun actionIcon(action: AppBarAction): ImageVector? =
    when (action.iconKey) {
        ICON_REFRESH -> Icons.Filled.Refresh
        ICON_SHARE -> Icons.Filled.Share
        ICON_SEARCH -> Icons.Filled.Search
        ICON_SETTINGS -> Icons.Filled.Settings
        else -> null
    }
