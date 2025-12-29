package io.github.jovanmosurovic.kode

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kode.composeapp.generated.resources.Res
import kode.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kode",
        icon = painterResource(Res.drawable.app_icon_32x32)
    ) {
        App()
    }
}