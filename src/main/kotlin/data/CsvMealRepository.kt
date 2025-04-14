package data

import org.damascus.data.CsvParser
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
class CsvMealRepository(private val parser: CsvParser, private val csvFileReader: CsvFileReader): MealRepository {
    private val allMeals: MutableList<Meal> = mutableListOf()
    override fun getAllMeals(): List<Meal> {
        csvFileReader.readLinesFromFile().map { row->
            allMeals.add(parser.parseLine(row))

        }
        return allMeals
    }

}