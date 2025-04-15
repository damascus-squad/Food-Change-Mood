package org.damascus.data.source

import data.CsvFileReader
import org.damascus.data.CsvParser
import org.damascus.model.Meal

class CsvMealDataSource(
    private val parser: CsvParser,
    private val fileReader: CsvFileReader
) {
    fun loadMeals(): List<Meal> {
        return fileReader.readLinesFromFile().map { parser.parseLine(it) }
    }
}
