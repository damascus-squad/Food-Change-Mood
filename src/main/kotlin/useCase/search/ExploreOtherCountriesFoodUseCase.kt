package org.damascus.useCase.search

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class ExploreOtherCountriesFoodUseCase(private val mealRepo: MealRepository) {

    fun getCountryFood(country: String, limit: Int): List<Meal> {
        val matchingMeals = searchMealsByCountry(country)
        return pickRandomMeals(matchingMeals, limit)
    }

    private fun searchMealsByCountry(country: String): List<Meal> {
        return mealRepo.getAllMeals().filter { meal ->
            meal.tags.any { tag -> tag.lowercase().contains(country.lowercase()) }
        }
    }

    private fun pickRandomMeals(meals: List<Meal>, count: Int): List<Meal> {
        return meals.shuffled().take(count)
    }
}