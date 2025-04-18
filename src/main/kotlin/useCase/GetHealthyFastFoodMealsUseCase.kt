package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.model.NutritionAverages

class GetHealthyFastFoodMealsUseCase(private val mealsRepository: MealRepository) {


    operator fun invoke(): List<String> {
        val meals = mealsRepository.getAllMeals()
        val averages = computeNutritionAverages(meals)
        return meals
            .filter { meal -> meal.isHealthyFastFoodMeal(averages) }
            .take(TOP_MEALS)
            .map { meal -> meal.name }
    }


    fun List<Double?>.calculateAverage(): Double =
        filterNotNull().let { if (it.isNotEmpty()) it.average() else 0.0 }


    private fun computeNutritionAverages(meals: List<Meal>): NutritionAverages {
        val avgFat = meals.map { it.nutrition.totalFat }.calculateAverage()
        val avgSaturatedFat = meals.map { it.nutrition.saturatedFat }.calculateAverage()
        val avgCarbs = meals.map { it.nutrition.carbohydrates }.calculateAverage()
        return NutritionAverages(avgFat, avgSaturatedFat, avgCarbs)
    }


    private fun Meal.isHealthyFastFoodMeal(averages: NutritionAverages): Boolean {
        return minutes?.let { preparationTime -> preparationTime <= 15 } == true &&
                nutrition.totalFat?.let { totalFat -> totalFat < averages.fat } == true &&
                nutrition.saturatedFat?.let { saturatedFat -> saturatedFat < averages.saturatedFat } == true &&
                nutrition.carbohydrates?.let { carbohydrates -> carbohydrates < averages.carbs } == true
    }


    companion object {
        const val TOP_MEALS = 10
    }
}