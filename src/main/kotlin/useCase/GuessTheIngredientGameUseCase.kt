package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.model.MealOptions

class GuessTheIngredientGameUseCase(
    private val mealRepository: MealRepository,
    private val getWrongIngredientUseCase: GetWrongIngredientUseCase
) {

    fun getValidMeals(): List<Meal> = mealRepository.getAllMeals().filter { meal -> meal.ingredients.size >= 3 }

    fun generateMealOptions(currentRandomMeal: Meal, correctIngredient: String): MealOptions {
        val validMeals = getValidMeals()
        val wrongIngredients = getWrongIngredientUseCase(validMeals, currentRandomMeal, correctIngredient)
        return MealOptions(correctIngredient, wrongIngredients)
    }

    fun getShuffledOptions(mealOptions: MealOptions): List<String> {
        return (mealOptions.wrongMealIngredients + mealOptions.correctMealIngredient).shuffled()
    }
}
