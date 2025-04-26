package org.damascus.presentation.search

import org.damascus.presentation.io.ConsoleDisplay
import org.damascus.presentation.io.ConsoleUserInput
import org.damascus.useCase.search.ExploreOtherCountriesFoodUseCase
import org.damascus.useCase.search.FindMealsByCaloriesAndProtein
import org.damascus.useCase.search.GetMealsByDateUseCase
import org.damascus.useCase.search.SearchMealByNameUseCase
import org.damascus.utils.TerminalColor
import org.damascus.utils.withStyle

class MealSearchUi(
    private val consoleUserInput: ConsoleUserInput,
    private val exploreOtherCountriesFoodUseCase: ExploreOtherCountriesFoodUseCase,
    private val searchMealByNameUseCase: SearchMealByNameUseCase,
    private val getMealsByDateUseCase: GetMealsByDateUseCase,
    private val findMealsByCaloriesAndProtein: FindMealsByCaloriesAndProtein,
    private val consoleDisplay: ConsoleDisplay
) : MealSearch {

    override fun getByName() {
        val mealName = consoleUserInput.readString("Please enter the search phrase: ")
        consoleDisplay.displayMealsBy(
            meals = searchMealByNameUseCase(mealName),
            label = "Search Result"
        ) { meal -> mapOf("Name" to meal.name) }
    }

    override fun getByCountry() {
        val country = consoleUserInput.readString("🌍 Enter the country name:")
        val meals = exploreOtherCountriesFoodUseCase.getCountryFood(country, limit = 20)
        println("✅ Found ${meals.size} meal(s) for '${country}".withStyle(TerminalColor.Green))
        consoleDisplay.displayMealsBy(
            meals = meals,
            label = country,
        ) { meal -> mapOf("Name" to meal.name) }
    }

    override fun getByDate() {
        val date = consoleUserInput.readString("📅 Enter date (yyyy-MM-dd):")
        try {
            consoleDisplay.displayMealsBy(
                meals = getMealsByDateUseCase(date),
                label = date
            ) { meal ->
                mapOf(
                    "Name" to meal.name,
                    "Data" to meal.submitted
                )
            }

        } catch (e: IllegalArgumentException) {
            println("❌ Invalid date format: ${e.message}".withStyle(TerminalColor.Red))
        }
    }

    override fun getByCaloriesAndProtein() {
        val targetCalories: Double = consoleUserInput.readDouble("Enter Calories amount: ")
        val targetProtein: Double = consoleUserInput.readDouble("Enter Protein amount: ")
        val result = findMealsByCaloriesAndProtein(targetCalories = targetCalories, targetProtein = targetProtein)
        consoleDisplay.displayMealsBy(
            meals = result,
            label = "Calories and Protein"
        ) { meal ->
            mapOf(
                "Name" to meal.name,
                "Calories" to meal.nutrition.calories,
                "Protein" to meal.nutrition.protein
            )
        }
    }

}