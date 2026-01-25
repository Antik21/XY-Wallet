package com.antik.wallet.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.antik.wallet.feature.detail.DetailNavigation
import com.antik.wallet.feature.detail.DetailScreen
import com.antik.wallet.feature.common.AppBarActionHandler
import com.antik.wallet.feature.common.AppBarConfig
import com.antik.wallet.feature.common.AppBarNavigationHandler
import com.antik.wallet.feature.common.AppBarNavigationIcon
import com.antik.wallet.feature.list.ListNavigation
import com.antik.wallet.feature.list.ListScreen
import kotlinx.serialization.Serializable

@Serializable
data object ListRoute

@Serializable
data class DetailRoute(val objectId: Int)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Observing the current entry triggers recomposition when the back stack changes.
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    var appBarConfig by remember { mutableStateOf(AppBarConfig(title = "XY-Wallet")) }
    var appBarActionHandler by remember { mutableStateOf<AppBarActionHandler>(NoOpAppBarActionHandler) }
    var appBarNavigationHandler by remember { mutableStateOf<AppBarNavigationHandler>(NoOpAppBarNavigationHandler) }
    val canGoBack = currentBackStackEntry != null && navController.previousBackStackEntry != null
    val resolvedNavigationIcon = appBarConfig.navigationIcon ?: if (canGoBack) {
        AppBarNavigationIcon.Back
    } else {
        null
    }
    val resolvedAppBarConfig = appBarConfig.copy(navigationIcon = resolvedNavigationIcon)

    LaunchedEffect(currentBackStackEntry) {
        // Reset handlers on destination change to avoid leaking behavior across screens.
        appBarActionHandler = NoOpAppBarActionHandler
        appBarNavigationHandler = NoOpAppBarNavigationHandler
    }
    val listNavigator = remember(navController) {
        object : ListNavigation.Navigator {
            override fun openDetail(objectId: Int) {
                navController.navigate(DetailRoute(objectId))
            }
        }
    }
    val detailNavigator = remember(navController) {
        object : DetailNavigation.Navigator {
            override fun back() {
                navController.popBackStack()
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                config = resolvedAppBarConfig,
                onNavigationClick = { icon ->
                    appBarNavigationHandler.onNavigation(icon)
                    if (
                        appBarNavigationHandler === NoOpAppBarNavigationHandler &&
                        icon == AppBarNavigationIcon.Back
                    ) {
                        navController.popBackStack()
                    }
                },
                onActionClick = appBarActionHandler::onAction,
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ListRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<ListRoute> {
                ListScreen(
                    navigator = listNavigator,
                    onAppBarConfigChange = { appBarConfig = it },
                    onAppBarActionHandlerChange = { appBarActionHandler = it },
                )
            }

            composable<DetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<DetailRoute>()
                DetailScreen(
                    objectId = route.objectId,
                    navigator = detailNavigator,
                    onAppBarConfigChange = { appBarConfig = it },
                    onAppBarActionHandlerChange = { appBarActionHandler = it },
                    onAppBarNavigationHandlerChange = { appBarNavigationHandler = it },
                )
            }
        }
    }
}

private object NoOpAppBarActionHandler : AppBarActionHandler {
    override fun onAction(actionId: String) = Unit
}

private object NoOpAppBarNavigationHandler : AppBarNavigationHandler {
    override fun onNavigation(navigationIcon: AppBarNavigationIcon) = Unit
}
