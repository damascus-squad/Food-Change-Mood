package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class SortSeafoodMealsByContentUseCase(private val mealRepository: MealRepository) {
    operator fun invoke(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter {meal->meal.isSeafoodMeal()}
            .sortedByDescending { meal->meal.nutrition.protein }
    }

    private fun Meal.isSeafoodMeal():Boolean{
        return this.tags.any{tag->tag.contains("seafood",ignoreCase = true)}
    }


}