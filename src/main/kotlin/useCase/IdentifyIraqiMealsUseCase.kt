package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class IdentifyIraqiMealsUseCase(
    private val repo : MealRepository
) {
    operator fun invoke(): List<Meal>{
        val iraqiMeals = repo.getAllMeals().filter { meal ->
            (meal.description.lowercase().contains("iraqi") ||
             meal.tags.joinToString().lowercase().contains("iraqi"))
        }

        return iraqiMeals
    }
}