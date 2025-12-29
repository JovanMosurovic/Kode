package io.github.jovanmosurovic.kode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ViewColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import io.github.jovanmosurovic.kode.ui.layout.PanelLayout

@Composable
fun LayoutSelector(
    currentLayout: PanelLayout,
    onLayoutChange: (PanelLayout) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Outlined.ViewColumn,
                contentDescription = "Change Layout",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset((-80).dp, 4.dp),
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 2.dp
        ) {
            Text(
                text = "Horizontal Layouts",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LayoutOption(
                layout = PanelLayout.HORIZONTAL_50_50,
                isSelected = currentLayout == PanelLayout.HORIZONTAL_50_50,
                onClick = {
                    onLayoutChange(PanelLayout.HORIZONTAL_50_50)
                    expanded = false
                }
            )

            LayoutOption(
                layout = PanelLayout.HORIZONTAL_70_30,
                isSelected = currentLayout == PanelLayout.HORIZONTAL_70_30,
                onClick = {
                    onLayoutChange(PanelLayout.HORIZONTAL_70_30)
                    expanded = false
                }
            )

            LayoutOption(
                layout = PanelLayout.HORIZONTAL_30_70,
                isSelected = currentLayout == PanelLayout.HORIZONTAL_30_70,
                onClick = {
                    onLayoutChange(PanelLayout.HORIZONTAL_30_70)
                    expanded = false
                }
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Vertical Layouts",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LayoutOption(
                layout = PanelLayout.VERTICAL_50_50,
                isSelected = currentLayout == PanelLayout.VERTICAL_50_50,
                onClick = {
                    onLayoutChange(PanelLayout.VERTICAL_50_50)
                    expanded = false
                }
            )

            LayoutOption(
                layout = PanelLayout.VERTICAL_70_30,
                isSelected = currentLayout == PanelLayout.VERTICAL_70_30,
                onClick = {
                    onLayoutChange(PanelLayout.VERTICAL_70_30)
                    expanded = false
                }
            )

            LayoutOption(
                layout = PanelLayout.VERTICAL_30_70,
                isSelected = currentLayout == PanelLayout.VERTICAL_30_70,
                onClick = {
                    onLayoutChange(PanelLayout.VERTICAL_30_70)
                    expanded = false
                }
            )
        }
    }
}

@Composable
private fun LayoutOption(
    layout: PanelLayout,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                if (isSelected) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                else MaterialTheme.colorScheme.background
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = layout.displayName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        LayoutPreview(layout = layout, isSelected = isSelected)
    }
}

@Composable
private fun LayoutPreview(
    layout: PanelLayout,
    isSelected: Boolean
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = Modifier
            .size(width = 60.dp, height = 40.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        when (layout) {
            PanelLayout.HORIZONTAL_50_50 -> HorizontalPreview(0.5f)
            PanelLayout.HORIZONTAL_70_30 -> HorizontalPreview(0.7f)
            PanelLayout.HORIZONTAL_30_70 -> HorizontalPreview(0.3f)
            PanelLayout.VERTICAL_50_50 -> VerticalPreview(0.5f)
            PanelLayout.VERTICAL_70_30 -> VerticalPreview(0.7f)
            PanelLayout.VERTICAL_30_70 -> VerticalPreview(0.3f)
        }
    }
}

@Composable
private fun HorizontalPreview(ratio: Float) {
    Row(modifier = Modifier.fillMaxSize().padding(4.dp)) {
        Box(
            modifier = Modifier
                .weight(ratio)
                .fillMaxHeight()
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
        )
        Spacer(modifier = Modifier.width(2.dp))
        Box(
            modifier = Modifier
                .weight(1f - ratio)
                .fillMaxHeight()
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f))
        )
    }
}

@Composable
private fun VerticalPreview(ratio: Float) {
    Column(modifier = Modifier.fillMaxSize().padding(4.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(ratio)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f - ratio)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f))
        )
    }
}

private val PanelLayout.displayName: String
    get() = when (this) {
        PanelLayout.HORIZONTAL_50_50 -> "50 / 50 "
        PanelLayout.HORIZONTAL_70_30 -> "70 / 30 "
        PanelLayout.HORIZONTAL_30_70 -> "30 / 70 "
        PanelLayout.VERTICAL_50_50 -> "50 / 50 "
        PanelLayout.VERTICAL_70_30 -> "70 / 30 "
        PanelLayout.VERTICAL_30_70 -> "30 / 70 "
    }
