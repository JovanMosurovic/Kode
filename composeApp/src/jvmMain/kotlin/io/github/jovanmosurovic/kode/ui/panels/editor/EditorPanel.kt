package io.github.jovanmosurovic.kode.ui.panels.editor

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import io.github.jovanmosurovic.kode.ui.panels.Panel

@Composable
fun EditorPanel() {
    Panel {
        Text(
            text = "EDITOR PLACEHOLDER",
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}