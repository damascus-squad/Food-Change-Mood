package org.damascus.logic

import org.damascus.model.Meal

class GetKetoMealUseCase(
    private val mealRepository: MealRepository
) {
    private val suggestedMeals = mutableSetOf<Int>()
    private var currentMeal: Meal? = null

    operator fun invoke(): Meal? {
        val meals = mealRepository.getAllMeals()
        val ketoMeals = meals
            .filter {
                isKetoFriendly(it) &&
                        it.id !in suggestedMeals
            }

        if (ketoMeals.isEmpty()) return null


        val randomMeal = ketoMeals.random()
        currentMeal = randomMeal
        suggestedMeals.add(randomMeal.id)

        return randomMeal
    }

    fun likeCurrentMeal(): Meal? {
        return currentMeal
    }

    fun dislikeAndGetNewMeal(): Meal? {
        return invoke()
    }





    private fun isKetoFriendly(meal: Meal): Boolean {
        val nutrition = meal.nutrition


        if (nutrition.carbohydrates > 50.0 || nutrition.sugar > 5.0) {
            return false
        }


        if (nutrition.calories < 200.0 || nutrition.calories > 800.0) {
            return false
        }

        val fatCalories = nutrition.totalFat * 9
        val proteinCalories = nutrition.protein * 4

        val fatPercentage = (fatCalories / nutrition.calories) * 100
        val proteinPercentage = (proteinCalories / nutrition.calories) * 100


        return fatPercentage >= 70.0 && proteinPercentage in 10.0..20.0
    }
}
