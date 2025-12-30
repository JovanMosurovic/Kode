package io.github.jovanmosurovic.kode

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kode.composeapp.generated.resources.Res
import kode.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import io.github.jovanmosurovic.kode.utils.FileConstants

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kode",
        state = rememberWindowState(width = 1200.dp, height = 800.dp),
        icon = painterResource(Res.drawable.app_icon_32x32)
    ) {
        App()
    }


    Runtime.getRuntime().addShutdownHook(Thread {
        FileConstants.clearDefaultScript()
    })
}