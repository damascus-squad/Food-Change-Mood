package org.damascus.useCase.retrieve

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class IdentifyMealsByNationalityUseCase(
    private val repo: MealRepository
) {
    operator fun invoke(nationality: String = "iraqi"): List<Meal> {
        return repo.getAllMeals().filter { meal -> isMealFromNationality(meal, nationality) }
    }

    private fun isMealFromNationality(meal: Meal, nationality: String): Boolean {
        return meal.name.contains(nationality, ignoreCase = true) ||
                meal.description.contains(nationality, ignoreCase = true) ||
                meal.tags.any { it.contains(nationality, ignoreCase = true) }
    }

}