package io.github.jovanmosurovic.kode.ui.panels.editor

import io.github.jovanmosurovic.kode.ui.panels.editor.templates.LiveTemplate
import io.github.jovanmosurovic.kode.utils.FileConstants

data class EditorState(
    val code: String = "",
    val isDirty: Boolean = false,
    val currentFile: String = FileConstants.defaultScriptPath,
    val lineCount: Int = 1,
    val columnCount: Int = 1,
    val cursorLine: Int = 1,
    val cursorColumn: Int = 1,

    val showTemplatePopup: Boolean = false,
    val matchingTemplates: List<LiveTemplate> = emptyList(),
    val selectedTemplateIndex: Int = 0,
    val currentWordStart: Int = 0
) {
    companion object {
        fun initial(): EditorState {
            FileConstants.ensureDefaultScriptExists()
            return EditorState()
        }
    }
}