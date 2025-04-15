package org.damascus.data.source

import data.MealFileReader
import org.damascus.data.*
import org.damascus.model.Meal

class CsvMealDataSource(
    private val parser: MealDataParser,
    private val fileReader: MealFileReader
) {
    fun loadMeals(): List<Meal> {
        return fileReader.readLinesFromFile().map { parser.parseLine(it) }
    }
}
