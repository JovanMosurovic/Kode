package io.github.jovanmosurovic.kode.ui.menubar

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val label: String,
    val icon: ImageVector? = null,
    val onClick: () -> Unit = {},
    val isSeparator: Boolean = false
) {
    companion object {
        val Separator = MenuItem(label = "", isSeparator = true)
    }
}
