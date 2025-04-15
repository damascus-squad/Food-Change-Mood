package org.damascus.useCase

import org.damascus.model.Meal

class GetEggFreeSweetUseCase(private val mealRepo: MealRepository) {
    operator fun invoke(): Meal = mealRepo.getEggFreeSweet()
}