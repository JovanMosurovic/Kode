package io.github.jovanmosurovic.kode.runner

import io.github.jovanmosurovic.kode.ui.panels.console.ConsoleViewModel
import io.github.jovanmosurovic.kode.ui.panels.editor.EditorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CodeRunner(
    private val consoleViewModel: ConsoleViewModel,
    private val scope: CoroutineScope
) {
    private var currentJob: Job? = null

    private companion object {
        private val KOTLINC_EXECUTABLE: String
            get() {
                val os = System.getProperty("os.name").lowercase()
                return when {
                    os.contains("win") -> "kotlinc.bat"
                    else -> "kotlinc"
                }
            }
        private const val KOTLINC_OPTIONS: String = "-script"
    }

    fun runKotlinScript(editorViewModel: EditorViewModel) {
        val fileName = editorViewModel.state.value.currentFile

        currentJob = scope.launch(Dispatchers.IO) {
            try {
                val processBuilder = ProcessBuilder(KOTLINC_EXECUTABLE, KOTLINC_OPTIONS, fileName)
                val process = processBuilder.start()

                withContext(Dispatchers.Main) {
                    consoleViewModel.clearConsole()
                    consoleViewModel.appendOutput("[INFO] Running Kotlin script...\n")
                    consoleViewModel.setRunning(true)
                    consoleViewModel.setProcess(process)
                }

                // stdout
                launch(Dispatchers.IO) {
                    process.inputStream.bufferedReader().use { reader ->
                        val buffer = CharArray(1)
                        while (isActive && reader.read(buffer) != -1) {
                            withContext(Dispatchers.Main) {
                                consoleViewModel.appendOutput(String(buffer))
                            }
                        }
                    }
                }

                // stderr
                launch(Dispatchers.IO) {
                    process.errorStream.bufferedReader().use { reader ->
                        val buffer = CharArray(1)
                        while (isActive && reader.read(buffer) != -1) {
                            withContext(Dispatchers.Main) {
                                consoleViewModel.appendOutput(String(buffer), isError = true)
                            }
                        }
                    }
                }

                val exitCode = process.waitFor()

                withContext(Dispatchers.Main) {
                    consoleViewModel.appendOutput("\n[INFO] Exit code: $exitCode\n")
                    consoleViewModel.setRunning(false)
                    consoleViewModel.setProcess(null)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    if (e !is java.io.IOException || !e.message?.contains("Stream closed")!!) {
                        consoleViewModel.appendOutput("[ERROR] ${e.message}\n", isError = true)
                    }
                    consoleViewModel.setRunning(false)
                }
            }
        }
    }

    fun sendInput(input: String) {
        scope.launch(Dispatchers.IO) {
            consoleViewModel.state.value.currentProcess?.let { process ->
                try {
                    process.outputStream.use { output ->
                        output.write("$input\n".toByteArray(Charsets.UTF_8))
                        output.flush()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        consoleViewModel.appendOutput("[ERROR] Failed to send input: ${e.message}\n", isError = true)
                    }
                }
            }
        }
    }


    fun stopExecution() {
        consoleViewModel.state.value.currentProcess?.let { process ->
            process.destroyForcibly()
            currentJob?.cancel()
            consoleViewModel.appendOutput("\n[INFO] Process terminated by user\n")
            consoleViewModel.setRunning(false)
            consoleViewModel.setProcess(null)
        }
    }
}