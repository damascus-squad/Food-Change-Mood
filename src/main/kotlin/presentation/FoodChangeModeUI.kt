package org.damascus.presentation


import org.damascus.useCase.GetEggFreeSweetUseCase

class FoodChangeMoodUI(
    private val getEggFreeSweetUseCase: GetEggFreeSweetUseCase
) {
    private fun getInput() = readlnOrNull()?.toIntOrNull()

    fun start() {
        showMenu(
            title = "Welcome to our App",
            options = listOf(
                "Get Egg-Free Sweet",
            ),
            actions = listOf(
                { printEggFreeSweet() },
                { },
                { }
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


    private fun printEggFreeSweet() {
        try {
            val meal = getEggFreeSweetUseCase()
            println("\nEgg-Free Sweet:")
            println("Name: ${meal.name}")
            println("Minutes: ${meal.minutes}")
            println("Submitted: ${meal.submitted}")
            println("Nutrition: ${meal.nutrition}")
            println("StepsCount: ${meal.stepsCount}")
            println("Steps: ${meal.steps}")
            println("Description: ${meal.description.take(150)}...")
            println("Ingredients: ${meal.ingredients}")
            println("IngredientsCount: ${meal.ingredientsCount}")
        } catch (e: NoSuchElementException) {
            println("error: ${e.message}\n")
        }
    }
}