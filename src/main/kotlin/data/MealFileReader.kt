package data

import org.damascus.utils.CSV_FILE_PATH
import java.io.File
import java.io.IOException

class MealFileReader(private val filePath: String = CSV_FILE_PATH) {

    fun readLinesFromFile(): List<String> {
        val lines = mutableListOf<String>()
        val file = getCsvFile()

        val reader = file.bufferedReader()

        val allLines = reader.readLines()

        var buffer = StringBuilder()
        var isInQuotedBlock = false

        for (i in 1 until allLines.size) {
            val line = allLines[i]

            if (line.isBlank() && !isInQuotedBlock) continue

            buffer.appendLine(line)

            val quoteCount = line.count { it == '"' }
            if (quoteCount % 2 != 0) {
                isInQuotedBlock = !isInQuotedBlock
            }

            if (!isInQuotedBlock) {

                val record = escapeSpecialCharacters(buffer.toString().trim())
                if (record.isNotBlank()) {
                    lines.add(record)
                }
                buffer = StringBuilder()
            }
        }

        return lines
    }

    private fun getCsvFile(): File {
        val foodCsvFile = File(filePath)
        if (foodCsvFile.exists()) {
            return foodCsvFile
        }
        throw IOException("File Not Found")
    }

    private fun escapeSpecialCharacters(input: String): String {
        return input
            .replace("\\t", "\t")
            .replace("\\n", "\n")
            .replace("\\r", "\r")
            .replace("\\\"", "\"")
            .replace("\"\"", "\"")
    }
}
