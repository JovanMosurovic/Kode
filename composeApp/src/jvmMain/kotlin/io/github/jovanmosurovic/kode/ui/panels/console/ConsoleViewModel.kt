package io.github.jovanmosurovic.kode.ui.panels.console

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConsoleViewModel {

    private val _state = MutableStateFlow(ConsoleState.initial())
    val state: StateFlow<ConsoleState> = _state.asStateFlow()

    fun appendOutput(text: String, isError: Boolean = false) {
        _state.update {
            it.copy(
                output = it.output + text,
                hasError = isError || it.hasError
            )
        }
    }

    fun updateInput(newInput: String) {
        _state.update { it.copy(input = newInput) }
    }

    fun clearConsole() {
        _state.update { ConsoleState.initial() }
    }

    fun setRunning(running: Boolean) {
        _state.update { it.copy(isRunning = running) }
    }

    fun setProcess(process: Process?) {
        _state.update { it.copy(currentProcess = process) }
    }
}