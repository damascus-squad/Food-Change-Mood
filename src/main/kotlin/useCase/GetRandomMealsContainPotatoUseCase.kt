package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import kotlin.random.Random

class GetRandomPotatoMealsUseCase(private val repository: MealRepository) {

    operator fun invoke(): List<Meal> =
        repository.getAllMeals()
            .filter { meal ->
                meal.ingredients.any { it.contains("potato", ignoreCase = true) }
            }
            .takeIf { it.isNotEmpty() }
            ?.let { meals ->
                if (meals.size <= 10) meals else meals.shuffled(Random).take(10)
            }
            ?: throw NoSuchElementException("🥔 No meals with potatoes found.")
}
