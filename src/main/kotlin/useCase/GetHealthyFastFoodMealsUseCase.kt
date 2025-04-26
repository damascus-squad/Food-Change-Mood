package org.damascus.useCase

import model.Nutrition
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.model.NutritionAverages

class GetHealthyFastFoodMealsUseCase(private val mealsRepository: MealRepository) {


    operator fun invoke(count: Int = 10): List<Meal> {
        val meals = mealsRepository.getAllMeals()
        val averages = computeNutritionAverages(meals)
        return meals
            .filter { meal -> meal isHealthyFastFoodBy averages }
            .take(count)
    }

    private fun List<Double?>.calculateAverage(): Double =
        filterNotNull().let { if (it.isNotEmpty()) it.average() else 0.0 }

    private fun computeNutritionAverages(meals: List<Meal>): NutritionAverages {
        val avgFat = meals.map { it.nutrition.totalFat }.calculateAverage()
        val avgSaturatedFat = meals.map { it.nutrition.saturatedFat }.calculateAverage()
        val avgCarbs = meals.map { it.nutrition.carbohydrates }.calculateAverage()
        return NutritionAverages(avgFat, avgSaturatedFat, avgCarbs)
    }

    private infix fun Nutrition.isNutritionallyBetterThan(averages: NutritionAverages): Boolean {
        return totalFat < averages.averageFat
                && saturatedFat < averages.averageSaturatedFat
                && carbohydrates < averages.averageCarbs
    }

    private infix fun Meal.isHealthyFastFoodBy(averages: NutritionAverages): Boolean {
        return minutes <= MAXIMUM_MINUTES_TO_PREPARE_MEAL
                && nutrition isNutritionallyBetterThan averages

    }

    private companion object {
        const val MAXIMUM_MINUTES_TO_PREPARE_MEAL = 15
    }
}