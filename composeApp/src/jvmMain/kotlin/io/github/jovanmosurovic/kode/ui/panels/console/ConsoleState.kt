package io.github.jovanmosurovic.kode.ui.panels.console

data class ConsoleState(
    val output: String = "",
    val input: String = "",
    val isRunning: Boolean = false,
    val hasError: Boolean = false,
    val currentProcess: Process? = null
) {
    companion object {
        fun initial() = ConsoleState()
    }
}