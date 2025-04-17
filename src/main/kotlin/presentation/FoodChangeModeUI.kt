package org.damascus.presentation


import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.useCase.GetRandomPotatoMealsUseCase

class FoodChangeMoodUI(
    private val getFirstNMealsUseCase: GetFirstTenMealsUseCase,
    private val getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase,
) {

    fun start() {
        showMenu(
            title = "Welcome to our App",
            options = listOf(
                "Display first 10 meals",
                "Display Random 10 Meals That Contain Potato",
                "Get ........"
            ),
            actions = listOf(
                { printFirst10Meals() },
                { getPotatoMeals() },
                { }
            )
        )
    }

    private fun showMenu(
        title: String,
        options: List<String>,
        actions: List<() -> Unit>,
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

    private fun getPotatoMeals() {
        getRandomPotatoMealsUseCase().forEachIndexed { index, meal ->

            println("\n Meal: ${index + 1}")
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
            println("-".repeat(50))
        }

    }


    private fun getInput() = readLine()?.toIntOrNull()


}
