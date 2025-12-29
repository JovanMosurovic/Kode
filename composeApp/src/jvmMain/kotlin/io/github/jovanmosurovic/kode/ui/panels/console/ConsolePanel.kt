package io.github.jovanmosurovic.kode.ui.panels.console

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose. runtime.Composable
import androidx.compose.ui.Modifier
import androidx. compose.ui.text.style.TextAlign
import io.github.jovanmosurovic.kode.ui.panels.Panel
import io.github.jovanmosurovic.kode.ui.theme.KodeTheme

@Composable
fun ConsolePanel() {
    Panel {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(KodeTheme.editorColors.background)
        ) {
            Text(
                text = "CONSOLE PLACEHOLDER",
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}