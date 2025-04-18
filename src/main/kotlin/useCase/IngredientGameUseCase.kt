package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.model.MealOptions

class IngredientGameUseCase(private val mealRepository: MealRepository) {

    fun playIngredientGame() {
        val validMeal = mealRepository.getAllMeals().filter { meal -> meal.ingredients.size >= 3 }
        val randomMeals = validMeal.shuffled()

        var score = 0
        var correctAnswers = 0
        var randomMealsIndex = 0

        println("🎉 Welcome to the Ultimate Ingredient Game! 🍔🌮🥗 Let's see if you're a food master!")

        while (correctAnswers < 15 && randomMealsIndex < randomMeals.size) {

            val currentRandomMeal = randomMeals[randomMealsIndex]
            val correctIngredient = currentRandomMeal.ingredients.random()
            val wrongIngredients = getWrongIngredients(validMeal, currentRandomMeal, correctIngredient)

            if (wrongIngredients.size < 2) {
                randomMealsIndex++
                continue
            }

            val mealIngredientsOptions = MealOptions(validMeal, correctIngredient, wrongIngredients)
            val options = getShuffledOptions(mealIngredientsOptions)

            println("\n🍽️ Meal: ${currentRandomMeal.name}")
            println("🥳 Choose the correct ingredient. Think carefully, or else you'll have to blame your stomach!")

            options.forEachIndexed { index, option ->
                println("${index+1}. $option")
            }
            println("Your answer (1-3)")
            val choice = readLine()?.toIntOrNull()

            if(choice==null || choice !in(1..3)){
                println("❌ Oops! Invalid input. It’s like adding pineapple to pizza... Just doesn’t work.")
                continue
            }

            val selectedOption = options[choice-1]

            if(selectedOption == correctIngredient){
                score+=1000
                correctAnswers++
                println("✅ Ding ding ding! Correct! Your score: $score 🏆 You’re on fire! 🔥\n")
            }else{
                println("❌ Oops, wrong ingredient! The correct answer was: $correctIngredient. It’s okay, everyone makes mistakes.")
                return
            }
            randomMealsIndex++

        }
        if (correctAnswers == 15) {
            println("🎉 WOW! You’ve won the game! Your final score is $score! You’re the food king/queen! 👑")
        } else {
            println("🥲 Game Over! You got $correctAnswers correct answers. Better luck next time! Final score: $score")
        }

    }

    private fun getWrongIngredients(
        validMeal: List<Meal>,
        currentRandomMeal: Meal,
        correctIngredient: String
    ): List<String> {
        val wrongIngredients = validMeal
            .filter { validMeal -> validMeal == currentRandomMeal }
            .flatMap { validMeal -> validMeal.ingredients }
            .filter { ingredient -> ingredient != correctIngredient }
            .distinct()
            .take(2)
        return wrongIngredients
    }

    private fun getShuffledOptions(mealOptions: MealOptions): List<String> {
        return (mealOptions.wrongMealIngredients + mealOptions.correctMealIngredient).shuffled()
    }


}