package org.damascus.useCase.suggest

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class GetEggFreeSweetUseCase(
    private val mealRepo: MealRepository
) {
    operator fun invoke(): Meal = mealRepo.getEggFreeSweet()
}