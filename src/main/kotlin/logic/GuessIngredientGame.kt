package org.damascus.logic

import org.damascus.model.Meal
import org.damascus.model.MealOptions
import org.damascus.useCase.GetMealGameUtilsUseCase

class GuessIngredientGame(
    private val getMealGameUtilsUseCase: GetMealGameUtilsUseCase
) {

    private val validMeals: List<Meal> = getMealGameUtilsUseCase.getValidMeals { meal: Meal ->
        meal.ingredients.size >= MIN_NEEDED_TOTAL_INGREDIENTS
    }

    companion object {
        const val MAX_ALLOWED_CORRECT_ANSWERS = 15
        const val MIN_NEEDED_WRONG_INGREDIENTS = 2
        const val MIN_NEEDED_TOTAL_INGREDIENTS = 3
    }

    fun playIngredientGame() {
        val randomMeals = validMeals.shuffled()

        var score = 0
        var correctAnswers = 0
        var randomMealsIndex = 0

        println("🎉 Welcome to the Ultimate Ingredient Game! 🍔🌮🥗 Let's see if you're a food master!")

        while (correctAnswers < MAX_ALLOWED_CORRECT_ANSWERS && randomMealsIndex < randomMeals.size) {

            val currentRandomMeal = randomMeals[randomMealsIndex]
            val correctIngredient = currentRandomMeal.ingredients.random()
            val wrongIngredients =
                getMealGameUtilsUseCase.getWrongIngredients(validMeals, currentRandomMeal, correctIngredient)

            if (wrongIngredients.size < MIN_NEEDED_WRONG_INGREDIENTS) {
                randomMealsIndex++
                continue
            }

            val mealIngredientsOptions = MealOptions(correctIngredient, wrongIngredients)
            val options = getMealGameUtilsUseCase.getShuffledOptions(mealIngredientsOptions)

            println("\n🍽️ Meal: ${currentRandomMeal.name}")
            println("🥳 Choose the correct ingredient. Think carefully, or else you'll have to blame your stomach!")

            options.forEachIndexed { index, option ->
                println("${index + 1}. $option")
            }
            print("Your answer (1-3): ")
            val choice = readlnOrNull()?.toIntOrNull()

            if (choice == null || choice !in (1..3)) {
                println("❌ Oops! Invalid input. It’s like adding pineapple to pizza... Just doesn’t work.")
                continue
            }

            val selectedOption = options[choice - 1]

            if (selectedOption == correctIngredient) {
                score += 1000
                correctAnswers++
                println("✅ Ding ding ding! Correct! Your score: $score 🏆 You’re on fire! 🔥\n")
            } else {
                println("❌ Oops, wrong ingredient! The correct answer was: $correctIngredient. It’s okay, everyone makes mistakes.")
                return
            }
            randomMealsIndex++

        }
        if (correctAnswers == MAX_ALLOWED_CORRECT_ANSWERS) {
            println("🎉 WOW! You’ve won the game! Your final score is $score! You’re the food king/queen! 👑")
        } else {
            println("🥲 Game Over! You got $correctAnswers correct answers. Better luck next time! Final score: $score")
        }

    }

}