package org.damascus.useCase.retrieve

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class SortSeafoodMealsByContentUseCase(private val mealRepository: MealRepository) {
    operator fun invoke(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter { meal -> meal.isSeafoodMeal() }
            .sortedByDescending { meal -> meal.nutrition.protein }
    }

    private fun Meal.isSeafoodMeal(): Boolean {
        return this.tags.any { tag -> tag.contains(FOOD_TYPE, ignoreCase = true) }
    }

    private companion object {
        const val FOOD_TYPE = "seafood"
    }

}