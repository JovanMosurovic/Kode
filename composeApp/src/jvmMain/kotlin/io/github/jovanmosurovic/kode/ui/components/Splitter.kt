package io.github.jovanmosurovic.kode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.SplitterScope
import org.jetbrains.skiko.Cursor
import java.awt.Cursor.E_RESIZE_CURSOR
import java.awt.Cursor.N_RESIZE_CURSOR

@OptIn(ExperimentalSplitPaneApi::class)
fun SplitterScope.horizontalSplitter() {
//    visiblePart {
//        Box(
//            Modifier
//                .width(1.dp)
//                .fillMaxHeight()
//                .background(MaterialTheme.colorScheme.outline)
//        )
//    }
    handle {
        Box(
            Modifier
                .markAsHandle()
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(E_RESIZE_CURSOR)))
                .width(8.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}

@OptIn(ExperimentalSplitPaneApi::class)
fun SplitterScope.verticalSplitter() {
//    visiblePart {
//        Box(
//            Modifier
//                .height(1.dp)
//                .fillMaxWidth()
//                .background(MaterialTheme.colorScheme.outline)
//        )
//    }
    handle {
        Box(
            Modifier
                .markAsHandle()
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(N_RESIZE_CURSOR)))
                .height(8.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}