package io.github.jovanmosurovic.kode.ui.menu

import io.github.jovanmosurovic.kode.ui.panels.editor.EditorState
import io.github.jovanmosurovic.kode.utils.FileOperations

class FileMenuActions(
    private val getState: () -> EditorState,
    private val updateState: (EditorState) -> Unit
) {

    fun onNewFile() {
        val newPath = FileOperations.newFile() ?: return
        updateState(
            EditorState(
                code = "",
                isDirty = false,
                currentFile = newPath,
                lineCount = 1,
                columnCount = 1
            )
        )
    }

    fun onOpenFile() {
        val filePath = FileOperations.openFile() ?: return
        val content = FileOperations.readFile(filePath)
        val lineCount = content.lines().size.coerceAtLeast(1)

        updateState(
            getState().copy(
                code = content,
                isDirty = false,
                currentFile = filePath,
                lineCount = lineCount
            )
        )
    }

    fun onSave() {
        val state = getState()
        FileOperations.saveFile(state.currentFile, state.code)
        updateState(state.copy(isDirty = false))
    }

    fun onSaveAs() {
        val state = getState()
        val newPath = FileOperations.saveFileAs(state.code) ?: return
        updateState(
            state.copy(
                currentFile = newPath,
                isDirty = false
            )
        )
    }
}