package com.antik.wallet.feature.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.antik.wallet.R
import com.antik.wallet.dto.MuseumObject
import com.antik.wallet.feature.common.AppBarActionHandler
import com.antik.wallet.feature.common.AppBarConfig
import com.antik.wallet.feature.common.AppBarNavigationHandler
import com.antik.wallet.feature.common.AppBarNavigationIcon
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun DetailScreen(
    objectId: Int,
    navigator: DetailNavigation.Navigator,
    onAppBarConfigChange: (AppBarConfig) -> Unit,
    onAppBarActionHandlerChange: (AppBarActionHandler) -> Unit,
    onAppBarNavigationHandlerChange: ((AppBarNavigationHandler) -> Unit)? = null,
) {
    val viewModel: DetailViewModel = koinViewModel(parameters = { parametersOf(objectId) })
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.appBar) {
        onAppBarConfigChange(state.appBar)
    }

    LaunchedEffect(viewModel) {
        onAppBarActionHandlerChange(
            object : AppBarActionHandler {
                override fun onAction(actionId: String) {
                    viewModel.onAppBarAction(actionId)
                }
            },
        )
        onAppBarNavigationHandlerChange?.invoke(
            object : AppBarNavigationHandler {
                override fun onNavigation(navigationIcon: AppBarNavigationIcon) {
                    if (navigationIcon == AppBarNavigationIcon.Back) {
                        viewModel.onBackClick()
                    }
                }
            },
        )
    }

    LaunchedEffect(viewModel) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                is SideEffect.ViewEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is SideEffect.Navigation.Back -> navigator.back()
            }
        }
    }

    AnimatedContent(state) { s ->
        when (s) {
            is ViewState.Data -> {
                ObjectDetails(
                    obj = s.museumObject,
                )
            }
            else -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.padding(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ObjectDetails(
    obj: MuseumObject,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .padding(
                WindowInsets.safeDrawing
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                    .asPaddingValues(),
            ),
    ) {
        AsyncImage(
            model = obj.primaryImageSmall,
            contentDescription = obj.title,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        )

        SelectionContainer {
            Column(Modifier.padding(12.dp)) {
                Text(obj.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(6.dp))
                LabeledInfo(stringResource(R.string.label_artist), obj.artistDisplayName)
                LabeledInfo(stringResource(R.string.label_date), obj.objectDate)
                LabeledInfo(stringResource(R.string.label_dimensions), obj.dimensions)
                LabeledInfo(stringResource(R.string.label_medium), obj.medium)
                LabeledInfo(stringResource(R.string.label_department), obj.department)
                LabeledInfo(stringResource(R.string.label_repository), obj.repository)
                LabeledInfo(stringResource(R.string.label_credits), obj.creditLine)
            }
        }
    }
}

@Composable
private fun LabeledInfo(
    label: String,
    data: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(vertical = 4.dp)) {
        Spacer(Modifier.height(6.dp))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$label: ")
                }
                append(data)
            }
        )
    }
}
