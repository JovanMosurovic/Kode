package io.github.jovanmosurovic.kode.utils

import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter

object FileOperations {

    private val ktsFilter = FilenameFilter { _, name -> name.endsWith(".kts") }

    fun newFile(parentFrame: Frame? = null): String? {
        val dialog = FileDialog(parentFrame, "New File", FileDialog.SAVE).apply {
            file = "untitled.kts"
            isVisible = true
        }

        val directory = dialog.directory ?: return null
        var filename = dialog.file ?: return null

        if (!filename.endsWith(".kts")) {
            filename = "$filename.kts"
        }

        val file = File(directory, filename)
        file.createNewFile()
        file.writeText("")
        return file.absolutePath
    }

    fun openFile(parentFrame: Frame? = null): String? {
        val dialog = FileDialog(parentFrame, "Open File", FileDialog.LOAD).apply {
            filenameFilter = ktsFilter
            isVisible = true
        }

        val directory = dialog.directory ?: return null
        val filename = dialog.file ?: return null

        if (!filename.endsWith(".kts")) {
            return null
        }

        return File(directory, filename).absolutePath
    }

    fun readFile(path: String): String {
        return File(path).readText()
    }

    fun saveFile(path: String, content: String) {
        File(path).writeText(content)
    }

    fun saveFileAs(content: String, parentFrame: Frame? = null): String? {
        val dialog = FileDialog(parentFrame, "Save As", FileDialog.SAVE).apply {
            file = "untitled.kts"
            isVisible = true
        }

        val directory = dialog.directory ?: return null
        var filename = dialog.file ?: return null

        if (!filename.endsWith(".kts")) {
            filename = "$filename.kts"
        }

        val file = File(directory, filename)
        file.writeText(content)
        return file.absolutePath
    }
}