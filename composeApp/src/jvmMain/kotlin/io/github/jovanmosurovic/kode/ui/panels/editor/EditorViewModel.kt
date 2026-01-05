package io.github.jovanmosurovic.kode.ui.panels.editor

import io.github.jovanmosurovic.kode.ui.panels.editor.templates.LiveTemplate
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

    private val _requestSelectAll = MutableStateFlow(false)
    val requestSelectAll: StateFlow<Boolean> = _requestSelectAll.asStateFlow()

    private val _requestPaste = MutableStateFlow(false)
    val requestPaste: StateFlow<Boolean> = _requestPaste.asStateFlow()

    fun updateCode(newCode: String) {
        _state.update { it.copy(
            code = newCode,
            isDirty = true,
            lineCount = newCode.count { char -> char == '\n' } + 1
        ) }
    }

    fun updateCursorPosition(offset: Int, text: String) {
        val lines = text.take(offset.coerceAtMost(text.length)).lines()
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

    fun selectAll() {
        _requestSelectAll.value = true
        _requestFocus.value = true
    }

    fun clearSelectAllRequest() {
        _requestSelectAll.value = false
    }

    fun paste() {
        _requestPaste.value = true
        _requestFocus.value = true
    }

    fun clearPasteRequest() {
        _requestPaste.value = false
    }

    fun getClipboardContent(): String? {
        return try {
            val clipboard = java.awt.Toolkit.getDefaultToolkit().systemClipboard
            clipboard.getData(java.awt.datatransfer.DataFlavor.stringFlavor) as? String
        } catch (_: Exception) {
            null
        }
    }

    fun checkAndShowTemplatePopup(cursorOffset: Int) {
        val currentState = _state.value
        val (wordStart, currentWord) = getCurrentWord(currentState.code, cursorOffset)

        if (currentWord.length >= 2) {
            val matching = LiveTemplate.findMatching(currentWord)

            _state.update {
                it.copy(
                    showTemplatePopup = matching.isNotEmpty(),
                    matchingTemplates = matching,
                    selectedTemplateIndex = 0,
                    currentWordStart = wordStart
                )
            }
        } else {
            hideTemplatePopup()
        }
    }

    fun showTemplatePopup(cursorOffset: Int) {
        val currentState = _state.value
        val (wordStart, currentWord) = getCurrentWord(currentState.code, cursorOffset)
        val matching = LiveTemplate.findMatching(currentWord)

        _state.update {
            it.copy(
                showTemplatePopup = matching.isNotEmpty(),
                matchingTemplates = matching,
                selectedTemplateIndex = 0,
                currentWordStart = wordStart
            )
        }
    }

    fun hideTemplatePopup() {
        _state.update {
            it.copy(
                showTemplatePopup = false,
                matchingTemplates = emptyList(),
                selectedTemplateIndex = 0
            )
        }
    }

    fun selectNextTemplate() {
        _state.update {
            if (it.matchingTemplates.isEmpty()) it
            else it.copy(
                selectedTemplateIndex = (it.selectedTemplateIndex + 1) % it.matchingTemplates.size
            )
        }
    }

    fun selectPreviousTemplate() {
        _state.update {
            if (it.matchingTemplates.isEmpty()) it
            else it.copy(
                selectedTemplateIndex = if (it.selectedTemplateIndex <= 0)
                    it.matchingTemplates.size - 1
                else it.selectedTemplateIndex - 1
            )
        }
    }

    fun applyTemplate(template: LiveTemplate, cursorOffset: Int): Pair<String, Int> {
        val currentState = _state.value
        val code = currentState.code
        val wordStart = currentState.currentWordStart

        val newCode = code.take(wordStart) +
                template.template +
                code.substring(cursorOffset)

        val newCursorPosition = wordStart + template.cursorOffset

        _state.update {
            it.copy(
                code = newCode,
                isDirty = true,
                lineCount = newCode.count { char -> char == '\n' } + 1,
                showTemplatePopup = false,
                matchingTemplates = emptyList(),
                selectedTemplateIndex = 0
            )
        }

        return Pair(newCode, newCursorPosition)
    }

    private fun getCurrentWord(text: String, cursorOffset: Int): Pair<Int, String> {
        if (cursorOffset <= 0 || cursorOffset > text.length) return Pair(cursorOffset, "")

        var start = cursorOffset - 1
        while (start >= 0 && text[start].isLetterOrDigit()) {
            start--
        }
        start++

        return Pair(start, text.substring(start, cursorOffset))
    }
}