package io.github.jovanmosurovic.kode.ui.panels.editor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditorViewModel {

    private val _state = MutableStateFlow(EditorState.initial())
    val state: StateFlow<EditorState> = _state.asStateFlow()

    private val _navigateToLine = MutableStateFlow<Pair<Int, Int>?>(null)
    val navigateToLine: StateFlow<Pair<Int, Int>?> = _navigateToLine.asStateFlow()

    private val _requestFocus = MutableStateFlow(false)
    val requestFocus: StateFlow<Boolean> = _requestFocus.asStateFlow()

    fun updateCode(newCode: String) {
        _state.update { it.copy(
            code = newCode,
            isDirty = true,
            lineCount = newCode.count { char -> char == '\n' } + 1
        ) }
    }

    fun updateCursorPosition(offset: Int, text: String) {
        val lines = text.substring(0, offset.coerceAtMost(text.length)).lines()
        val line = lines.size
        val column = lines.lastOrNull()?.length?.plus(1) ?: 1

        _state.update { it.copy(
            cursorLine = line,
            cursorColumn = column
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

    fun navigateToPosition(line: Int, column: Int) {
        _navigateToLine.value = Pair(line, column)
        _requestFocus.value = true
    }

    fun clearNavigation() {
        _navigateToLine.value = null
    }

    fun clearFocusRequest() {
        _requestFocus.value = false
    }
}