package io.github.jovanmosurovic.kode.ui.panels.console

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jovanmosurovic.kode.runner.CodeRunner
import io.github.jovanmosurovic.kode.ui.panels.Panel
import io.github.jovanmosurovic.kode.ui.panels.editor.EditorViewModel
import io.github.jovanmosurovic.kode.ui.theme.KodeTheme

@Composable
fun ConsolePanel(
    codeRunner: CodeRunner,
    viewModel: ConsoleViewModel = remember { ConsoleViewModel() },
    editorViewModel: EditorViewModel? = null,
    modifier: Modifier = Modifier
) {
    Panel {
        val state by viewModel.state.collectAsState()
        val scrollState = rememberScrollState()
        val editorColors = KodeTheme.editorColors
        val errorColor = MaterialTheme.colorScheme.error
        val warningColor = editorColors.warning
        val normalTextColor = MaterialTheme.colorScheme.onSurface

        LaunchedEffect(state.output) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }

        val errors = remember(state.output) {
            ErrorParser.parseErrors(state.output)
        }

        val formattedOutput = remember(state.output, errorColor, warningColor, normalTextColor, errors) {
            formatConsoleOutputWithClickableErrors(
                state.output,
                errorColor,
                warningColor,
                normalTextColor,
                errors
            )
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
                var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
                var hoveredErrorIndex by remember { mutableStateOf<Int?>(null) }

                BasicText(
                    text = formattedOutput,
                    onTextLayout = { textLayoutResult = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(8.dp)
                        .pointerHoverIcon(
                            if (hoveredErrorIndex != null) PointerIcon.Hand
                            else PointerIcon.Default
                        )
                        .pointerInput(formattedOutput) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()

                                    when (event.type) {
                                        // Tracking hover
                                        androidx.compose.ui.input.pointer.PointerEventType.Move -> {
                                            val position = event.changes.first().position
                                            textLayoutResult?.let { layoutResult ->
                                                val offset = layoutResult.getOffsetForPosition(position)
                                                val annotations = formattedOutput.getStringAnnotations(
                                                    tag = "ERROR",
                                                    start = offset,
                                                    end = offset + 1
                                                )
                                                hoveredErrorIndex = annotations.firstOrNull()?.let {
                                                    errors.indexOfFirst { error ->
                                                        "${error.line}:${error.column}" == it.item
                                                    }
                                                }
                                            }
                                        }
                                        // Handle exit
                                        androidx.compose.ui.input.pointer.PointerEventType.Exit -> {
                                            hoveredErrorIndex = null
                                        }
                                    }
                                }
                            }
                        }
                        .pointerInput(formattedOutput) {
                            detectTapGestures { pos ->
                                textLayoutResult?.let { layoutResult ->
                                    val offset = layoutResult.getOffsetForPosition(pos)

                                    val annotations = formattedOutput.getStringAnnotations(
                                        tag = "ERROR",
                                        start = offset,
                                        end = offset + 1
                                    )

                                    annotations.firstOrNull()?.let { annotation ->
                                        val parts = annotation.item.split(":")
                                        if (parts.size >= 2) {
                                            val line = parts[0].toIntOrNull()
                                            val column = parts[1].toIntOrNull()
                                            if (line != null && column != null) {
                                                editorViewModel?.navigateToPosition(line, column)
                                            }
                                        }
                                    }
                                }
                            }
                        },
                    style = TextStyle(
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = 14.sp,
                        color = normalTextColor,
                        lineHeight = 20.sp
                    )
                )
            }

            // Input area
            if (state.isRunning) {
                val focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }

                LaunchedEffect(state.isRunning) {
                    focusRequester.requestFocus()
                }

                BasicTextField(
                    value = state.input,
                    onValueChange = { viewModel.updateInput(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(editorColors.lineNumberBackground)
                        .padding(8.dp)
                        .focusRequester(focusRequester)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyUp) {
                                codeRunner.sendInput(state.input)
                                viewModel.appendOutput("> ${state.input}\n")
                                viewModel.updateInput("")
                                true
                            } else {
                                false
                            }
                        },
                    textStyle = TextStyle(
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontSize = 14.sp,
                        color = editorColors.identifier,
                        lineHeight = 20.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Row {
                            Text(
                                text = "> ",
                                style = TextStyle(
                                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                    fontSize = 14.sp,
                                    color = editorColors.function,
                                    lineHeight = 20.sp
                                )
                            )
                            innerTextField()
                        }
                    }
                )
            }
        }
    }
}

private fun formatConsoleOutputWithClickableErrors(
    output: String,
    errorColor: androidx.compose.ui.graphics.Color,
    warningColor: androidx.compose.ui.graphics.Color,
    normalTextColor: androidx.compose.ui.graphics.Color,
    errors: List<ErrorLocation>
): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0

        errors.sortedBy { it.startIndex }.forEach { error ->
            // Text before error
            if (error.startIndex > currentIndex) {
                val normalText = output.substring(currentIndex, error.startIndex)
                appendNormalText(normalText, errorColor, warningColor, normalTextColor)
            }

            // Clickable error - capturing current length before appending
            val errorText = output.substring(error.startIndex, error.endIndex)
            val startAnnotation = this.length

            withStyle(
                SpanStyle(
                    color = errorColor,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(errorText)
            }

            val endAnnotation = this.length

            addStringAnnotation(
                tag = "ERROR",
                annotation = "${error.line}:${error.column}",
                start = startAnnotation,
                end = endAnnotation
            )

            currentIndex = error.endIndex
        }

        // Text after error
        if (currentIndex < output.length) {
            val remainingText = output.substring(currentIndex)
            appendNormalText(remainingText, errorColor, warningColor, normalTextColor)
        }
    }
}

private fun AnnotatedString.Builder.appendNormalText(
    text: String,
    errorColor: androidx.compose.ui.graphics.Color,
    warningColor: androidx.compose.ui.graphics.Color,
    normalTextColor: androidx.compose.ui.graphics.Color
) {
    val lines = text.lines()
    lines.forEachIndexed { index, line ->
        val color = when {
            line.startsWith("[ERROR]") || line.contains("error", ignoreCase = true) -> errorColor
            line.startsWith("[WARNING]") || line.contains("warning", ignoreCase = true) -> warningColor
            else -> normalTextColor
        }

        withStyle(SpanStyle(color = color)) {
            append(line)
        }

        if (index < lines.lastIndex) {
            append("\n")
        }
    }
}
