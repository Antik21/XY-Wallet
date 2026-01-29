package com.antik.wallet.feature.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antik.wallet.feature.common.AppBarActionHandler
import com.antik.wallet.feature.common.AppBarConfig
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartScreen(
    navigator: StartNavigation.Navigator,
    onAppBarConfigChange: (AppBarConfig) -> Unit,
    onAppBarActionHandlerChange: (AppBarActionHandler) -> Unit,
) {
    val viewModel: StartViewModel = koinViewModel()
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

    LaunchedEffect(viewModel) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                is SideEffect.Navigation.OpenListFlow -> navigator.openListFlow()
                is SideEffect.Navigation.OpenWebFlow -> navigator.openWebFlow(effect.url)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Choose a flow",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = viewModel::onListFlowClick) {
            Text(text = "Open list flow")
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = viewModel::onWebFlowClick) {
            Text(text = "Open web flow")
        }
    }
}
