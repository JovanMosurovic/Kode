package io.github.jovanmosurovic.kode.ui.panels.editor

data class EditorState(
    val code: String = "",
    val isDirty: Boolean = false,
    val currentFile: String? = null,
    val lineCount: Int = 1,
    val columnCount: Int = 1,
) {
    companion object {
        fun initial() = EditorState()
    }
}