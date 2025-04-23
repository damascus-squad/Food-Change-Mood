package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class IdentifyIraqiMealsUseCase(
    private val repo: MealRepository
) {
    operator fun invoke(nationality: String = "iraqi"): List<Meal> {
        val iraqiMeals = repo.getAllMeals().filter { meal ->
            (meal.description.lowercase().contains(nationality) ||
                    meal.tags.joinToString().lowercase().contains(nationality))
        }

        return iraqiMeals
    }

}