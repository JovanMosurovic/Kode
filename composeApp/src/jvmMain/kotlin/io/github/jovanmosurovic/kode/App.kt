package io.github.jovanmosurovic.kode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import io.github.jovanmosurovic.kode.ui.layout.MainLayout
import io.github.jovanmosurovic.kode.ui.menubar.CustomToolbar
import io.github.jovanmosurovic.kode.ui.theme.KodeTheme

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun App() {
    KodeTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            CustomToolbar(
                onRunClick = { println("Run clicked! ") },
                onNewFile = { println("New file") },
                onOpenFile = { println("Open file") },
                onSaveFile = { println("Save file") },
                onExit = {}
            )

            HorizontalDivider(color = MaterialTheme.colors.surface, thickness = 1.dp)

            MainLayout()
        }
    }


}
