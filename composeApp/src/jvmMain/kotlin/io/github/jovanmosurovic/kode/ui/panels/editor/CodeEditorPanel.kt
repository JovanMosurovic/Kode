package io.github.jovanmosurovic.kode.ui.panels.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jovanmosurovic.kode.ui.panels.Panel
import io.github.jovanmosurovic.kode.ui.theme.KodeTheme
import kotlinx.coroutines.launch

@Composable
fun CodeEditorPanel(
    viewModel: EditorViewModel,
    modifier: Modifier = Modifier
) {
    Panel {
        val state by viewModel.state.collectAsState()
        val scrollState = rememberScrollState()
        val syntaxHighlighter = rememberSyntaxHighlighter()
        val editorColors = KodeTheme.editorColors
        val scope = rememberCoroutineScope()

        val highlightedCode = remember(state.code) {
            syntaxHighlighter.highlight(state.code)
        }

        Row(
            modifier = modifier
                .fillMaxSize()
                .background(editorColors.background)
        ) {
            LineNumbers(
                lineCount = state.lineCount,
                scrollState = scrollState
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = highlightedCode,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState, enabled = false)
                        .padding(8.dp),
                    style = TextStyle(
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                )

                BasicTextField(
                    value = state.code,
                    onValueChange = { newCode ->
                        viewModel.updateCode(newCode)

                        val lines = newCode.count { it == '\n' }
                        val lineHeight = 20
                        val targetScroll = lines * lineHeight

                        scope.launch {
                            scrollState.animateScrollTo(targetScroll.coerceAtLeast(0))
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(8.dp),
                    textStyle = TextStyle(
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = 14.sp,
                        color = editorColors.identifier.copy(alpha = 0f),
                        lineHeight = 20.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        Box {
                            if (state.code.isEmpty()) {
                                Text(
                                    text = "// Write your Kotlin code here...",
                                    style = TextStyle(
                                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                        fontSize = 14.sp,
                                        color = editorColors.comment.copy(alpha = 0.6f),
                                        lineHeight = 20.sp
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
    }
}
