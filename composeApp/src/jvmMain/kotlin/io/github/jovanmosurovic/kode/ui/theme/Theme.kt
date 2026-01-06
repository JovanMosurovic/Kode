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
    val lineNumber: Color,
    val warning: Color
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
        lineNumber = Color.Unspecified,
        warning = Color.Unspecified
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
    lineNumber = codeLineNumberLight,
    warning = warningLight
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
    lineNumber = codeLineNumberDark,
    warning = warningDark
)

private val AtomColorScheme = darkColorScheme(
    primary = Color(0xFF61AFEF),
    onPrimary = Color.White,
    secondary = Color(0xFF98C379),
    onSecondary = Color.White,
    background = atomBackground,
    onBackground = atomOnBackground,
    surface = atomSurface,
    onSurface = atomOnBackground,
    surfaceVariant = atomSurfaceVariant,
    onSurfaceVariant = atomOnBackground,
    error = Color(0xFFE06C75),
    onError = Color.White,
    outline = Color(0xFF4B5263),
    outlineVariant = atomSurfaceVariant,
)

private val MonokaiColorScheme = darkColorScheme(
    primary = Color(0xFFA6E22E),
    onPrimary = Color.Black,
    secondary = Color(0xFF66D9EF),
    onSecondary = Color.Black,
    background = monokaiBackground,
    onBackground = monokaiOnBackground,
    surface = monokaiSurface,
    onSurface = monokaiOnBackground,
    surfaceVariant = monokaiSurfaceVariant,
    onSurfaceVariant = monokaiOnBackground,
    error = Color(0xFFF92672),
    onError = Color.White,
    outline = Color(0xFF75715E),
    outlineVariant = monokaiSurfaceVariant,
)

private val DarculaColorScheme = darkColorScheme(
    primary = Color(0xFF589DF6),
    onPrimary = Color.White,
    secondary = Color(0xFF499C54),
    onSecondary = Color.White,
    background = darculaBackground,
    onBackground = darculaOnBackground,
    surface = darculaSurface,
    onSurface = darculaOnBackground,
    surfaceVariant = darculaSurfaceVariant,
    onSurfaceVariant = darculaOnBackground,
    error = Color(0xFFFF6B68),
    onError = Color.White,
    outline = Color(0xFF606366),
    outlineVariant = darculaSurfaceVariant,
)

private val AtomEditorColors = EditorColors(
    keyword = atomKeyword,
    string = atomString,
    comment = atomComment,
    number = atomNumber,
    function = atomFunction,
    type = atomType,
    identifier = atomIdentifier,
    operator = atomOperator,
    bracket = atomBracket,
    background = atomBackground,
    lineNumberBackground = atomBackground,
    lineNumber = atomLineNumber,
    warning = Color(0xFFE5C07B)
)

private val MonokaiEditorColors = EditorColors(
    keyword = monokaiKeyword,
    string = monokaiString,
    comment = monokaiComment,
    number = monokaiNumber,
    function = monokaiFunction,
    type = monokaiType,
    identifier = monokaiIdentifier,
    operator = monokaiOperator,
    bracket = monokaiBracket,
    background = monokaiBackground,
    lineNumberBackground = monokaiBackground,
    lineNumber = monokaiLineNumber,
    warning = Color(0xFFE6DB74)
)

private val DarculaEditorColors = EditorColors(
    keyword = darculaKeyword,
    string = darculaString,
    comment = darculaComment,
    number = darculaNumber,
    function = darculaFunction,
    type = darculaType,
    identifier = darculaIdentifier,
    operator = darculaOperator,
    bracket = darculaBracket,
    background = darculaBackground,
    lineNumberBackground = darculaBackground,
    lineNumber = darculaLineNumber,
    warning = Color(0xFFBBB529)
)

@Composable
fun KodeTheme(
    theme: AppTheme = AppTheme.DARK,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        AppTheme.LIGHT -> LightColorScheme
        AppTheme.DARK -> DarkColorScheme
        AppTheme.ATOM -> AtomColorScheme
        AppTheme.MONOKAI -> MonokaiColorScheme
        AppTheme.DARCULA -> DarculaColorScheme
    }

    val editorColors = when (theme) {
        AppTheme.LIGHT -> LightEditorColors
        AppTheme.DARK -> DarkEditorColors
        AppTheme.ATOM -> AtomEditorColors
        AppTheme.MONOKAI -> MonokaiEditorColors
        AppTheme.DARCULA -> DarculaEditorColors
    }

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