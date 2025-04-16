package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GetMealsByDateUseCase(private val repository: MealRepository) {

    operator fun invoke(inputDate: String): List<Meal> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val targetDate = try {
            LocalDate.parse(inputDate, formatter)
        } catch (e: Exception) {
            throw IllegalArgumentException("⚠️ Incorrect date format. Use yyyy-MM-dd.")
        }

        val allMeals = repository.getAllMeals()

        val matchedMeals = allMeals.filter {
            try {
                LocalDate.parse(it.submitted, formatter) == targetDate
            } catch (_: Exception) {
                false
            }
        }

        if (matchedMeals.isEmpty()) {
            throw NoSuchElementException("😢 No meals found on $inputDate.")
        }

        return matchedMeals
    }
}
