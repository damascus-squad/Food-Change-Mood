package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class GetHighCalorieMealUseCase(
    private val mealRepo: MealRepository
) {
    private val suggestedHighCalorieMeals = mutableSetOf<Int>()

    operator fun invoke(threshold: Int = 700): List<Meal> {
        val allMeals = mealRepo.getAllMeals()

        val highCalMeals = allMeals.filter {
            it.nutrition.calories > threshold && !suggestedHighCalorieMeals.contains(it.id)
        }

        if (highCalMeals.isEmpty()) throw NoSuchElementException("No more high-calorie meals.")

        val selected = highCalMeals.random()
        suggestedHighCalorieMeals.add(selected.id)

        return highCalMeals
    }
}
