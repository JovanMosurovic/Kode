package io.github.jovanmosurovic.kode.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.github.jovanmosurovic.kode.utils.AppInfo
import kode.composeapp.generated.resources.Res
import kode.composeapp.generated.resources.github_mark
import kode.composeapp.generated.resources.github_mark_white
import org.jetbrains.compose.resources.painterResource
import java.awt.Desktop
import java.net.URI

@Composable
fun AboutDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.width(500.dp).wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(28.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "About This Project",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Description
                Text(
                    text = "This project was developed as a solution for the JetBrains internship test task " +
                            "“Compose IDE Integration for Kotlin Multiplatform” (December 2025).\n" +
                            "\n" +
                            "The application is a cross-platform GUI tool that allows users to write and execute Kotlin scripts, " +
                            "displaying the live output side-by-side.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Version info
                InfoRow("Version:", AppInfo.VERSION)
                InfoRow("Date:", AppInfo.DATE)
                InfoRow("Contact:", AppInfo.CONTACT)

                Spacer(modifier = Modifier.height(24.dp))

                // GitHub links
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GitHubLink(
                        text = "Project GitHub",
                        url = AppInfo.PROJECT_GITHUB
                    )

                    Spacer(modifier = Modifier.width(40.dp))

                    GitHubLink(
                        text = "Author's GitHub",
                        url = AppInfo.AUTHOR_GITHUB
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
@Composable
private fun GitHubIcon(modifier: Modifier = Modifier) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val isDark = (backgroundColor.red + backgroundColor.green + backgroundColor.blue) / 3f < 0.5f

    Icon(
        painter = painterResource(
            if (isDark) Res.drawable.github_mark_white
            else Res.drawable.github_mark
        ),
        contentDescription = "GitHub",
        modifier = modifier,
        tint = Color.Unspecified
    )
}


@Composable
private fun GitHubLink(text: String, url: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                try {
                    Desktop.getDesktop().browse(URI(url))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .padding(8.dp)
    ) {
        GitHubIcon(modifier = Modifier.size(20.dp))

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
