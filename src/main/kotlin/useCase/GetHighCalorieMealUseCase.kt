package org.damascus.useCase

import org.damascus.model.Meal

class GetHighCalorieMealUseCase(private val repo: MealRepository) {
    operator fun invoke(): Meal = repo.getHighCalorieMeal()
}
