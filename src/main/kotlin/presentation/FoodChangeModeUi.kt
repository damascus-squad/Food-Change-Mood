package org.damascus.presentation


import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.model.Meal
import org.damascus.useCase.GetEasyFoodSuggestionsUseCase
import org.damascus.useCase.GetEggFreeSweetUseCase
import org.damascus.useCase.GetKetoMealUseCase
import org.damascus.useCase.IdentifyIraqiMealsUseCase
import org.damascus.useCase.SearchMealByNameUseCase

class FoodChangeMoodUi(
    private val getFirstNMealsUseCase: GetFirstTenMealsUseCase,
    private val getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase,
    private val getEggFreeSweetUseCase: GetEggFreeSweetUseCase,
    private val getKetoMealUseCase: GetKetoMealUseCase,
    private val identifyIraqiMealsUseCase: IdentifyIraqiMealsUseCase,
    private val searchMealByNameUseCase: SearchMealByNameUseCase
) {

    fun start() {
        showMenu(
            title = "Welcome to our App",
            options = listOf(
                "Display first 10 meals",
                "Identify iraqi meals",
                "Easy Food Suggestion",
                "Display a Keto Diet Meal",
                "Search Meals",
                "Get Egg-Free Sweet",
            ),
            actions = listOf(
                { printMealsList(getFirstNMealsUseCase()) },
                { printMealsList(identifyIraqiMealsUseCase.invoke()) },
                { printMealsList(getEasyFoodSuggestionsUseCase()) },
                { printEggFreeSweet()},
                { showKetoMenu(getKetoMealUseCase()) },
                { printSearchResult() },
                { }
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
     * This function displays any list of meals
     * every meal will be displayed as each property in one line
     * between each meal 2 lines
     */
    fun printMealsList(mealsList: List<Meal>) {
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

    fun printEggFreeSweet() {
        while (true) {
            try {
                val meal = getEggFreeSweetUseCase()
                println("\nEgg-Free Sweet")
                println("Name: ${meal.name}")
                println("Description: ${meal.description.take(150)}...")

                if (askUserToLike()) {
                    printMealsList(getFirstNMealsUseCase())
                    return
                } else {
                    println("Fetching another one...")
                }

            } catch (e: NoSuchElementException) {
                println("Error: ${e.message}")
                return
            }
        }
    }
    private fun askUserToLike(): Boolean {
        print("Do you like it? (y/n): ")
        return when (readlnOrNull()?.trim()?.lowercase()) {
            "y" -> true
            "n" -> false
            else -> {
                println("Invalid input. Please enter 'y' or 'n'.")
                askUserToLike()
            }
        }
    }


    fun showKetoMenu(meals: List<Meal>) {
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


    private fun getInput() = readLine()?.toIntOrNull()

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