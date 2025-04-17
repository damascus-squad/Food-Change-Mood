package org.damascus.presentation


import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.logic.GetKetoMealUseCase
import org.damascus.model.Meal

class FoodChangeMoodUI(
    private val getFirstNMealsUseCase: GetFirstTenMealsUseCase,
    private val getKetoMealUseCase: GetKetoMealUseCase
) {
    private fun getInput() = readLine()?.toIntOrNull()

    fun start() {
        showMenu(
            title = "Welcome to our App",
            options = listOf(
                "Display first 10 meals",
                "Display a Keto Diet Meal",
                "Get ........"
            ),
            actions = listOf(
                { printFirst10Meals() },
                { showKetoMealHelper() },
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




    private fun showKetoMealHelper() {
        val meal = getKetoMealUseCase()

        if (meal == null) {
            println("\nSorry, no more keto-friendly meals available!")
            return
        }

        showMealSummary(meal)

        showMenu(
            title = "Keto Meal Options",
            options = listOf(
                "Like it 👍",
                "Dislike 👎"
            ),
            actions = listOf(
                { likeMeal() },
                { dislikeMeal() }
            )
        )
    }
    private fun showMealSummary(meal: Meal) {
        println("\n=== Keto-Friendly Meal Suggestion ===")
        println("Name: ${meal.name}")
        println("Description: ${meal.description.take(150)}${if (meal.description.length > 150) "..." else ""}")
    }
    private fun likeMeal() {
        val meal = getKetoMealUseCase.likeCurrentMeal()

        if (meal == null) {
            println("\nNo meal information available.")
            return
        }

        println("\n=== Full Meal Details ===")
        println("Name: ${meal.name}")
        println("Description: ${meal.description}")
        println("Ingredients (${meal.ingredientsCount}):")
        meal.ingredients.forEachIndexed { index, ingredient ->
            println("  ${index + 1}. $ingredient")
        }
        println("\nPreparation Steps (${meal.stepsCount}):")
        meal.steps.forEachIndexed { index, step ->
            println("  ${index + 1}. $step")
        }
        println("\nNutritional Information:")
        println("  Calories: ${meal.nutrition.calories}")
        println("  Total Fat: ${meal.nutrition.totalFat}g")
        println("  Carbohydrates: ${meal.nutrition.carbohydrates}g")
        println("  Protein: ${meal.nutrition.protein}g")
        println("  Sugar: ${meal.nutrition.sugar}g")
        println("  Sodium: ${meal.nutrition.sodium}mg")
        println("  Saturated Fat: ${meal.nutrition.saturatedFat}g")


        println("\nPress Enter to return to main menu...")
        readLine()
        return
    }
    private fun dislikeMeal() {
        getKetoMealUseCase.dislikeAndGetNewMeal()
        showKetoMealHelper()
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
}