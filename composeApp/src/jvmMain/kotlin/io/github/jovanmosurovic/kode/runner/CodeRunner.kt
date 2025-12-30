package io.github.jovanmosurovic.kode.runner

import io.github.jovanmosurovic.kode.ui.panels.console.ConsoleViewModel
import io.github.jovanmosurovic.kode.ui.panels.editor.EditorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CodeRunner(
    private val consoleViewModel: ConsoleViewModel,
    private val scope: CoroutineScope
) {
    fun runKotlinScript(editorViewModel: EditorViewModel) {
        val fileName = editorViewModel.state.value.currentFile
        scope.launch {
            consoleViewModel.clearConsole()
            consoleViewModel.setRunning(true)
            consoleViewModel.appendOutput("[INFO] Running Kotlin script... \n")

            try {
                val processBuilder = ProcessBuilder("kotlinc.bat", "-script", "\"$fileName\"")
                    .apply {
                        redirectErrorStream(false)
                    }

                val process = withContext(Dispatchers.IO) {
                    processBuilder.start()
                }
                consoleViewModel.setProcess(process)

                // stdout
                launch(Dispatchers.IO) {
                    process.inputStream.bufferedReader().useLines { lines ->
                        lines.forEach { line ->
                            withContext(Dispatchers.Main) {
                                consoleViewModel.appendOutput("$line\n")
                            }
                        }
                    }
                }

                // stderr
                launch(Dispatchers.IO) {
                    process.errorStream.bufferedReader().useLines { lines ->
                        lines.forEach { line ->
                            withContext(Dispatchers.Main) {
                                consoleViewModel.appendOutput("[ERROR] $line\n", isError = true)
                            }
                        }
                    }
                }

                // Wait for process to complete
                val exitCode = withContext(Dispatchers.IO) {
                    process.waitFor()
                }

                consoleViewModel.appendOutput("\n[INFO] Process finished with exit code: $exitCode\n")

            } catch (e: Exception) {
                consoleViewModel.appendOutput("[ERROR] Failed to run script: ${e.message}\n", isError = true)
            } finally {
                consoleViewModel.setRunning(false)
                consoleViewModel.setProcess(null)
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
            consoleViewModel.appendOutput("\n[INFO] Process terminated by user\n")
            consoleViewModel.setRunning(false)
            consoleViewModel.setProcess(null)
        }
    }
}