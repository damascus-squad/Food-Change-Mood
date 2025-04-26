package org.damascus.presentation.search

import org.damascus.presentation.utils.ConsoleDisplay
import org.damascus.presentation.utils.ConsoleUserInput
import org.damascus.useCase.ExploreOtherCountriesFoodUseCase
import org.damascus.useCase.FindMealsByCaloriesAndProtein
import org.damascus.useCase.GetMealsByDateUseCase
import org.damascus.useCase.SearchMealByNameUseCase
import org.damascus.utils.TerminalColor
import org.damascus.utils.withStyle
import java.util.NoSuchElementException

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
        consoleDisplay.meals(searchMealByNameUseCase(mealName))
    }

    override fun getByCountry() {
        val country = consoleUserInput.readString("🌍 Enter the country name:")
        val meals = exploreOtherCountriesFoodUseCase.getCountryFood(country, limit = 20)
        println("✅ Found ${meals.size} meal(s) for '${country}".withStyle(TerminalColor.Green))
        consoleDisplay.meals(meals)
    }

    override fun getByDate() {
        while (true) {
            val date = consoleUserInput.readString("📅 Enter date (yyyy-MM-dd):")
            try {
                val meals = getMealsByDateUseCase(date)
                consoleDisplay.meals(meals)
            } catch (e: IllegalArgumentException) {
                println("❌ Invalid date format: ${e.message}".withStyle(TerminalColor.Red))
            } catch (e: NoSuchElementException) {
                println("⚠️ No meals found: ${e.message}".withStyle(TerminalColor.Red))
            }
        }
    }

    override fun getByCaloriesAndProtein() {
        val targetCalories: Double = consoleUserInput.readDouble("Enter Calories amount: ")
        val targetProtein: Double = consoleUserInput.readDouble("Enter Protein amount: ")
        val result = findMealsByCaloriesAndProtein(targetCalories = targetCalories, targetProtein = targetProtein)
        consoleDisplay.meals(result)
    }

}