package data

import org.damascus.data.source.CsvMealDataSource
import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class MealRepositoryImpl(private val csvMealDataSource: CsvMealDataSource) : MealRepository {
    override fun getAllMeals(): List<Meal> = csvMealDataSource.loadMeals()

    private val suggestedHighCalorieMeals = mutableSetOf<Int>()

    override fun getHighCalorieMeal(): List<Meal> {
        val allMeals = getAllMeals()

        val highCalMeals = allMeals.filter {
            it.nutrition.calories > 700 && !suggestedHighCalorieMeals.contains(it.id)
        }

        if (highCalMeals.isEmpty()) throw NoSuchElementException("No more high-calorie meals.")

        val selected = highCalMeals.random()
        suggestedHighCalorieMeals.add(selected.id)

        return highCalMeals
    }
}