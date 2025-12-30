package io.github.jovanmosurovic.kode.ui.panels.editor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditorViewModel {

    private val _state = MutableStateFlow(EditorState.initial())
    val state: StateFlow<EditorState> = _state.asStateFlow()

    fun updateCode(newCode: String) {
        _state.update { it.copy(
            code = newCode,
            isDirty = true,
            lineCount = newCode.count { char -> char == '\n' } + 1
        ) }
    }

    fun updateState(newState: EditorState) {
        _state.value = newState
    }

    fun clearCode() {
        _state.update { EditorState.initial() }
    }

    fun openFile(filePath: String, content: String) {
        _state.update { it.copy(
            code = content,
            currentFile = filePath,
            isDirty = false,
            lineCount = content.count { char -> char == '\n' } + 1
        ) }
    }

    fun markAsSaved() {
        _state.update { it.copy(isDirty = false) }
    }
}