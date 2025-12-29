package io.github.jovanmosurovic.kode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

            Divider(color = Color.Gray, thickness = 1.dp)

            MainLayout()
        }
    }


}
