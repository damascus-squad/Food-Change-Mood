package org.damascus.useCase.suggest

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class GetHighCalorieMealUseCase(
    private val repo: MealRepository
) {
    operator fun invoke(): List<Meal> = repo.getAllMeals().filter { it.nutrition.calories > 700 }
}
