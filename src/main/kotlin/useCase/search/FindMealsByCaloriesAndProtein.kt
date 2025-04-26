package org.damascus.useCase.search

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class FindMealsByCaloriesAndProtein(
    private val mealRepository: MealRepository,
) {

    operator fun invoke(targetCalories: Double, targetProtein: Double): List<Meal> {
        val targetMeal = mealRepository.getAllMeals()
            .filter { meal ->
                isWithinCaloriesRange(calories = meal.nutrition.calories, target = targetCalories)
                        &&
                        isWithinProteinRange(protein = meal.nutrition.protein, target = targetProtein)
            }
        return targetMeal
    }

    private fun isWithinCaloriesRange(calories: Double, target: Double): Boolean {
        return calories in (target - CALORIES_TOLERANCE)..(target + CALORIES_TOLERANCE)
    }

    private fun isWithinProteinRange(protein: Double, target: Double): Boolean {
        return protein in (target - PROTEIN_TOLERANCE)..(target + PROTEIN_TOLERANCE)
    }

    private companion object {
        const val CALORIES_TOLERANCE = 15.0
        const val PROTEIN_TOLERANCE = 3.0
    }

}