package org.damascus.logic

import org.damascus.model.GameResult
import org.damascus.model.Meal
import org.damascus.model.MealOptions
import org.damascus.useCase.suggest.GetMealGameUtilsUseCase

class GuessIngredientGame(
    private val getMealGameUtilsUseCase: GetMealGameUtilsUseCase
) {

    private val validMeals: List<Meal> = getMealGameUtilsUseCase.getValidMeals { meal: Meal ->
        meal.ingredients.size >= MIN_NEEDED_TOTAL_INGREDIENTS
    }

    fun playIngredientGame(
        getUserChoice: (mealName: String, options: List<String>) -> Int?,
        onFeedback: (message: String) -> Unit
    ): GameResult {
        val randomMeals = validMeals.shuffled()

        var score = 0
        var correctAnswers = 0
        var randomMealsIndex = 0

        val gameMessages = mutableListOf<String>()

        while (correctAnswers < MAX_ALLOWED_CORRECT_ANSWERS && randomMealsIndex < randomMeals.size) {
            val currentMeal = randomMeals[randomMealsIndex]
            val correctIngredient = currentMeal.ingredients.random()
            val wrongIngredients =
                getMealGameUtilsUseCase.getWrongIngredients(validMeals, currentMeal, correctIngredient)

            if (wrongIngredients.size < MIN_NEEDED_WRONG_INGREDIENTS) {
                randomMealsIndex++
                continue
            }

            val mealOptions = MealOptions(correctIngredient, wrongIngredients)
            val options = getMealGameUtilsUseCase.getShuffledOptions(mealOptions)


            val userChoice = getUserChoice(currentMeal.name, options)

            if (userChoice == null || userChoice !in 1..options.size) {
                onFeedback("❌ Invalid choice. Skipping this round.")
                randomMealsIndex++
                continue
            }

            val selected = options[userChoice - 1]
            if (selected == correctIngredient) {
                score += 1000
                correctAnswers++
                onFeedback("✅ Correct! Score: $score\n")
            } else {
                onFeedback("❌ Wrong! Correct answer was: $correctIngredient")
                break
            }

            randomMealsIndex++
        }

        return GameResult(score, correctAnswers, gameMessages)
    }


    companion object {
        const val MAX_ALLOWED_CORRECT_ANSWERS = 15
        const val MIN_NEEDED_WRONG_INGREDIENTS = 2
        const val MIN_NEEDED_TOTAL_INGREDIENTS = 3
    }
}