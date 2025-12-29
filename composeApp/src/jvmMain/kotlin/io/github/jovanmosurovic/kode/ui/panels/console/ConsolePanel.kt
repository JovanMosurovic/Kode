package io.github.jovanmosurovic.kode.ui.panels.console

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose. runtime.Composable
import androidx. compose.ui.text.style.TextAlign
import io.github.jovanmosurovic.kode.ui.panels.Panel

@Composable
fun ConsolePanel() {
    Panel {
        Text(
            text = "CONSOLE PLACEHOLDER",
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}