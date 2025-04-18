package data

import org.damascus.data.source.CsvMealDataSource
import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class MealRepositoryImpl(private val csvMealDataSource: CsvMealDataSource) : MealRepository {
    override fun getAllMeals(): List<Meal> = csvMealDataSource.loadMeals()

    private val suggestedEggFreeSweets = mutableSetOf<Int>()
    override fun getEggFreeSweet(): Meal {
        val allMeals = getAllMeals()
        val eggFree = allMeals.filter { meal ->
            meal.tags.any { it.contains("sweet", true) } &&
                    !meal.ingredients.any { it.contains("egg", true) } &&
                    !suggestedEggFreeSweets.contains(meal.id)
        }

        if (eggFree.isEmpty()) throw NoSuchElementException("No more egg-free sweets left.")

        val next = eggFree.random()
        suggestedEggFreeSweets.add(next.id)
        return next
    }


}