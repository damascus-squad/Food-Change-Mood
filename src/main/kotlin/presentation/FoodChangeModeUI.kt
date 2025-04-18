package org.damascus.presentation

import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.model.Meal
import org.damascus.useCase.ExploreOtherCountriesFoodUseCase

class FoodChangeMoodUI(
    private val getFirstNMealsUseCase: GetFirstTenMealsUseCase,
    private val foodUseCase: ExploreOtherCountriesFoodUseCase
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
                "Get ........",
                "Get ........",
                "Get ........",
                "Get ........",
                "Get ........",
                "Explore Other Countries' Food"
            ),
            actions = listOf(
                { printFirst10Meals() },
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                { },
                {
                    val country = promptForCountry()
                    val result = foodUseCase.getCountryFood(country, limit = 20)
                    displayCountryMeals(result,country)
                }
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

    private fun promptForCountry(): String {
        while (true) {
            println("🌍 Enter the country name:")
            val input = readlnOrNull()?.trim().orEmpty()
            if (input.isNotBlank()) {
                return input
            }
            println("❌ Country name cannot be empty. Please try again.")
        }
    }

    private fun displayCountryMeals(meals: List<Meal>, country: String) {
        if (meals.isEmpty()){
            println("No Meals Found for this $country")
            return
        }
        println("✅ Found ${meals.size} meal(s) for '$country'. Showing ${meals.size}:\n")
        meals.forEachIndexed { index, meal ->
            println("🍽 #${index + 1}: ${meal.name}")
            println("—".repeat(40))
        }
    }
}