package org.damascus.presentation


import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.useCase.GuessMealPreparationTimeUseCase

class FoodChangeMoodUI(
    private val getFirstNMealsUseCase: GetFirstTenMealsUseCase,
    private val guessMealPreparationTimeUseCase: GuessMealPreparationTimeUseCase
) {
    private fun getInput() = readLine()?.toIntOrNull()

    fun start() {
        showMenu(
            title = "Welcome to our App",
            options = listOf(
                "Display first 10 meals",
                "Get .....",
                "Get ........",
                "Get ........",
                "Guess Preparation Time of Meal",
            ),
            actions = listOf(
                { printFirst10Meals() },
                { },
                { },
                { },
                { guessMealPreparationTimeUseCase.playGuessGame()},
            )
        )
    }

    private fun showMenu(
        title: String,
        options: List<String>,
        actions: List<() -> Unit>
    ) {
        println("\n=== $title ===")

        options.forEachIndexed { index, option ->
            println("${index + 1}- $option")
        }

        print("Enter your choice: (0 to exit): ")
        val input = getInput()

        if (input == 0) {
            return
        } else if (input == null || input !in 1..options.size) {
            println("Invalid input. Try again.\n")
        } else {
            actions[input - 1]()
        }
        showMenu(title, options, actions)
    }


    /**
     * for test first run
     */
    fun printFirst10Meals() {
        getFirstNMealsUseCase().forEachIndexed { index, meal ->
            println(
                "Meal ${index + 1}: " +
                        "Name='${meal.name}'\n " +
                        "ID=${meal.id}\n " +
                        "Minutes=${meal.minutes}\n " +
                        "ContributorID=${meal.contributorId}\n " +
                        "Submitted='${meal.submitted}\n, " +
                        "Tags=${meal.tags}\n " +
                        "Nutrition=${meal.nutrition}\n " +
                        "StepsCount=${meal.stepsCount}\n " +
                        "Steps=${meal.steps}\n " +
                        "Description='${meal.description.take(30)}...'\n " + // to avoid long prints
                        "Ingredients=${meal.ingredients}\n " +
                        "IngredientsCount=${meal.ingredientsCount}\n\n"
            )
        }
    }
}