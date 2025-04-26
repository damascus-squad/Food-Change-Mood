package org.damascus.presentation.suggest

import org.damascus.model.Meal
import org.damascus.presentation.io.ConsoleDisplay
import org.damascus.presentation.io.ConsoleUserInput
import org.damascus.useCase.suggest.GetEggFreeSweetUseCase
import org.damascus.useCase.suggest.GetHighCalorieMealUseCase
import org.damascus.useCase.suggest.GetKetoMealUseCase
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
                println("\nEgg-Free Sweet".withStyle(TerminalColor.Yellow))
                println("Name: ${meal.name}".withStyle(TerminalColor.Blue))
                println("Description: ${meal.description.take(150)}...".withStyle(TerminalColor.Blue))

                if (consoleUserInput.readBoolean()) {
                    consoleDisplay.displayMealsBy(
                        meals = listOf(meal),
                        label = "Egg Free Sweet"
                    )
                    return
                } else {
                    println("Fetching another one...".withStyle(TerminalColor.Cyan))
                    continue
                }

            } catch (e: NoSuchElementException) {
                println("Error: ${e.message}".withStyle(TerminalColor.Red))
                return
            }
        }
    }

    override fun getHighCalorieMeal() {
        while (true) {
            try {
                val meals = highCalorieMealUseCase()

                for (meal in meals) {
                    println("\nHigh Calorie Meal".withStyle(TerminalColor.Yellow))
                    println("Name: ${meal.name}".withStyle(TerminalColor.Blue))
                    println("Calories: ${meal.nutrition.calories}".withStyle(TerminalColor.Blue))
                    println("Description: ${meal.description.take(150)}...".withStyle(TerminalColor.Blue))

                    if (consoleUserInput.readBoolean()) {
                        consoleDisplay.displayMealsBy(
                            meals = listOf(meal),
                            label = "High Calorie Meal"
                        )
                        return
                    } else {
                        println("Fetching another one...".withStyle(TerminalColor.Cyan))
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
                (consoleDisplay.displayMealsBy(
                    meals = listOf(suggestion),
                    label = "Keto Meal"
                )
                        )
                return
            } else {
                println("Fetching another one...".withStyle(TerminalColor.Cyan))
                continue
            }
        }
    }
}