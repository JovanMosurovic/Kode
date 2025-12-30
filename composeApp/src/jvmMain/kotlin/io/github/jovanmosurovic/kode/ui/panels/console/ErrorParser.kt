package io.github.jovanmosurovic.kode.ui.panels.console

data class ErrorLocation(
    val line: Int,
    val column: Int,
    val message: String,
    val startIndex: Int,
    val endIndex: Int
)

object ErrorParser {
    private val kotlinErrorPattern = Regex("""(\S+\.kts):(\d+):(\d+):\s*(error|warning):\s*(.+)""")

    fun parseErrors(text: String): List<ErrorLocation> {
        val errors = mutableListOf<ErrorLocation>()

        kotlinErrorPattern.findAll(text).forEach { match ->
            val line = match.groupValues[2].toIntOrNull() ?: return@forEach
            val column = match.groupValues[3].toIntOrNull() ?: return@forEach
            val message = match.groupValues[5]

            errors.add(
                ErrorLocation(
                    line = line,
                    column = column,
                    message = message,
                    startIndex = match.range.first,
                    endIndex = match.range.last + 1
                )
            )
        }

        return errors
    }
}
