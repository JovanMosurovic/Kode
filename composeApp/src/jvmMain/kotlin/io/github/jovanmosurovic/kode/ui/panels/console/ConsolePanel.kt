package io.github.jovanmosurovic.kode.ui.panels.console

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jovanmosurovic.kode.ui.panels.Panel
import io.github.jovanmosurovic.kode.ui.theme.KodeTheme

@Composable
fun ConsolePanel(
    viewModel: ConsoleViewModel = remember { ConsoleViewModel() },
    modifier: Modifier = Modifier
) {
    Panel {
        val state by viewModel.state.collectAsState()
        val scrollState = rememberScrollState()
        val editorColors = KodeTheme.editorColors
        val errorColor = MaterialTheme.colorScheme.error

        LaunchedEffect(state.output) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }

        val formattedOutput = remember(state.output, editorColors, errorColor) {
            formatConsoleOutputNonComposable(state.output, editorColors, errorColor)
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(editorColors.background)
        ) {
            // Output area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = formattedOutput,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(8.dp),
                    style = TextStyle(
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = 14.sp,
                        color = editorColors.identifier,
                        lineHeight = 20.sp
                    )
                )
            }

            // Input area
            if (state.isRunning) {
                BasicTextField(
                    value = state.input,
                    onValueChange = { viewModel.updateInput(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(editorColors.lineNumberBackground)
                        .padding(8.dp),
                    textStyle = TextStyle(
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = 14.sp,
                        color = editorColors.identifier,
                        lineHeight = 20.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        Box {
                            if (state.input.isEmpty()) {
                                Text(
                                    text = "> ",
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

private fun formatConsoleOutputNonComposable(
    output: String,
    editorColors: io.github.jovanmosurovic.kode.ui.theme.EditorColors,
    errorColor: androidx.compose.ui.graphics.Color
): AnnotatedString {
    return buildAnnotatedString {
        val lines = output.lines()

        lines.forEachIndexed { index, line ->
            when {
                line.startsWith("[ERROR]") || line.contains("error", ignoreCase = true) -> {
                    withStyle(SpanStyle(color = errorColor)) {
                        append(line)
                    }
                }
                line.startsWith("[INFO]") || line.startsWith("INFO:") -> {
                    withStyle(SpanStyle(color = editorColors.function)) {
                        append(line)
                    }
                }
                line.startsWith("[WARNING]") || line.contains("warning", ignoreCase = true) -> {
                    withStyle(SpanStyle(color = editorColors.number)) {
                        append(line)
                    }
                }
                else -> {
                    withStyle(SpanStyle(color = editorColors.identifier)) {
                        append(line)
                    }
                }
            }

            if (index < lines.lastIndex) {
                append("\n")
            }
        }
    }
}
