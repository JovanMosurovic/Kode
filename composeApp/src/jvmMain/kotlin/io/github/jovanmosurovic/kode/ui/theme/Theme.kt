package io.github.jovanmosurovic.kode.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class EditorColors(
    val keyword: Color,
    val string: Color,
    val comment: Color,
    val number: Color,
    val function: Color,
    val type: Color,
    val identifier: Color,
    val operator: Color,
    val bracket: Color,
    val background: Color,
    val lineNumberBackground: Color,
    val lineNumber: Color
)

val LocalEditorColors = staticCompositionLocalOf {
    EditorColors(
        keyword = Color.Unspecified,
        string = Color.Unspecified,
        comment = Color.Unspecified,
        number = Color.Unspecified,
        function = Color.Unspecified,
        type = Color.Unspecified,
        identifier = Color.Unspecified,
        operator = Color.Unspecified,
        bracket = Color.Unspecified,
        background = Color.Unspecified,
        lineNumberBackground = Color.Unspecified,
        lineNumber = Color.Unspecified
    )
}

private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
)

private val LightEditorColors = EditorColors(
    keyword = codeKeywordLight,
    string = codeStringLight,
    comment = codeCommentLight,
    number = codeNumberLight,
    function = codeFunctionLight,
    type = codeTypeLight,
    identifier = codeIdentifierLight,
    operator = codeOperatorLight,
    bracket = codeBracketLight,
    background = codeEditorBackgroundLight,
    lineNumberBackground = codeLineNumberBackgroundLight,
    lineNumber = codeLineNumberLight
)

private val DarkEditorColors = EditorColors(
    keyword = codeKeywordDark,
    string = codeStringDark,
    comment = codeCommentDark,
    number = codeNumberDark,
    function = codeFunctionDark,
    type = codeTypeDark,
    identifier = codeIdentifierDark,
    operator = codeOperatorDark,
    bracket = codeBracketDark,
    background = codeEditorBackgroundDark,
    lineNumberBackground = codeLineNumberBackgroundDark,
    lineNumber = codeLineNumberDark
)

@Composable
fun KodeTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val editorColors = if (darkTheme) DarkEditorColors else LightEditorColors

    CompositionLocalProvider(LocalEditorColors provides editorColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object KodeTheme {
    val editorColors: EditorColors
        @Composable
        get() = LocalEditorColors.current
}