package org.damascus.presentation

import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.model.Meal
import org.damascus.useCase.*

class FoodChangeMoodUi(
    private val getFirstNMealsUseCase: GetFirstTenMealsUseCase,
    private val foodUseCase: ExploreOtherCountriesFoodUseCase,
    private val getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase,
    private val getKetoMealUseCase: GetKetoMealUseCase,
    private val identifyIraqiMealsUseCase: IdentifyIraqiMealsUseCase,
    private val searchMealByNameUseCase: SearchMealByNameUseCase
) {
    private fun getInput() = readLine()?.toIntOrNull()

    fun start() {
        showMenu(
            title = "Welcome to our App",
            options = listOf(
                "Display first 10 meals",
                "Identify iraqi meals",
                "Easy Food Suggestion",
                "Display a Keto Diet Meal",
                "Search Meals",
                "Explore Other Countries' Food"
            ),
            actions = listOf(
                { printMealsList(getFirstNMealsUseCase()) },
                { printMealsList(identifyIraqiMealsUseCase.invoke()) },
                { printMealsList(getEasyFoodSuggestionsUseCase()) },
                { showKetoMenu(getKetoMealUseCase()) },
                { printSearchResult() },
                {
                    val country = promptForCountry()
                    val result = foodUseCase.getCountryFood(country, limit = 20)
                    displayCountryMeals(result, country)
                }
            )
        )
    }

    private fun printSearchResult() {
        try {
            printMealsList(searchMealByNameUseCase(getSearchPhrase()).take(10))
        } catch (e: Exception) {
            println(e.message)
        }
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
     * This function displays any list of meals
     * every meal will be displayed as each property in one line
     * between each meal 2 lines
     */
    private fun printMealsList(mealsList: List<Meal>) {
        mealsList.forEachIndexed { index, meal ->
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
                        "Description='${meal.description.take(30)}...'\n " +
                        "Ingredients=${meal.ingredients}\n " +
                        "IngredientsCount=${meal.ingredientsCount}\n\n"
            )
        }
    }

    private fun showKetoMenu(meals: List<Meal>) {
        val notShownMeals = meals.shuffled().toMutableList()

        var suggestion: Meal
        while (true) {
            if (notShownMeals.isEmpty()) {
                println("No more keto meals available to suggest.")
                return
            }

            suggestion = notShownMeals.removeLast()
            println(
                """
                    Here is your keto meal suggestion:
                    name        : ${suggestion.name}
                    description : ${suggestion.description}
            """.trimIndent()
            )

            println(
                """
                    
            === Like or Dislike? ===
            1- Like 👍
            2- Dislike 👎
            3- Exit keto ❌
            """.trimIndent()
            )

            print("Enter your choice : ")
            val input = getInput()

            when (input) {
                1 -> {
                    printMealsList(listOf(suggestion))
                    return
                }

                2 -> continue
                3 -> return
                else -> println("Invalid input. Try again.\n")
            }
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

    private fun getSearchPhrase(): String {
        while (true) {
            print("Please enter the search phrase: ")
            readlnOrNull()?.let {
                return it
            }
            println("Invalid input, please try again.")

        }
    }

}