package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class IdentifyIraqiMealsUseCase(
    private val repo: MealRepository
) {
    operator fun invoke(): List<Meal> {
        val iraqiMeals = repo.getAllMeals().filter { meal ->
            (meal.description.lowercase().contains(IRAQ_NATIONALITY) ||
                    meal.tags.joinToString().lowercase().contains(IRAQ_NATIONALITY))
        }

        return iraqiMeals
    }

    private companion object {
        const val IRAQ_NATIONALITY = "iraqi"
    }
}