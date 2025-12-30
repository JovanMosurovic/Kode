package io.github.jovanmosurovic.kode.utils

import java.io.File

object FileConstants {
    private const val DEFAULT_SCRIPT_NAME = "scratch.kts"

    val defaultScriptPath: String by lazy {
        val projectDir = System.getProperty("user.dir")
        val scriptsDir = File(projectDir, "scripts")
        if (!scriptsDir.exists()) {
            scriptsDir.mkdirs()
        }
        File(scriptsDir, DEFAULT_SCRIPT_NAME).absolutePath
    }

    fun ensureDefaultScriptExists() {
        val file = File(defaultScriptPath)
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    fun clearDefaultScript() {
        val file = File(defaultScriptPath)
        if (file.exists()) {
            file.writeText("")
        }
    }
}