package io.github.jovanmosurovic.kode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jovanmosurovic.kode.runner.CodeRunner
import io.github.jovanmosurovic.kode.ui.components.StatusBar
import io.github.jovanmosurovic.kode.ui.dialogs.AboutDialog
import io.github.jovanmosurovic.kode.ui.dialogs.LiveTemplatesHelpDialog
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import io.github.jovanmosurovic.kode.ui.layout.MainLayout
import io.github.jovanmosurovic.kode.ui.layout.PanelLayout
import io.github.jovanmosurovic.kode.ui.menu.FileMenuActions
import io.github.jovanmosurovic.kode.ui.menubar.CustomToolbar
import io.github.jovanmosurovic.kode.ui.panels.console.ConsoleViewModel
import io.github.jovanmosurovic.kode.ui.panels.editor.EditorViewModel
import io.github.jovanmosurovic.kode.ui.theme.KodeTheme
import io.github.jovanmosurovic.kode.utils.FileConstants

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun App(onCloseRequest: () -> Unit = {}) {

    val editorViewModel = remember { EditorViewModel() }
    val consoleViewModel = remember { ConsoleViewModel() }
    val scope = rememberCoroutineScope()
    val codeRunner = remember(consoleViewModel, scope) { CodeRunner(consoleViewModel, scope) }
    val editorState by editorViewModel.state.collectAsState()

    var currentLayout by remember { mutableStateOf(PanelLayout.HORIZONTAL_50_50) }
    var showLiveTemplatesDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    val fileMenuActions = remember {
        FileMenuActions(
            getState = { editorViewModel.state.value },
            updateState = { newState -> editorViewModel.updateState(newState) }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            FileConstants.clearDefaultScript()
        }
    }

    KodeTheme {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            CustomToolbar(
                onRunClick = {
                    fileMenuActions.onSave()
                    codeRunner.runKotlinScript(editorViewModel)
                },
                onStopClick = { codeRunner.stopExecution() },
                onNewFile = { fileMenuActions.onNewFile() },
                onOpenFile = { fileMenuActions.onOpenFile() },
                onSaveFile = { fileMenuActions.onSave() },
                onSaveFileAs = { fileMenuActions.onSaveAs() },
                onExit = onCloseRequest,
                onPaste = { editorViewModel.paste() },
                onSelectAll = { editorViewModel.selectAll() },
                currentLayout = currentLayout,
                onLayoutChange = { currentLayout = it },
                onAbout = { showAboutDialog = true },
                onLiveTemplatesHelp = { showLiveTemplatesDialog = true }
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                thickness = 1.dp
            )


            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                MainLayout(editorViewModel, consoleViewModel, codeRunner, currentLayout)
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                thickness = 1.dp
            )

            StatusBar(
                line = editorState.cursorLine,
                column = editorState.cursorColumn
            )
        }

        if (showLiveTemplatesDialog) {
            LiveTemplatesHelpDialog(onDismiss = { showLiveTemplatesDialog = false })
        }

        if (showAboutDialog) {
            AboutDialog(onDismiss = { showAboutDialog = false })
        }
    }


}
