package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GetMealsByDateUseCase(private val repository: MealRepository) {

    operator fun invoke(inputDate: String): List<Meal> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val targetDate: LocalDate = try {
            LocalDate.parse(inputDate, formatter)
        } catch (e: Exception) {
            throw IllegalArgumentException("⚠️ Incorrect date format. Use yyyy-MM-dd.")
        }

        val matchedMeals = repository.getAllMeals().filter { meal ->
            try {
                LocalDate.parse(meal.submitted, formatter) == targetDate
            } catch (e: Exception) {
                false
            }
        }

        if (matchedMeals.isEmpty()) {
            throw NoSuchElementException("❌ No meals found on $inputDate.")
        }

        return matchedMeals
    }
}
