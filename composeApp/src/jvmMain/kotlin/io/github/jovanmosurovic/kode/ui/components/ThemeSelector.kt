package io.github.jovanmosurovic.kode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import io.github.jovanmosurovic.kode.ui.theme.AppTheme

@Composable
fun ThemeSelector(
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(8.dp)
                ),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    contentDescription = "Theme",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = currentTheme.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(0.dp, 4.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            AppTheme.entries.forEach { theme ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(getThemePreviewColor(theme))
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = theme.displayName,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    onClick = {
                        onThemeChange(theme)
                        expanded = false
                    },
                    modifier = Modifier.background(
                        if (theme == currentTheme) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    }
}

@Composable
private fun getThemePreviewColor(theme: AppTheme) = when (theme) {
    AppTheme.LIGHT -> Color(0xF5F5F5F5)
    AppTheme.DARK -> Color(0xFF1E1F22)
    AppTheme.ATOM -> Color(0xFF282C34)
    AppTheme.MONOKAI -> Color(0xFF272822)
    AppTheme.DARCULA -> Color(0xFF2B2B2B)
}
