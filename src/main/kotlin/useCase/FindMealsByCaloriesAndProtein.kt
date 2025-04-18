package org.damascus.useCase

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
        val tolerance = 15.0
        return calories in (target - tolerance)..(target + tolerance)
    }

    private fun isWithinProteinRange(protein: Double, target: Double): Boolean {
        val tolerance = 3.0
        return protein in (target - tolerance)..(target + tolerance)
    }

}
