package org.damascus.presentation


import org.damascus.di.MealUseCases
import org.damascus.logic.GuessIngredientGame
import org.damascus.model.Meal
import org.damascus.utils.TerminalColor
import org.damascus.utils.withStyle
import java.util.*

class FoodChangeMoodUi(
    private val useCases: MealUseCases
) {
    private fun getInput() = readlnOrNull()?.toIntOrNull()

    fun start() {
        showMenu(
            uiActionList = listOf(
                UiAction(
                    name = "Identify iraqi meals".withStyle(TerminalColor.Green),
                    action = { printMealsList(useCases.identifyIraqiMeals()) }
                ),
                UiAction(
                    name = "Search Meals By Date".withStyle(TerminalColor.Green),
                    action = { showMealsForSelectedDate() }
                ),
                UiAction(
                    name = "Play Ingredient Game".withStyle(TerminalColor.Green),
                    action = { playIngredientGame() }
                ),
                UiAction(
                    name = "Play Guess Meal Game".withStyle(TerminalColor.Green),
                    action = { playGuessGame(useCases.getRandomMeal()) }
                ),
                UiAction(
                    name = "Search Meals by Name".withStyle(TerminalColor.Green),
                    action = { printSearchResult() }
                ),
                UiAction(
                    name = "Display a Keto Diet Meal".withStyle(TerminalColor.Green),
                    action = { showKetoMenu(useCases.getKetoMeal()) }
                ),
                UiAction(
                    name = "Show High Calorie Meals".withStyle(TerminalColor.Green),
                    action = { printHighCalorieMeal() }
                ),
                UiAction(
                    name = "Get Egg-Free Sweet".withStyle(TerminalColor.Green),
                    action = { printEggFreeSweet() }
                ),
                UiAction(
                    name = "Easy Food Suggestion".withStyle(TerminalColor.Green),
                    action = { printMealsList(useCases.getEasyFoodSuggestions()) }
                ),
                UiAction(
                    name = "Display first 10 meals".withStyle(TerminalColor.Green),
                    action = { printMealsList(useCases.getFirstNMeals()) }
                ),
                UiAction(
                    name = "Display Random 10 Meals That Contain Potato".withStyle(TerminalColor.Green),
                    action = { getPotatoMeals() }
                ),
                UiAction(
                    name = "Explore Country Meals".withStyle(TerminalColor.Green),
                    action = {
                        val country = promptForCountry()
                        val result = useCases.exploreOtherCountriesFood.getCountryFood(country, limit = 20)
                        displayCountryMeals(result, country)
                    }
                ),
                UiAction(
                    name = "Display Italian Meals".withStyle(TerminalColor.Green),
                    action = { printMealsList(useCases.getItalianLargeGroupMeals()) }
                ),
                UiAction(
                    name = "Display Healthy Meals".withStyle(TerminalColor.Green),
                    action = { printHealthyMeals() }
                ),
                UiAction(
                    name = "Display Seafood Meals".withStyle(TerminalColor.Green),
                    action = { printSeafoodMeals() }
                ),
                UiAction(
                    name = "Find meals by calories and protein".withStyle(TerminalColor.Green),
                    action = { printMealsByCaloriesAndProtein() }
                )
            )
        )
    }

    private fun printMealsByCaloriesAndProtein() {

        val targetCalories: Double = getInputs("Enter Calories amount: ".withStyle(TerminalColor.Blue))
        val targetProtein: Double = getInputs("Enter Protein amount: ".withStyle(TerminalColor.Blue))

        val result: List<Meal> =
            useCases.findMealsByCaloriesAndProtein(targetCalories = targetCalories, targetProtein = targetProtein)

        println("Found (${result.size}) meals")
        result.forEach {
            println(
                "Calories: ${it.nutrition.calories}, Protein: ${it.nutrition.protein} | Meal name: ${it.name}".withStyle(
                    TerminalColor.Green
                )
            )
        }
    }

    private fun getInputs(title: String): Double {
        print(title)
        var userInput: Double? = readlnOrNull()?.toDoubleOrNull()
        while (userInput == null) {
            print("Wrong input: ".withStyle(TerminalColor.Red))
            userInput = readlnOrNull()?.toDoubleOrNull()
        }
        return userInput
    }

    private fun promptForCountry(): String {
        while (true) {
            println("🌍 Enter the country name:".withStyle(TerminalColor.Blue))
            val input = readlnOrNull()?.trim().orEmpty()
            if (input.isNotBlank()) {
                return input
            }
            println("❌ Country name cannot be empty. Please try again.".withStyle(TerminalColor.Red))
        }
    }

    private fun displayCountryMeals(meals: List<Meal>, country: String) {
        if (meals.isEmpty()) {
            println("No Meals Found for this $country".withStyle(TerminalColor.Yellow))
            return
        }
        println("✅ Found ${meals.size} meal(s) for '$country'. Showing ${meals.size}:\n".withStyle(TerminalColor.Green))
        meals.forEachIndexed { index, meal ->
            println("🍽 #${index + 1}: ${meal.name}".withStyle(TerminalColor.Green))
            println("—".repeat(40).withStyle(TerminalColor.Magenta))
        }
    }

    private fun printHealthyMeals() {
        val healthyMeals = useCases.getHealthyFastFoodMeals()
        if (healthyMeals.isEmpty()) {
            println("No healthy meals available.".withStyle(TerminalColor.Cyan))
        } else {
            healthyMeals.forEachIndexed { index, mealName ->
                println("Healthy Meal ${index + 1}: $mealName".withStyle(TerminalColor.Green))
            }
        }
    }

    private fun printSeafoodMeals() {
        val seafoodMeals = useCases.sortSeafoodMeals()
        if (seafoodMeals.isEmpty()) {
            println("No seafood meals available".withStyle(TerminalColor.Cyan))
            return
        }
        seafoodMeals.forEachIndexed { index, meal ->
            println(
                "Meal ${index + 1} : ${meal.name} - Protein Content : ${meal.nutrition.protein} ".withStyle(
                    TerminalColor.Green
                )
            )
        }
    }

    private fun printSearchResult() {
        try {
            printMealsList(useCases.searchMealByName(getSearchPhrase()).take(10))
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun showMenu(
        title: String = "Welcome to our App",
        uiActionList: List<UiAction>
    ) {
        while (true) {
            println("\n=== ${title.withStyle(TerminalColor.Blue)} ===")

            uiActionList.forEachIndexed { index, action ->
                println("${index + 1}- ${action.name}")
            }

            print("Enter your choice: (0 to exit): ".withStyle(TerminalColor.Cyan))
            val input = getInput()

            if (input == 0) {
                return
            } else if (input == null || input !in 1..uiActionList.size) {
                println("Invalid input. Try again.\n".withStyle(TerminalColor.Red))
            } else {
                uiActionList[input - 1].action()
            }
        }
    }

    /**
     * This function displays any list of meals
     * every meal will be displayed as each property in one line
     * between each meal 2 lines
     */
    private fun printMealsList(mealsList: List<Meal>) {
        mealsList.forEachIndexed { index, meal ->
            println(
                "Meal ${index + 1}: " +
                        "Name='${meal.name}'\n ".withStyle(TerminalColor.Green) +
                        "ID=${meal.id}\n ".withStyle(TerminalColor.Green) +
                        "Minutes=${meal.minutes}\n ".withStyle(TerminalColor.Green) +
                        "ContributorID=${meal.contributorId}\n ".withStyle(TerminalColor.Green) +
                        "Submitted='${meal.submitted}\n, ".withStyle(TerminalColor.Green) +
                        "Tags=${meal.tags}\n ".withStyle(TerminalColor.Green) +
                        "Nutrition=${meal.nutrition}\n ".withStyle(TerminalColor.Green) +
                        "StepsCount=${meal.stepsCount}\n ".withStyle(TerminalColor.Green) +
                        "Steps=${meal.steps}\n ".withStyle(TerminalColor.Green) +
                        "Description='${meal.description.take(30)}...'\n ".withStyle(TerminalColor.Green) +
                        "Ingredients=${meal.ingredients}\n ".withStyle(TerminalColor.Green) +
                        "IngredientsCount=${meal.ingredientsCount}\n\n".withStyle(TerminalColor.Green)
            )
        }
    }

    private fun printEggFreeSweet() {
        while (true) {
            try {
                val meal = useCases.getEggFreeSweet()
                println("\nEgg-Free Sweet".withStyle(TerminalColor.Blue))
                println("Name: ${meal.name}".withStyle(TerminalColor.Blue))
                println("Description: ${meal.description.take(150)}...".withStyle(TerminalColor.Blue))

                if (askUserToLike()) {
                    printMealsList(useCases.getFirstNMeals())
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

    private fun printHighCalorieMeal() {
        while (true) {
            try {
                val meals = useCases.getHighCalorieMeal()

                for (meal in meals) {
                    println("\nHigh Calorie Meal".withStyle(TerminalColor.Blue))
                    println("Name: ${meal.name}".withStyle(TerminalColor.Blue))
                    println("Calories: ${meal.nutrition.calories}".withStyle(TerminalColor.Blue))
                    println("Description: ${meal.description.take(150)}...".withStyle(TerminalColor.Blue))

                    if (askUserToLike()) {
                        printMealDetails(meal)
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

    private fun askUserToLike(): Boolean {
        print("Do you like it? (y/n): ".withStyle(TerminalColor.Green))
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
        println("Minutes: ${meal.minutes}".withStyle(TerminalColor.Green))
        println("Submitted: ${meal.submitted}".withStyle(TerminalColor.Green))
        println("Nutrition: ${meal.nutrition}".withStyle(TerminalColor.Green))
        println("StepsCount: ${meal.stepsCount}".withStyle(TerminalColor.Green))
        println("Steps: ${meal.steps}".withStyle(TerminalColor.Green))
        println("Ingredients: ${meal.ingredients}".withStyle(TerminalColor.Green))
        println("IngredientsCount: ${meal.ingredientsCount}".withStyle(TerminalColor.Green))
    }

    private fun playIngredientGame() {
        println("🎮 Starting the Ingredient Game...".withStyle(TerminalColor.Blue))
        val guessIngredientGame = GuessIngredientGame(useCases.getMealGameUtils)
        guessIngredientGame.playIngredientGame()
    }

    private fun showKetoMenu(meals: List<Meal>) {
        val notShownMeals = meals.shuffled().toMutableList()

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

            println(
                """
                    
            === Like or Dislike? ===
            1- Like 👍
            2- Dislike 👎
            3- Exit keto ❌
            """.trimIndent().withStyle(TerminalColor.Yellow)
            )

            print("Enter your choice : ".withStyle(TerminalColor.Blue))
            val input = getInput()

            when (input) {
                1 -> {
                    printMealsList(listOf(suggestion))
                    return
                }

                2 -> continue
                3 -> return
                else -> println("Invalid input. Try again.\n".withStyle(TerminalColor.Red))
            }
        }
    }

    private fun getSearchPhrase(): String {
        while (true) {
            print("Please enter the search phrase: ".withStyle(TerminalColor.Blue))
            readlnOrNull()?.let {
                return it
            }
            println("Invalid input, please try again.".withStyle(TerminalColor.Red))
        }

    }

    private fun playGuessGame(meal: Meal) {
        println("🎮 Welcome to the Guess Game!".withStyle(TerminalColor.Blue))
        println("Meal name: ${meal.name}".withStyle(TerminalColor.Green))
        println("Guess the preparation time in minutes (you have 3 attempts)".withStyle(TerminalColor.Blue))

        val scanner = Scanner(System.`in`)
        var attempts = 3

        while (attempts > 0) {
            print("Enter your guess: ".withStyle(TerminalColor.Blue))
            val guess = scanner.nextLine().toIntOrNull()

            if (guess == null) {
                println("⚠️ Please enter a valid number.".withStyle(TerminalColor.Red))
                continue
            }

            when {
                guess == meal.minutes -> {
                    println("✅ Correct! The preparation time is ${meal.minutes} minutes.".withStyle(TerminalColor.Green))
                    return
                }

                guess < meal.minutes -> println("⬆️ Your guess is too low.".withStyle(TerminalColor.Green))
                else -> println("⬇️ Your guess is too high.".withStyle(TerminalColor.Green))
            }

            attempts--
            if (attempts > 0) println("📌 You have $attempts attempt(s) left.".withStyle(TerminalColor.Red))
        }

        println("❌ No more attempts. The correct time was: ${meal.minutes} minutes.".withStyle(TerminalColor.Red))
    }

    private var mealsByDate: List<Meal> = listOf()

    private fun showMealsForSelectedDate() {
        print("\n📅 Enter date (yyyy-MM-dd): ".withStyle(TerminalColor.Green))

        readlnOrNull()?.let { input ->
            val meals = try {
                useCases.getMealsByDate(input)
            } catch (e: IllegalArgumentException) {
                println("❌ ${e.message}")
                return
            } catch (e: NoSuchElementException) {
                println("⚠️ ${e.message}")
                return
            }

            mealsByDate = meals
            println("\n🍽️ Meals added on $input:".withStyle(TerminalColor.Blue))
            meals.forEach { meal ->
                println("🔹 [${meal.id}] ${meal.name}".withStyle(TerminalColor.Green))
            }

            showDetailedMealView()
        }
    }

    private fun showDetailedMealView() {
        if (mealsByDate.isEmpty()) {
            println("⚠️ No meals found. Please search by date first.".withStyle(TerminalColor.Blue))
            return
        }

        print("\n🔢 Enter meal ID to see details: ".withStyle(TerminalColor.Blue))

        readlnOrNull()?.toIntOrNull()
            ?.let { id -> mealsByDate.find { it.id == id } }
            ?.also { meal ->
                println("\n🍽️ Meal Details: ${meal.name}".withStyle(TerminalColor.Green))
                println("\n🍽️ Meal Id: ${meal.id}".withStyle(TerminalColor.Green))
                println("ℹ️ Description: ${meal.description}".withStyle(TerminalColor.Green))

                if (meal.minutes >= 60) {
                    val hours = meal.minutes / 60
                    val remainingMinutes = meal.minutes % 60
                    val timeText = if (remainingMinutes > 0) "${hours}h ${remainingMinutes}m" else "${hours}h"
                    println("⌚ Preparation Time: $timeText".withStyle(TerminalColor.Cyan))
                } else {
                    println("⌚ Preparation Time: ${meal.minutes}m".withStyle(TerminalColor.Cyan))
                }
                println("📅 Submitted On: ${meal.submitted}".withStyle(TerminalColor.Green))
                println("🍴 Ingredients: ${meal.ingredients.joinToString(", ")}".withStyle(TerminalColor.Green))
                println("🔢 ${meal.stepsCount} Steps to Prepare:".withStyle(TerminalColor.Green))
                meal.steps.forEachIndexed { index, step ->
                    println("  ${index + 1}. $step")
                }

                println("\n🍏 Nutritional Information:")
                with(meal.nutrition) {
                    println("🔸 Calories: $calories kcal".withStyle(TerminalColor.Green))
                    println("🔸 Total Fat: $totalFat g".withStyle(TerminalColor.Green))
                    println("🔸 Saturated Fat: $saturatedFat g".withStyle(TerminalColor.Green))
                    println("🔸 Carbohydrates: $carbohydrates g".withStyle(TerminalColor.Green))
                    println("🔸 Sugar: $sugar g".withStyle(TerminalColor.Green))
                    println("🔸 Sodium: $sodium mg".withStyle(TerminalColor.Green))
                    println("🔸 Protein: $protein g".withStyle(TerminalColor.Green))
                }

                println("\n🏷️ Tags: ${meal.tags.joinToString(" || ")}".withStyle(TerminalColor.Green))
            }
            ?: println("❌ Invalid or non-existing meal ID.".withStyle(TerminalColor.Red))
    }

    private fun getPotatoMeals() {
        useCases.getRandomPotatoMeals().forEachIndexed { index, meal ->

            println("\n Meal: ${index + 1}".withStyle(TerminalColor.Blue))
            println("\n🍽️ Meal Details: ${meal.name}".withStyle(TerminalColor.Blue))
            println("\n🍽️ Meal Id: ${meal.id}".withStyle(TerminalColor.Blue))
            println("ℹ️ Description: ${meal.description}".withStyle(TerminalColor.Blue))

            if (meal.minutes >= 60) {
                val hours = meal.minutes / 60
                val remainingMinutes = meal.minutes % 60
                val timeText = if (remainingMinutes > 0) "${hours}h ${remainingMinutes}m" else "${hours}h"
                println("⌚ Preparation Time: $timeText".withStyle(TerminalColor.Green))
            } else {
                println("⌚ Preparation Time: ${meal.minutes}m".withStyle(TerminalColor.Green))
            }

            println("📅 Submitted On: ${meal.submitted}")
            println("🍴 Ingredients: ${meal.ingredients.joinToString(", ")}".withStyle(TerminalColor.Green))
            println("🔢 ${meal.stepsCount} Steps to Prepare:")
            meal.steps.forEachIndexed { idx, step ->
                println("  ${idx + 1}. $step")
            }

            println("\n🍏 Nutritional Information:")
            with(meal.nutrition) {
                println("🔸 Calories: $calories kcal".withStyle(TerminalColor.Green))
                println("🔸 Total Fat: $totalFat g".withStyle(TerminalColor.Green))
                println("🔸 Saturated Fat: $saturatedFat g".withStyle(TerminalColor.Green))
                println("🔸 Carbohydrates: $carbohydrates g".withStyle(TerminalColor.Green))
                println("🔸 Sugar: $sugar g".withStyle(TerminalColor.Green))
                println("🔸 Sodium: $sodium mg".withStyle(TerminalColor.Green))
                println("🔸 Protein: $protein g".withStyle(TerminalColor.Green))
            }

            println("\n🏷️ Tags: ${meal.tags.joinToString(" || ")}".withStyle(TerminalColor.Green))
            println("-".repeat(50).withStyle(TerminalColor.Cyan))
        }

    }

}