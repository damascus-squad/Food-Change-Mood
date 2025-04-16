package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class GetHealthyFastFoodMealsUseCase(private val mealsRepository: MealRepository) {

    fun getHealthyFastFoodMeals(): List<String> {
        val meals = mealsRepository.getAllMeals()
        val (avgFat, avgSaturatedFat, avgCarbs) = computeNutritionAverages(meals)
        return meals
            .filter { meal -> meal.isHealthyFastFoodMeal(avgFat, avgSaturatedFat, avgCarbs) }
            .take(TOP_MEALS)
            .map { meal -> meal.name }
    }

    fun List<Double?>.calculateAverage(): Double =
        filterNotNull().let { if (it.isNotEmpty()) it.average() else 0.0 }

    private fun computeNutritionAverages(meals: List<Meal>): Triple<Double, Double, Double> {
        val avgFat = meals.map { it.nutrition.totalFat }.calculateAverage()
        val avgSaturatedFat = meals.map { it.nutrition.saturatedFat }.calculateAverage()
        val avgCarbs = meals.map { it.nutrition.carbohydrates }.calculateAverage()
        return Triple(avgFat, avgSaturatedFat, avgCarbs)
    }

    private fun Meal.isHealthyFastFoodMeal(avgFat: Double, avgSaturatedFat: Double, avgCarbs: Double): Boolean {
        return minutes?.let { preparationTime -> preparationTime <= 15 } == true &&
                nutrition.totalFat?.let { totalFat -> totalFat < avgFat } == true &&
                nutrition.saturatedFat?.let { saturatedFat -> saturatedFat < avgSaturatedFat } == true &&
                nutrition.carbohydrates?.let { carbohydrates -> carbohydrates < avgCarbs } == true
    }
    companion object{
        const val TOP_MEALS =10
    }
}