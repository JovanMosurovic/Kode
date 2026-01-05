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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
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

private const val LINE_HEIGHT = 24
private const val CHAR_WIDTH = 9
private const val LINE_NUMBER_WIDTH = 58
private const val PADDING = 8

@Composable
fun CodeEditorPanel(
    viewModel: EditorViewModel,
    modifier: Modifier = Modifier
) {
    Panel {
        val state by viewModel.state.collectAsState()
        val navigateToLine by viewModel.navigateToLine.collectAsState()
        val requestFocus by viewModel.requestFocus.collectAsState()
        val requestSelectAll by viewModel.requestSelectAll.collectAsState()
        val requestPaste by viewModel.requestPaste.collectAsState()
        val scrollState = rememberScrollState()
        val syntaxHighlighter = rememberSyntaxHighlighter()
        val editorColors = KodeTheme.editorColors
        val scope = rememberCoroutineScope()
        val focusRequester = remember { FocusRequester() }

        var editorPosition by remember { mutableStateOf(IntOffset.Zero) }
        var textFieldValue by remember { mutableStateOf(TextFieldValue(text = state.code)) }

        LaunchedEffect(state.code) {
            if (textFieldValue.text != state.code) {
                textFieldValue = TextFieldValue(text = state.code, selection = TextRange(state.code.length))
            }
        }

        // Focus request
        LaunchedEffect(requestFocus) {
            if (requestFocus) {
                focusRequester.requestFocus()
                viewModel.clearFocusRequest()
            }
        }

        // Select All
        LaunchedEffect(requestSelectAll) {
            if (requestSelectAll) {
                focusRequester.requestFocus()
                textFieldValue = textFieldValue.copy(selection = TextRange(0, textFieldValue.text.length))
                viewModel.clearSelectAllRequest()
            }
        }

        // Paste
        LaunchedEffect(requestPaste) {
            if (requestPaste) {
                focusRequester.requestFocus()
                viewModel.getClipboardContent()?.let { clipboardText ->
                    val selection = textFieldValue.selection
                    val newText = textFieldValue.text.replaceRange(selection.start, selection.end, clipboardText)
                    val newCursor = selection.start + clipboardText.length
                    textFieldValue = TextFieldValue(text = newText, selection = TextRange(newCursor))
                    viewModel.updateCode(newText)
                }
                viewModel.clearPasteRequest()
            }
        }

        // Navigation to the error line
        LaunchedEffect(navigateToLine) {
            navigateToLine?.let { (line, column) ->
                val offset = calculateOffset(state.code, line, column)
                textFieldValue = textFieldValue.copy(selection = TextRange(offset))
                viewModel.updateCursorPosition(offset, state.code)
                scrollState.animateScrollTo(((line - 1) * LINE_HEIGHT).coerceAtLeast(0))
                viewModel.clearNavigation()
            }
        }

        val highlightedCode = remember(state.code, syntaxHighlighter) { syntaxHighlighter.highlight(state.code) }

        val popupPosition = remember(state.cursorLine, state.cursorColumn, editorPosition, scrollState.value) {
            val lineIndex = (state.cursorLine - 1).coerceAtLeast(0)
            IntOffset(
                x = editorPosition.x + LINE_NUMBER_WIDTH + (state.cursorColumn * CHAR_WIDTH),
                y = editorPosition.y + PADDING + (lineIndex * LINE_HEIGHT) - scrollState.value - LINE_HEIGHT
            )
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .onPreviewKeyEvent { handleKeyEvent(it, state, viewModel, textFieldValue) { textFieldValue = it } }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(editorColors.background)
                    .onGloballyPositioned { coords ->
                        val pos = coords.positionInRoot()
                        editorPosition = IntOffset(pos.x.toInt(), pos.y.toInt())
                    }
            ) {
                LineNumbers(lineCount = state.lineCount, scrollState = scrollState)

                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    BasicTextField(
                        value = textFieldValue,
                        onValueChange = { newValue ->
                            textFieldValue = newValue
                            if (newValue.text != state.code) viewModel.updateCode(newValue.text)
                            viewModel.updateCursorPosition(newValue.selection.start, newValue.text)
                            viewModel.checkAndShowTemplatePopup(newValue.selection.start)
                            scope.launch {
                                val targetScroll = newValue.text.count { it == '\n' } * LINE_HEIGHT
                                scrollState.animateScrollTo(targetScroll.coerceAtLeast(0))
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(PADDING.dp)
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontSize = 16.sp,
                            color = editorColors.identifier.copy(alpha = 0f),
                            lineHeight = LINE_HEIGHT.sp
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            Box {
                                Text(
                                    text = highlightedCode,
                                    style = TextStyle(
                                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                                        fontSize = 16.sp,
                                        lineHeight = LINE_HEIGHT.sp
                                    )
                                )
                                innerTextField()
                            }
                        }
                    )
                }
            }

            if (state.showTemplatePopup && state.matchingTemplates.isNotEmpty()) {
                LiveTemplatePopup(
                    templates = state.matchingTemplates,
                    selectedIndex = state.selectedTemplateIndex,
                    onSelect = { template ->
                        val (newCode, newCursor) = viewModel.applyTemplate(template, textFieldValue.selection.start)
                        textFieldValue = TextFieldValue(text = newCode, selection = TextRange(newCursor))
                    },
                    onDismiss = { viewModel.hideTemplatePopup() },
                    position = popupPosition
                )
            }
        }
    }
}

private fun calculateOffset(code: String, line: Int, column: Int): Int {
    val lines = code.lines()
    var offset = 0
    for (i in 0 until (line - 1).coerceAtMost(lines.size - 1)) {
        offset += lines[i].length + 1
    }
    return offset + (column - 1).coerceAtLeast(0).coerceAtMost(lines.getOrNull(line - 1)?.length ?: 0)
}

private fun handleKeyEvent(
    keyEvent: KeyEvent,
    state: EditorState,
    viewModel: EditorViewModel,
    textFieldValue: TextFieldValue,
    updateTextField: (TextFieldValue) -> Unit
): Boolean {
    if (keyEvent.type != KeyEventType.KeyDown) return false

    return when {
        keyEvent.key == Key.Spacebar && keyEvent.isCtrlPressed -> {
            viewModel.showTemplatePopup(textFieldValue.selection.start)
            true
        }
        keyEvent.key == Key.Tab && state.showTemplatePopup -> {
            applySelectedTemplate(state, viewModel, textFieldValue, updateTextField)
            true
        }
        keyEvent.key == Key.DirectionDown && state.showTemplatePopup -> {
            viewModel.selectNextTemplate()
            true
        }
        keyEvent.key == Key.DirectionUp && state.showTemplatePopup -> {
            viewModel.selectPreviousTemplate()
            true
        }
        keyEvent.key == Key.Enter && state.showTemplatePopup -> {
            applySelectedTemplate(state, viewModel, textFieldValue, updateTextField)
            true
        }
        keyEvent.key == Key.Escape && state.showTemplatePopup -> {
            viewModel.hideTemplatePopup()
            true
        }
        else -> false
    }
}

private fun applySelectedTemplate(
    state: EditorState,
    viewModel: EditorViewModel,
    textFieldValue: TextFieldValue,
    updateTextField: (TextFieldValue) -> Unit
) {
    state.matchingTemplates.getOrNull(state.selectedTemplateIndex)?.let { template ->
        val (newCode, newCursor) = viewModel.applyTemplate(template, textFieldValue.selection.start)
        updateTextField(TextFieldValue(text = newCode, selection = TextRange(newCursor)))
    }
}