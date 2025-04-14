package org.damascus.logic


import org.damascus.model.Meal

class GetFirstTenMealsUseCase(
    val mealRepo: MealRepository,
) {
    fun getMeals(): List<Meal> = mealRepo.getAllMeals().take(10)
}