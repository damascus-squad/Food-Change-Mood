package org.damascus.logic


import org.damascus.model.Meal

class GetFirstTenMealsUseCase(
    private val mealRepo: MealRepository,
) {
    operator fun invoke(): List<Meal> = mealRepo.getAllMeals().take(10)
}