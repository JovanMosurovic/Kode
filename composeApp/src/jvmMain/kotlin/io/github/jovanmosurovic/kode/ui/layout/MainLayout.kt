package io.github.jovanmosurovic.kode.ui.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import io.github.jovanmosurovic.kode.ui.panels.console.ConsolePanel
import io.github.jovanmosurovic.kode.ui.panels.editor.CodeEditorPanel
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.VerticalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState
import io.github.jovanmosurovic.kode.ui.components.horizontalSplitter
import io.github.jovanmosurovic.kode.ui.components.verticalSplitter
import io.github.jovanmosurovic.kode.ui.panels.console.ConsoleViewModel
import io.github.jovanmosurovic.kode.ui.panels.editor.EditorViewModel

enum class PanelLayout {
    HORIZONTAL_50_50,
    HORIZONTAL_70_30,
    HORIZONTAL_30_70,

    VERTICAL_50_50,
    VERTICAL_70_30,
    VERTICAL_30_70
}

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun MainLayout(
    editorViewModel: EditorViewModel,
    consoleViewModel: ConsoleViewModel,
    currentLayout: PanelLayout = PanelLayout.HORIZONTAL_50_50
) {
    when (currentLayout) {
        PanelLayout.HORIZONTAL_50_50 -> {
            HorizontalSplitPane(
                splitPaneState = rememberSplitPaneState(0.5f)
            ) {
                first(minSize = 100.dp) { CodeEditorPanel(viewModel = editorViewModel) }
                second(minSize = 100.dp) { ConsolePanel(viewModel = consoleViewModel) }
                splitter { horizontalSplitter() }
            }
        }

        PanelLayout.HORIZONTAL_70_30 -> {
            HorizontalSplitPane(
                splitPaneState = rememberSplitPaneState(0.7f)
            ) {
                first(minSize = 100.dp) { CodeEditorPanel(viewModel = editorViewModel) }
                second(minSize = 100.dp) { ConsolePanel(viewModel = consoleViewModel) }
                splitter { horizontalSplitter() }
            }
        }

        PanelLayout.HORIZONTAL_30_70 -> {
            HorizontalSplitPane(
                splitPaneState = rememberSplitPaneState(0.3f)
            ) {
                first(minSize = 100.dp) { CodeEditorPanel(viewModel = editorViewModel) }
                second(minSize = 100.dp) { ConsolePanel(viewModel = consoleViewModel) }
                splitter { horizontalSplitter() }
            }
        }

        PanelLayout.VERTICAL_50_50 -> {
            VerticalSplitPane(
                splitPaneState = rememberSplitPaneState(0.5f)
            ) {
                first(minSize = 100.dp) { CodeEditorPanel(viewModel = editorViewModel) }
                second(minSize = 100.dp) { ConsolePanel(viewModel = consoleViewModel) }
                splitter { verticalSplitter() }
            }
        }

        PanelLayout.VERTICAL_70_30 -> {
            VerticalSplitPane(
                splitPaneState = rememberSplitPaneState(0.7f)
            ) {
                first(minSize = 100.dp) { CodeEditorPanel(viewModel = editorViewModel) }
                second(minSize = 100.dp) { ConsolePanel(viewModel = consoleViewModel) }
                splitter { verticalSplitter() }
            }
        }

        PanelLayout.VERTICAL_30_70 -> {
            VerticalSplitPane(
                splitPaneState = rememberSplitPaneState(0.3f)
            ) {
                first(minSize = 100.dp) { CodeEditorPanel(viewModel = editorViewModel) }
                second(minSize = 100.dp) { ConsolePanel(viewModel = consoleViewModel) }
                splitter { verticalSplitter() }
            }
        }
    }
}