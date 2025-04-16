package org.damascus.presentation

import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.useCase.GetMealsByDateUseCase

class FoodChangeMoodUI(
    private val getFirstNMealsUseCase: GetFirstTenMealsUseCase,
    private val getMealsByDateUseCase: GetMealsByDateUseCase,
) {
    private fun getInput() = readLine()?.toIntOrNull()
    private var mealsByDate: List<Meal> = listOf()
    fun start() {
        showMenu(
            title = "Welcome to our App",
            options = listOf(
                "Display first 10 meals",
                "Get food by date",
                "Get ........"
            ),
            actions = listOf(
                { printFirst10Meals() },
                { printMealsByDate() },
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
                        "Description='${meal.description.take(30)}...'\n " +
                        "Ingredients=${meal.ingredients}\n " +
                        "IngredientsCount=${meal.ingredientsCount}\n\n"
            )
        }
    }


    private fun printMealsByDate() {
        print("\n📅 Enter date (yyyy-MM-dd): ")
        val input = readlnOrNull() ?: return

        try {
            mealsByDate = getMealsByDateUseCase(input)
            println("\n🍽️ Meals added on $input:")
            mealsByDate.forEach { meal ->
                println("🔹 [${meal.id}] ${meal.name}")
            }

            printMealDetailsById()

        } catch (e: IllegalArgumentException) {
            println("❌ ${e.message}")
        } catch (e: NoSuchElementException) {
            println("⚠️ ${e.message}")
        }
    }

    private fun printMealDetailsById() {
        if (mealsByDate.isEmpty()) {
            println("⚠️ No meals found. Please search by date first.")
            return
        }

        print("\n🔢 Enter meal ID to see details: ")
        val mealId = readlnOrNull()?.toIntOrNull()

        if (mealId != null) {
            val selectedMeal = mealsByDate.find { it.id == mealId }

            if (selectedMeal != null) {
                println("\n🍽️ Meal Details: ${selectedMeal.name}")
                println("🔸 Description: ${selectedMeal.description}")
                println("🔸 Preparation Time: ${selectedMeal.minutes} minutes")
                println("🔸 Submitted On: ${selectedMeal.submitted}")
                println("🔸 Ingredients: ${selectedMeal.ingredients.joinToString(", ")}")
                println("🔸 Steps to Prepare:")
                selectedMeal.steps.forEachIndexed { index, step ->
                    println("  ${index + 1}. $step")
                }

                println("\n🍏 Nutritional Information:")
                with(selectedMeal.nutrition) {
                    println("🔸 Calories: $calories kcal")
                    println("🔸 Total Fat: $totalFat g")
                    println("🔸 Saturated Fat: $saturatedFat g")
                    println("🔸 Carbohydrates: $carbohydrates g")
                    println("🔸 Sugar: $sugar g")
                    println("🔸 Sodium: $sodium mg")
                    println("🔸 Protein: $protein g")
                }

                println("\n🏷️ Tags: ${selectedMeal.tags.joinToString(" || ")}")


            } else {
                println("❌ Meal with ID $mealId not found in the list.")
            }
        } else {
            println("❌ Invalid meal ID.")
        }
    }
}