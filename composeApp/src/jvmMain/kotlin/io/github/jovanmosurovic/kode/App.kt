package io.github.jovanmosurovic.kode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import io.github.jovanmosurovic.kode.ui.layout.MainLayout
import io.github.jovanmosurovic.kode.ui.layout.PanelLayout
import io.github.jovanmosurovic.kode.ui.menubar.CustomToolbar
import io.github.jovanmosurovic.kode.ui.panels.console.ConsoleViewModel
import io.github.jovanmosurovic.kode.ui.panels.editor.EditorViewModel
import io.github.jovanmosurovic.kode.ui.theme.KodeTheme

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun App() {

    val editorViewModel = remember { EditorViewModel() }
    val consoleViewModel = remember { ConsoleViewModel() }
    var currentLayout by remember { mutableStateOf(PanelLayout.HORIZONTAL_50_50) }

    KodeTheme {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            CustomToolbar(
                onRunClick = { println("Run clicked! ") },
                onNewFile = { println("New file") },
                onOpenFile = { println("Open file") },
                onSaveFile = { println("Save file") },
                onExit = {},
                currentLayout = currentLayout,
                onLayoutChange = { currentLayout = it }
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                thickness = 1.dp
            )


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                MainLayout(editorViewModel, consoleViewModel, currentLayout)
            }
        }
    }


}
