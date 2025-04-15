package data

import org.damascus.data.CsvParser
import org.damascus.data.source.CsvMealDataSource
import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class CsvMealRepository(private val csvMealDataSource: CsvMealDataSource) : MealRepository {
    override fun getAllMeals(): List<Meal> = csvMealDataSource.loadMeals()
}