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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jovanmosurovic.kode.ui.panels.Panel
import io.github.jovanmosurovic.kode.ui.panels.editor.templates.LiveTemplatePopup
import io.github.jovanmosurovic.kode.ui.theme.KodeTheme
import kotlinx.coroutines.launch

@Composable
fun CodeEditorPanel(
    viewModel: EditorViewModel,
    modifier: Modifier = Modifier
) {
    Panel {
        val state by viewModel.state.collectAsState()
        val navigateToLine by viewModel.navigateToLine.collectAsState()
        val requestFocus by viewModel.requestFocus.collectAsState()
        val scrollState = rememberScrollState()
        val syntaxHighlighter = rememberSyntaxHighlighter()
        val editorColors = KodeTheme.editorColors
        val scope = rememberCoroutineScope()
        val focusRequester = remember { FocusRequester() }

        var editorPosition by remember { mutableStateOf(IntOffset.Zero) }

        var textFieldValue by remember {
            mutableStateOf(TextFieldValue(text = state.code))
        }

        // Key event handler function
        val handleKeyEvent: (KeyEvent) -> Boolean = { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown) {
                when {
                    // Ctrl+Space to show templates
                    keyEvent.key == Key.Spacebar && keyEvent.isCtrlPressed -> {
                        viewModel.showTemplatePopup(textFieldValue.selection.start)
                        true
                    }
                    // Tab to apply first/selected template
                    keyEvent.key == Key.Tab && state.showTemplatePopup -> {
                        val template = state.matchingTemplates.getOrNull(state.selectedTemplateIndex)
                        if (template != null) {
                            val (newCode, newCursor) = viewModel.applyTemplate(
                                template,
                                textFieldValue.selection.start
                            )
                            textFieldValue = TextFieldValue(
                                text = newCode,
                                selection = TextRange(newCursor)
                            )
                        }
                        true
                    }
                    // Arrow down
                    keyEvent.key == Key.DirectionDown && state.showTemplatePopup -> {
                        viewModel.selectNextTemplate()
                        true
                    }
                    // Arrow up
                    keyEvent.key == Key.DirectionUp && state.showTemplatePopup -> {
                        viewModel.selectPreviousTemplate()
                        true
                    }
                    // Enter to apply selected
                    keyEvent.key == Key.Enter && state.showTemplatePopup -> {
                        val template = state.matchingTemplates.getOrNull(state.selectedTemplateIndex)
                        if (template != null) {
                            val (newCode, newCursor) = viewModel.applyTemplate(
                                template,
                                textFieldValue.selection.start
                            )
                            textFieldValue = TextFieldValue(
                                text = newCode,
                                selection = TextRange(newCursor)
                            )
                        }
                        true
                    }
                    // Escape to close
                    keyEvent.key == Key.Escape && state.showTemplatePopup -> {
                        viewModel.hideTemplatePopup()
                        true
                    }
                    else -> false
                }
            } else false
        }

        LaunchedEffect(state.code) {
            if (textFieldValue.text != state.code) {
                textFieldValue = TextFieldValue(
                    text = state.code,
                    selection = TextRange(state.code.length)
                )
            }
        }

        val highlightedCode = remember(state.code) {
            syntaxHighlighter.highlight(state.code)
        }

        // Handle focus request
        LaunchedEffect(requestFocus) {
            if (requestFocus) {
                focusRequester.requestFocus()
                viewModel.clearFocusRequest()
            }
        }

        // Navigation to the error line
        LaunchedEffect(navigateToLine) {
            navigateToLine?.let { (line, column) ->
                val code = state.code
                val lines = code.lines()

                var offset = 0
                for (i in 0 until (line - 1).coerceAtMost(lines.size - 1)) {
                    offset += lines[i].length + 1
                }
                offset += (column - 1).coerceAtLeast(0).coerceAtMost(
                    lines.getOrNull(line - 1)?.length ?: 0
                )

                textFieldValue = textFieldValue.copy(
                    selection = TextRange(offset)
                )

                viewModel.updateCursorPosition(offset, code)

                val lineHeight = 20
                val targetScroll = (line - 1) * lineHeight
                scrollState.animateScrollTo(targetScroll.coerceAtLeast(0))

                viewModel.clearNavigation()
            }
        }

        // Popup position
        val popupPosition = remember(state.cursorLine, state.cursorColumn, editorPosition) {
            val lineHeight = 24
            val charWidth = 9
            IntOffset(
                x = editorPosition.x + 58 + (state.cursorColumn * charWidth),
                y = editorPosition.y + (state.cursorLine * lineHeight) - scrollState.value + 20
            )
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .onPreviewKeyEvent(handleKeyEvent)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(editorColors.background)
                    .onGloballyPositioned { coordinates ->
                        val position = coordinates.positionInRoot()
                        editorPosition = IntOffset(position.x.toInt(), position.y.toInt())
                    }
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
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                    )

                    BasicTextField(
                        value = textFieldValue,
                        onValueChange = { newValue ->
                            textFieldValue = newValue
                            if (newValue.text != state.code) {
                                viewModel.updateCode(newValue.text)
                            }

                            viewModel.updateCursorPosition(newValue.selection.start, newValue.text)

                            viewModel.checkAndShowTemplatePopup(newValue.selection.start)

                            val lines = newValue.text.count { it == '\n' }
                            val lineHeight = 20
                            val targetScroll = lines * lineHeight

                            scope.launch {
                                scrollState.animateScrollTo(targetScroll.coerceAtLeast(0))
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(8.dp)
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontSize = 16.sp,
                            color = editorColors.identifier.copy(alpha = 0f),
                            lineHeight = 24.sp
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            Box {
                                if (state.code.isEmpty()) {
                                    Text(
                                        text = "// Write your Kotlin code here...",
                                        style = TextStyle(
                                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                            fontSize = 16.sp,
                                            color = editorColors.comment.copy(alpha = 0.6f),
                                            lineHeight = 24.sp
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            // Live template popup
            if (state.showTemplatePopup && state.matchingTemplates.isNotEmpty()) {
                LiveTemplatePopup(
                    templates = state.matchingTemplates,
                    selectedIndex = state.selectedTemplateIndex,
                    onSelect = { template ->
                        val (newCode, newCursor) = viewModel.applyTemplate(
                            template,
                            textFieldValue.selection.start
                        )
                        textFieldValue = TextFieldValue(
                            text = newCode,
                            selection = TextRange(newCursor)
                        )
                    },
                    onDismiss = { viewModel.hideTemplatePopup() },
                    position = popupPosition
                )
            }
        }
    }
}
