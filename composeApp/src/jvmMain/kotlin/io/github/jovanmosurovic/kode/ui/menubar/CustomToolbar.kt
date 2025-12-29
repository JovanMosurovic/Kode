package io.github.jovanmosurovic.kode.ui.menubar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun CustomToolbar(
    onRunClick: () -> Unit,
    onNewFile: () -> Unit,
    onOpenFile: () -> Unit,
    onSaveFile: () -> Unit,
    onExit: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MenuBarDropdown(
                title = "File",
                items = listOf(
                    MenuItem("New", Icons.AutoMirrored.Outlined.NoteAdd, onNewFile),
                    MenuItem("Open", Icons.Outlined.FileOpen, onOpenFile),
                    MenuItem.Separator,
                    MenuItem("Save", Icons.Outlined.Save, onSaveFile),
                    MenuItem("Save As", Icons.Outlined.SaveAs, onSaveFile),
                    MenuItem.Separator,
                    MenuItem("Exit", Icons.AutoMirrored.Outlined.ExitToApp, onExit)
                )
            )

            MenuBarDropdown(
                title = "Edit",
                items = listOf(
                    MenuItem("Cut", Icons.Outlined.ContentCut),
                    MenuItem("Copy", Icons.Outlined.ContentCopy),
                    MenuItem("Paste", Icons.Outlined.ContentPaste)
                )
            )

            MenuBarDropdown(
                title = "Help",
                items = listOf(
                    MenuItem("About", Icons.Outlined.Info)
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            FloatingActionButton(
                onClick = onRunClick,
                modifier = Modifier.height(36.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            FloatingActionButton(
                onClick = {},
                modifier = Modifier.height(36.dp),
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ) {
                Icon(
                    imageVector = Icons.Outlined.Stop,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun MenuBarDropdown(
    title: String,
    items: List<MenuItem>
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box {
        TextButton(
            onClick = { expanded = true },
            modifier = Modifier.height(48.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = if (expanded) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                else MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(0.dp, 0.dp),
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 2.dp
        ) {
            items.forEach { item ->
                if (item.isSeparator) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                } else {
                    DropdownMenuItem(
                        text = {
                            Text(
                                item.label,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = item.icon?.let {
                            {
                                Icon(
                                    imageVector = it,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        onClick = {
                            item.onClick()
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onBackground,
                            leadingIconColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }
    }
}


