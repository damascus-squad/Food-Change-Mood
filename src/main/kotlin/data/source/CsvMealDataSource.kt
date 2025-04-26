package org.damascus.data.source

import data.MealFileReader
import org.damascus.data.MealDataParser
import org.damascus.model.Meal

class CsvMealDataSource(
    private val parser: MealDataParser,
    private val fileReader: MealFileReader
) {
    fun loadMeals(): List<Meal> {
        val lines = fileReader.readLinesFromFile()
        val meals = mutableListOf<Meal>()

        lines.forEachIndexed { index, line ->
            try {
                meals.add(parser.parseLine(line))
            } catch (e: Exception) {
                null
            }
        }
        return meals
    }
}
