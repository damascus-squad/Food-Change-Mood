package org.damascus.useCase


import org.damascus.model.Meal
import org.damascus.useCase.MealRepository

class GetFirstTenMealsUseCase(
    private val mealRepo: MealRepository,
) {
    operator fun invoke(): List<Meal> = mealRepo.getAllMeals().take(10)
}