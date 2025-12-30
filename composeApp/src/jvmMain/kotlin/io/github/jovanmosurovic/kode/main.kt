package io.github.jovanmosurovic.kode

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.jovanmosurovic.kode.utils.AppInfo
import io.github.jovanmosurovic.kode.utils.FileConstants

fun main() = application {
    val windowState = rememberWindowState(
        width = 1200.dp,
        height = 800.dp,
        position = WindowPosition(alignment = Alignment.Center),
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = AppInfo.APP_NAME,
        state = windowState
    ) {
        App(onCloseRequest = ::exitApplication)
    }


    Runtime.getRuntime().addShutdownHook(Thread {
        FileConstants.clearDefaultScript()
    })
}