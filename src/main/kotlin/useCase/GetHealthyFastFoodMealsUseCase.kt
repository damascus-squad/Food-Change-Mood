package org.damascus.useCase

import model.Nutrition
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.model.NutritionAverages

class GetHealthyFastFoodMealsUseCase(private val mealsRepository: MealRepository) {


    operator fun invoke(topMeals: Int = 10): List<String> {
        val meals = mealsRepository.getAllMeals()
        val averages = computeNutritionAverages(meals)
        return meals
            .filter { meal -> meal isHealthyFastFoodBy averages }
            .take(topMeals)
            .map { meal -> meal.name }
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
        return totalFat.let { totalFat -> totalFat < averages.averageFat }
                && saturatedFat.let { saturatedFat -> saturatedFat < averages.averageSaturatedFat }
                && carbohydrates.let { carbohydrates -> carbohydrates < averages.averageCarbs }
    }

    private infix fun Meal.isHealthyFastFoodBy(averages: NutritionAverages): Boolean {
        return minutes.let { preparationTime -> preparationTime <= MAXIMUM_MINUTES_TO_PREPARE_MEAL }
                && nutrition isNutritionallyBetterThan averages

    }

    companion object {
        const val TOP_MEALS = 10
        const val MAXIMUM_MINUTES_TO_PREPARE_MEAL = 15
    }
}