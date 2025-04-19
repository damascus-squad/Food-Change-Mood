package org.damascus.presentation


import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.model.Meal
import org.damascus.useCase.GetEasyFoodSuggestionsUseCase
import org.damascus.useCase.GetHighCalorieMealUseCase
import org.damascus.useCase.GetKetoMealUseCase
import org.damascus.useCase.GetMealsByDateUseCase
import org.damascus.useCase.IdentifyIraqiMealsUseCase
import org.damascus.useCase.SearchMealByNameUseCase

class FoodChangeMoodUi(
    private val getFirstNMealsUseCase: GetFirstTenMealsUseCase,
    private val getEasyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase,
    private val getHighCalorieMealUseCase: GetHighCalorieMealUseCase,
    private val getKetoMealUseCase: GetKetoMealUseCase,
    private val identifyIraqiMealsUseCase: IdentifyIraqiMealsUseCase,
    private val searchMealByNameUseCase: SearchMealByNameUseCase,
    private val getMealsByDateUseCase: GetMealsByDateUseCase
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
                "Get High Calorie Meal",
                "Search Meals By Date"
            ),
            actions = listOf(
                { printMealsList(getFirstNMealsUseCase()) },
                { printMealsList(identifyIraqiMealsUseCase.invoke()) },
                { printMealsList(getEasyFoodSuggestionsUseCase()) },
                { printHighCalorieMeal()},
                { showKetoMenu(getKetoMealUseCase()) },
                { printSearchResult() },
                { showMealsForSelectedDate()}
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

    fun printHighCalorieMeal() {
        while (true) {
            try {
                val meals = getHighCalorieMealUseCase()

                for (meal in meals) {
                    println("\nHigh Calorie Meal")
                    println("Name: ${meal.name}")
                    println("Calories: ${meal.nutrition.calories}")
                    println("Description: ${meal.description.take(150)}...")

                    if (askUserToLike()) {
                        printMealDetails(meal)
                        return
                    } else {
                        println("Skipping...")
                        continue
                    }
                }

            } catch (e: NoSuchElementException) {
                println("No more high-calorie meals to show.")
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

    private fun printMealDetails(meal: Meal) {
        println("Minutes: ${meal.minutes}")
        println("Submitted: ${meal.submitted}")
        println("Nutrition: ${meal.nutrition}")
        println("StepsCount: ${meal.stepsCount}")
        println("Steps: ${meal.steps}")
        println("Ingredients: ${meal.ingredients}")
        println("IngredientsCount: ${meal.ingredientsCount}")
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
    private var mealsByDate: List<Meal> = listOf()
    private fun getSearchPhrase(): String {
        while (true) {
            print("Please enter the search phrase: ")
            readlnOrNull()?.let {
                return it
            }
            println("Invalid input, please try again.")
        }
    }

    private fun showMealsForSelectedDate() {
        print("\n📅 Enter date (yyyy-MM-dd): ")

        readlnOrNull()?.let { input ->
            val meals = try {
                getMealsByDateUseCase(input)
            } catch (e: IllegalArgumentException) {
                println("❌ ${e.message}")
                return
            } catch (e: NoSuchElementException) {
                println("⚠️ ${e.message}")
                return
            }

            mealsByDate = meals
            println("\n🍽️ Meals added on $input:")
            meals.forEach { meal ->
                println("🔹 [${meal.id}] ${meal.name}")
            }

            showDetailedMealView()
        }
    }

    private fun showDetailedMealView() {
        if (mealsByDate.isEmpty()) {
            println("⚠️ No meals found. Please search by date first.")
            return
        }

        print("\n🔢 Enter meal ID to see details: ")

        readlnOrNull()?.toIntOrNull()
            ?.let { id -> mealsByDate.find { it.id == id } }
            ?.also { meal ->
                println("\n🍽️ Meal Details: ${meal.name}")
                println("\n🍽️ Meal Id: ${meal.id}")
                println("ℹ️ Description: ${meal.description}")

                if (meal.minutes >= 60) {
                    val hours = meal.minutes / 60
                    val remainingMinutes = meal.minutes % 60
                    val timeText = if (remainingMinutes > 0) "${hours}h ${remainingMinutes}m" else "${hours}h"
                    println("⌚ Preparation Time: $timeText")
                } else {
                    println("⌚ Preparation Time: ${meal.minutes}m")
                }
                println("📅 Submitted On: ${meal.submitted}")
                println("🍴 Ingredients: ${meal.ingredients.joinToString(", ")}")
                println("🔢 ${meal.stepsCount} Steps to Prepare:")
                meal.steps.forEachIndexed { index, step ->
                    println("  ${index + 1}. $step")
                }

                println("\n🍏 Nutritional Information:")
                with(meal.nutrition) {
                    println("🔸 Calories: $calories kcal")
                    println("🔸 Total Fat: $totalFat g")
                    println("🔸 Saturated Fat: $saturatedFat g")
                    println("🔸 Carbohydrates: $carbohydrates g")
                    println("🔸 Sugar: $sugar g")
                    println("🔸 Sodium: $sodium mg")
                    println("🔸 Protein: $protein g")
                }

                println("\n🏷️ Tags: ${meal.tags.joinToString(" || ")}")
            }
            ?: println("❌ Invalid or non-existing meal ID.")
    }
}
