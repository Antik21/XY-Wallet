package com.antik.wallet.feature.web

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antik.wallet.feature.common.AppBarActionHandler
import com.antik.wallet.feature.common.AppBarConfig
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WebScreen(
    url: String,
    onAppBarConfigChange: (AppBarConfig) -> Unit,
    onAppBarActionHandlerChange: (AppBarActionHandler) -> Unit,
) {
    val viewModel: WebViewModel = koinViewModel(parameters = { parametersOf(url) })
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.appBar) {
        onAppBarConfigChange(state.appBar)
    }

    LaunchedEffect(viewModel) {
        onAppBarActionHandlerChange(
            object : AppBarActionHandler {
                override fun onAction(actionId: String) = Unit
            },
        )
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                loadUrl(state.url)
            }
        },
        update = { webView ->
            if (webView.url != state.url) {
                webView.loadUrl(state.url)
            }
        },
        modifier = Modifier.fillMaxSize(),
    )
}
