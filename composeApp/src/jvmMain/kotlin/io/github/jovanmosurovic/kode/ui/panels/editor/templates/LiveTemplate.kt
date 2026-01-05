package io.github.jovanmosurovic.kode.ui.panels.editor.templates

data class LiveTemplate(
    val abbreviation: String,
    val description: String,
    val template: String,
    val cursorOffset: Int = 0  // Offset from start where cursor should be placed
) {
    companion object {
        val templates = listOf(
            LiveTemplate(
                abbreviation = "main",
                description = "Main function",
                template = "fun main(args: Array<String>) {\n    \n}",
                cursorOffset = 35
            ),
            LiveTemplate(
                abbreviation = "fori",
                description = "For loop with index",
                template = "for (i in 0 until count) {\n    \n}",
                cursorOffset = 14
            ),
            LiveTemplate(
                abbreviation = "for",
                description = "For each loop",
                template = "for (item in collection) {\n    \n}",
                cursorOffset = 5
            ),
            LiveTemplate(
                abbreviation = "foreach",
                description = "forEach with lambda",
                template = "collection.forEach { item ->\n    \n}",
                cursorOffset = 0
            ),
            LiveTemplate(
                abbreviation = "if",
                description = "If statement",
                template = "if (condition) {\n    \n}",
                cursorOffset = 4
            ),
            LiveTemplate(
                abbreviation = "ifelse",
                description = "If-else statement",
                template = "if (condition) {\n    \n} else {\n    \n}",
                cursorOffset = 4
            ),
            LiveTemplate(
                abbreviation = "when",
                description = "When expression",
                template = "when (value) {\n    condition -> result\n    else -> default\n}",
                cursorOffset = 6
            ),
            LiveTemplate(
                abbreviation = "fun",
                description = "Function declaration",
                template = "fun functionName(): Unit {\n    \n}",
                cursorOffset = 4
            ),
            LiveTemplate(
                abbreviation = "println",
                description = "Print line",
                template = "println(\"\")",
                cursorOffset = 9
            ),
            LiveTemplate(
                abbreviation = "try",
                description = "Try-catch block",
                template = "try {\n    \n} catch (e: Exception) {\n    \n}",
                cursorOffset = 10
            ),
            LiveTemplate(
                abbreviation = "while",
                description = "While loop",
                template = "while (condition) {\n    \n}",
                cursorOffset = 7
            ),
            LiveTemplate(
                abbreviation = "val",
                description = "Immutable variable",
                template = "val name = value",
                cursorOffset = 4
            )
        )

        fun findMatching(prefix: String): List<LiveTemplate> {
            if (prefix.isBlank()) return emptyList()
            return templates.filter {
                it.abbreviation.startsWith(prefix, ignoreCase = true)
            }.sortedBy { it.abbreviation }
        }
    }
}
