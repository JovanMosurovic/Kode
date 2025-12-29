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
import androidx.compose.ui.graphics.Color
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
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp
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
                modifier = Modifier.height(36.dp)
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
//                    Text("Run")
                }
            }

            Spacer(modifier = Modifier.weight(0.02f))

            FloatingActionButton(
                onClick = {},
                modifier = Modifier.height(36.dp)
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
            shape = RoundedCornerShape(7.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(title)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(0.dp, 0.dp)
        ) {
            items.forEach { item ->
                if (item.isSeparator) {
                    HorizontalDivider()
                } else {
                    DropdownMenuItem(
                        text = { Text(item.label) },
                        leadingIcon = item.icon?.let {
                            {
                                Icon(
                                    imageVector = it,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        onClick = {
                            item.onClick()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

