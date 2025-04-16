package org.damascus.data

import org.damascus.model.Meal
import org.damascus.presentation.TerminalColor
import org.damascus.presentation.withStyle
import org.damascus.useCase.GetEggFreeSweetUseCase
import org.damascus.useCase.GetHighCalorieMealUseCase

object FoodHelper {
    lateinit var getEggFreeSweetUseCase: GetEggFreeSweetUseCase
    lateinit var getHighCalorieMealUseCase: GetHighCalorieMealUseCase

    fun printEggFreeSweet() {
        while (true) {
            try {
                val meal = getEggFreeSweetUseCase()
                println("\nEgg-Free Sweet".withStyle(TerminalColor.Green))
                println("Name: ${meal.name}")
                println("Description: ${meal.description.take(150)}...")

                if (askUserToLike()) {
                    printMealDetails(meal)
                    return
                } else {
                    println("Fetching another one...".withStyle(TerminalColor.Yellow))
                }

            } catch (e: NoSuchElementException) {
                println("Error: ${e.message}".withStyle(TerminalColor.Red))
                return
            }
        }
    }

    fun printHighCalorieMeal() {
        while (true) {
            try {
                val meal = getHighCalorieMealUseCase()
                println("\nHigh Calorie Meal".withStyle(TerminalColor.Magenta))
                println("Name: ${meal.name}")
                println("Calories: ${meal.nutrition.calories}")
                println("Description: ${meal.description.take(150)}...")

                if (askUserToLike()) {
                    printMealDetails(meal)
                    return
                } else {
                    println("Skipping...".withStyle(TerminalColor.Yellow))
                    continue
                }

            } catch (e: NoSuchElementException) {
                println("No more high-calorie meals to show.".withStyle(TerminalColor.Red))
                return
            }
        }
    }

    private fun askUserToLike(): Boolean {
        print("Do you like it? (y/n): ".withStyle(TerminalColor.Cyan))
        return when (readlnOrNull()?.trim()?.lowercase()) {
            "y" -> true
            "n" -> false
            else -> {
                println("Invalid input. Please enter 'y' or 'n'.".withStyle(TerminalColor.Red))
                askUserToLike()
            }
        }
    }

    private fun printMealDetails(meal: Meal) {
        println("Minutes: ${meal.minutes}")
        println("Submitted: ${meal.submitted}")
        println("Nutrition: ${meal.nutrition}")
        println("StepsCount: ${meal.stepsCount}")
        println("Steps: ${meal.steps}")
        println("Ingredients: ${meal.ingredients}")
        println("IngredientsCount: ${meal.ingredientsCount}")
    }
}
