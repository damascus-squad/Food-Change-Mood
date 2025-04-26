package org.damascus.presentation.suggest

import org.damascus.model.Meal
import org.damascus.presentation.utils.ConsoleDisplay
import org.damascus.presentation.utils.ConsoleUserInput
import org.damascus.useCase.*
import org.damascus.utils.TerminalColor
import org.damascus.utils.withStyle

class MealSuggestUi(
    private val consoleDisplay: ConsoleDisplay,
    private val eggFreeSweetUseCase: GetEggFreeSweetUseCase,
    private val highCalorieMealUseCase: GetHighCalorieMealUseCase,
    private val ketoMealUseCase: GetKetoMealUseCase,
    private val consoleUserInput: ConsoleUserInput
) : MealSuggest {

    override fun getEggFreeSweet() {
        while (true) {
            try {
                val meal = eggFreeSweetUseCase()
                println("\nEgg-Free Sweet".withStyle(TerminalColor.Blue))
                println("Name: ${meal.name}".withStyle(TerminalColor.Blue))
                println("Description: ${meal.description.take(150)}...".withStyle(TerminalColor.Blue))

                if (consoleUserInput.readBoolean()) {
                    consoleDisplay.specificMeal(meal)
                    return
                } else {
                    println("Fetching another one...".withStyle(TerminalColor.Red))
                }

            } catch (e: NoSuchElementException) {
                println("Error: ${e.message}".withStyle(TerminalColor.Red))
                return
            }
        }
    }

    override fun getHighCalorieMeal(){
        while (true) {
            try {
                val meals = highCalorieMealUseCase()

                for (meal in meals) {
                    println("\nHigh Calorie Meal".withStyle(TerminalColor.Blue))
                    println("Name: ${meal.name}".withStyle(TerminalColor.Blue))
                    println("Calories: ${meal.nutrition.calories}".withStyle(TerminalColor.Blue))
                    println("Description: ${meal.description.take(150)}...".withStyle(TerminalColor.Blue))

                    if (consoleUserInput.readBoolean()) {
                        consoleDisplay.specificMeal(meal)
                        return
                    } else {
                        println("Skipping...".withStyle(TerminalColor.Green))
                        continue
                    }
                }

            } catch (e: NoSuchElementException) {
                println("No more high-calorie meals to show.".withStyle(TerminalColor.Red))
                return
            }
        }
    }


    override fun getKetoMeals() {
        val notShownMeals = ketoMealUseCase().shuffled().toMutableList()

        var suggestion: Meal

        while (true) {
            if (notShownMeals.isEmpty()) {
                println("No more keto meals available to suggest.".withStyle(TerminalColor.Red))
                return
            }

            suggestion = notShownMeals.removeLast()
            println(
                """
                    Here is your keto meal suggestion:
                    name        : ${suggestion.name}
                    description : ${suggestion.description}
            """.trimIndent().withStyle(TerminalColor.Green)
            )

            if (consoleUserInput.readBoolean()) {
                (consoleDisplay.meals(listOf(suggestion)))
                return
            } else {
                println("Skipping...".withStyle(TerminalColor.Green))
                continue
            }
        }
    }

    override fun getEasyFood() {
        TODO("Not yet implemented")
    }
}