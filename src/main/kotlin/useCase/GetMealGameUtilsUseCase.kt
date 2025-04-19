package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.model.MealOptions

class GetMealGameUtilsUseCase(
    private val mealRepository: MealRepository
) {

    fun getValidMeals(
        predicate: ((Meal) -> Boolean)
    ): List<Meal> {
        return mealRepository.getAllMeals()
            .filter { meal -> predicate(meal) }
    }

    fun getWrongIngredients(
        validMeals: List<Meal>,
        currentRandomMeal: Meal,
        correctIngredient: String,
        minNeededWrongIngredients: Int = 2
    ): List<String> {
        return validMeals
            .filter { meal -> meal != currentRandomMeal }
            .flatMap { meal -> meal.ingredients }
            .filter { ingredient -> ingredient != correctIngredient }
            .distinct()
            .shuffled()
            .take(minNeededWrongIngredients)
            .toList()
    }

    fun getShuffledOptions(mealOptions: MealOptions): List<String> {
        return (mealOptions.wrongMealIngredients + mealOptions.correctMealIngredient).shuffled()
    }

}