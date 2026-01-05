package io.github.jovanmosurovic.kode.ui.panels.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import io.github.jovanmosurovic.kode.ui.theme.KodeTheme

class SyntaxHighlighter(
    private val keywordColor: Color,
    private val stringColor: Color,
    private val commentColor: Color,
    private val numberColor: Color,
    private val functionColor: Color,
    private val typeColor: Color,
    private val identifierColor: Color,
    private val operatorColor: Color,
    private val bracketColor: Color
) {

    private val kotlinKeywords = setOf(
        "fun", "val", "var", "class", "interface", "object", "enum",
        "if", "else", "when", "for", "while", "do", "return",
        "import", "package", "as", "in", "is", "public", "private",
        "protected", "internal", "abstract", "final", "open", "override",
        "suspend", "inline", "data", "sealed", "companion", "const",
        "this", "super", "null", "true", "false", "try", "catch", "finally",
        "throw", "break", "continue"
    )

    private val kotlinTypes = setOf(
        "Int", "String", "Boolean", "Double", "Float", "Long", "Short",
        "Byte", "Char", "Unit", "Any", "Nothing", "List", "Map", "Set",
        "Array", "Pair", "Triple", "MutableList", "MutableMap", "MutableSet"
    )

    fun highlight(code: String): AnnotatedString {
        if (code.isEmpty()) return AnnotatedString("")

        return buildAnnotatedString {
            var currentIndex = 0

            while (currentIndex < code.length) {
                when {
                    // String literals
                    code[currentIndex] == '"' -> {
                        val endIndex = findStringEnd(code, currentIndex + 1)
                        withStyle(SpanStyle(color = stringColor)) {
                            append(code.substring(currentIndex, endIndex))
                        }
                        currentIndex = endIndex
                    }

                    // Single line comments
                    code.startsWith("//", currentIndex) -> {
                        val endIndex = code.indexOf('\n', currentIndex).let {
                            if (it == -1) code.length else it + 1
                        }
                        withStyle(SpanStyle(color = commentColor)) {
                            append(code.substring(currentIndex, endIndex))
                        }
                        currentIndex = endIndex
                    }

                    // Multi-line comments
                    code.startsWith("/*", currentIndex) -> {
                        val endIndex = code.indexOf("*/", currentIndex + 2).let {
                            if (it == -1) code.length else it + 2
                        }
                        withStyle(SpanStyle(color = commentColor)) {
                            append(code.substring(currentIndex, endIndex))
                        }
                        currentIndex = endIndex
                    }

                    // Numbers
                    code[currentIndex].isDigit() -> {
                        val endIndex = findNumberEnd(code, currentIndex)
                        withStyle(SpanStyle(color = numberColor)) {
                            append(code.substring(currentIndex, endIndex))
                        }
                        currentIndex = endIndex
                    }

                    // Identifiers and keywords
                    code[currentIndex].isLetter() || code[currentIndex] == '_' -> {
                        val endIndex = findIdentifierEnd(code, currentIndex)
                        val word = code.substring(currentIndex, endIndex)

                        val color = when {
                            word in kotlinKeywords -> keywordColor
                            word in kotlinTypes -> typeColor
                            word[0].isUpperCase() -> typeColor
                            endIndex < code.length && code[endIndex] == '(' -> functionColor
                            else -> identifierColor
                        }

                        withStyle(SpanStyle(color = color)) {
                            append(word)
                        }
                        currentIndex = endIndex
                    }

                    // Operators and brackets
                    code[currentIndex] in "+-*/=<>!&|(){}[],.;:" -> {
                        val color = if (code[currentIndex] in "(){}[]") bracketColor else operatorColor
                        withStyle(SpanStyle(color = color)) {
                            append(code[currentIndex])
                        }
                        currentIndex++
                    }

                    // Whitespace and other
                    else -> {
                        append(code[currentIndex])
                        currentIndex++
                    }
                }
            }
        }
    }

    private fun findStringEnd(code: String, startIndex: Int): Int {
        var i = startIndex
        while (i < code.length) {
            if (code[i] == '"' && (i == 0 || code[i - 1] != '\\')) {
                return i + 1
            }
            i++
        }
        return code.length
    }

    private fun findNumberEnd(code: String, startIndex: Int): Int {
        var i = startIndex
        while (i < code.length && (code[i].isDigit() || code[i] == '.' || code[i] == 'f' || code[i] == 'L')) {
            i++
        }
        return i
    }

    private fun findIdentifierEnd(code: String, startIndex: Int): Int {
        var i = startIndex
        while (i < code.length && (code[i].isLetterOrDigit() || code[i] == '_')) {
            i++
        }
        return i
    }
}

@Composable
fun rememberSyntaxHighlighter(): SyntaxHighlighter {
    val editorColors = KodeTheme.editorColors

    return remember(editorColors) {
        SyntaxHighlighter(
            keywordColor = editorColors.keyword,
            stringColor = editorColors.string,
            commentColor = editorColors.comment,
            numberColor = editorColors.number,
            functionColor = editorColors.function,
            typeColor = editorColors.type,
            identifierColor = editorColors.identifier,
            operatorColor = editorColors.operator,
            bracketColor = editorColors.bracket
        )
    }
}