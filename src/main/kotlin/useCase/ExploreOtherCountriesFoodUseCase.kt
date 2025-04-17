package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class ExploreOtherCountriesFoodUseCase(private val mealRepo: MealRepository) {
    fun getCountryFood(limit:Int){
        val country = promptForCountry() ?: return
        val matchingMeals = searchMealsByCountry(country)

        if (matchingMeals.isEmpty()) {
            println("❌ No meals found for country: $country")
            return
        }

        val selectedMeals = pickRandomMeals(matchingMeals, limit)
        displayMeals(selectedMeals, matchingMeals.size, country)
    }

    private fun promptForCountry(): String? {
        println("🌍 Enter the country name:")
        val input = readlnOrNull()?.trim().orEmpty()
        if (input.isBlank()) {
            println("❌ Country name cannot be empty.")
            return null
        }
        return input
    }

    private fun searchMealsByCountry(country: String): List<Meal> {
        return mealRepo.getAllMeals().filter { meal ->
            meal.tags.any { tag -> tag.lowercase().contains(country.lowercase()) }
        }
    }

    private fun pickRandomMeals(meals: List<Meal>, count: Int): List<Meal> {
        return meals.shuffled().take(count)
    }

    private fun displayMeals(meals: List<Meal>, total: Int, country: String) {
        println("✅ Found $total meal(s) for '$country'. Showing ${meals.size}:\n")
        meals.forEachIndexed { index, meal ->
            println("🍽 #${index + 1}: ${meal.name}")
            println("—".repeat(40))
        }
    }
}