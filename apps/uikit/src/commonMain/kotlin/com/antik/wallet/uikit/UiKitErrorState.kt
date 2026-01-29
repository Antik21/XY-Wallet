package com.antik.wallet.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image

@Composable
fun UiKitErrorState(
    title: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    image: Painter? = null,
    imageContentDescription: String? = null,
    retryTitle: String = "Повторить",
    isRetryEnabled: Boolean = true,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (image != null) {
            Image(
                painter = image,
                contentDescription = imageContentDescription,
                modifier = Modifier.size(120.dp),
            )
            Spacer(Modifier.height(16.dp))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )

        if (!description.isNullOrBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(16.dp))

        UiKitButton(
            title = retryTitle,
            onClick = onRetry,
            enabled = isRetryEnabled,
        )
    }
}
