package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class GetEggFreeSweetUseCase(
    private val mealRepo: MealRepository
) {
    private val suggestedEggFreeSweets = mutableSetOf<Int>()

    operator fun invoke(): Meal  {
        val allMeals = mealRepo.getAllMeals()
        val eggFree = allMeals.filter { meal ->
            meal.tags.any { it.contains("sweet", true) } &&
                    !meal.ingredients.any { it.contains("egg", true) } &&
                    !suggestedEggFreeSweets.contains(meal.id)
        }

        if (eggFree.isEmpty()) throw NoSuchElementException("No more egg-free sweets left.")

        val next = eggFree.random()
        suggestedEggFreeSweets.add(next.id)
        return next
    }
}