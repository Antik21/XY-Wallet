package com.antik.wallet.navigation

import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.antik.wallet.feature.museum.detail.DetailNavigation
import com.antik.wallet.feature.common.AppBarActionHandler
import com.antik.wallet.feature.common.AppBarConfig
import com.antik.wallet.feature.common.AppBarNavigationHandler
import com.antik.wallet.feature.common.AppBarNavigationIcon
import com.antik.wallet.feature.museum.list.ListNavigation
import com.antik.wallet.feature.museum.detail.DetailScreen
import com.antik.wallet.feature.museum.list.ListScreen
import com.antik.wallet.feature.start.StartNavigation
import com.antik.wallet.feature.start.StartScreen
import com.antik.wallet.feature.web.WebScreen
import kotlinx.serialization.Serializable

@Serializable
data object StartRoute

@Serializable
data object ListRoute

@Serializable
data class DetailRoute(val objectId: Int)

@Serializable
data class WebRoute(val url: String)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Observing the current entry triggers recomposition when the back stack changes.
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    var appBarConfig by remember { mutableStateOf(AppBarConfig(title = "XY-Wallet")) }
    var appBarActionHandler by remember { mutableStateOf<AppBarActionHandler>(NoOpAppBarActionHandler) }
    var appBarNavigationHandler by remember { mutableStateOf<AppBarNavigationHandler>(NoOpAppBarNavigationHandler) }
    var lastNonEmptyTitle by remember { mutableStateOf(appBarConfig.title) }
    val resolvedAppBarConfig = if (appBarConfig.title.isBlank()) {
        appBarConfig.copy(title = lastNonEmptyTitle)
    } else {
        appBarConfig
    }
    LaunchedEffect(appBarConfig.title) {
        if (appBarConfig.title.isNotBlank()) {
            lastNonEmptyTitle = appBarConfig.title
        }
    }
    LaunchedEffect(currentBackStackEntry) {
        // Reset handlers on destination change to avoid leaking behavior across screens.
        appBarActionHandler = NoOpAppBarActionHandler
        appBarNavigationHandler = NoOpAppBarNavigationHandler
    }
    val startNavigator = remember(navController) {
        object : StartNavigation.Navigator {
            override fun openListFlow() {
                navController.navigate(ListRoute)
            }

            override fun openWebFlow(url: String) {
                navController.navigate(WebRoute(Uri.encode(url)))
            }
        }
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
            startDestination = StartRoute,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { it },
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { -it },
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { -it },
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { it },
                )
            },
        ) {
            composable<StartRoute> {
                StartScreen(
                    navigator = startNavigator,
                    onAppBarConfigChange = { appBarConfig = it },
                    onAppBarActionHandlerChange = { appBarActionHandler = it },
                )
            }

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

            composable<WebRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<WebRoute>()
                WebScreen(
                    url = Uri.decode(route.url),
                    onAppBarConfigChange = { appBarConfig = it },
                    onAppBarActionHandlerChange = { appBarActionHandler = it },
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
