package io.github.jovanmosurovic.kode.ui.panels.editor

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jovanmosurovic.kode.ui.theme.KodeTheme

@Composable
fun LineNumbers(
    lineCount: Int,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    val editorColors = KodeTheme.editorColors

    Column(
        modifier = modifier
            .width(50.dp)
            .fillMaxHeight()
            .background(editorColors.lineNumberBackground)
            .verticalScroll(scrollState, enabled = false)
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        repeat(lineCount) { index ->
            Text(
                text = "${index + 1}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    color = editorColors.lineNumber
                ),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
        }
    }
}