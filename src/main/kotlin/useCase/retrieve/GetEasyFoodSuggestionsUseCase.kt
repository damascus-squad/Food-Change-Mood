package org.damascus.useCase.retrieve

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class GetEasyFoodSuggestionsUseCase(
    private val repo: MealRepository
) {
    operator fun invoke(): List<Meal> {
        val easyMeals = repo.getAllMeals().filter { meal ->
            meal.minutes <= 30 &&
                    meal.ingredientsCount <= 5 &&
                    meal.stepsCount <= 6
        }
        return easyMeals.shuffled().take(10)
    }
}