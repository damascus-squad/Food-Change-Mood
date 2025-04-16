package org.damascus.useCase

import org.damascus.logic.MealRepository
import java.util.*


class GuessMealPreparationTimeUseCase(private val mealRepository: MealRepository) {

    fun playGuessGame() {
        val scanner = Scanner(System.`in`)
        val meal = mealRepository.getAllMeals().random()
        val actualTime = meal.minutes

        if (actualTime == 0) {
            println("This meal does not have a defined preparation time. Choosing another one.")
            return
        }

        println("🎮 Welcome to the Guess Game!")
        println("Meal name: ${meal.name}")
        println("Guess the preparation time in minutes (you have 3 attempts)")

        var attempts = 3

        while (attempts > 0) {
            print("Enter your guess: ")
            val guess = scanner.nextLine().toIntOrNull()

            if (guess == null) {
                println("⚠️ Please enter a valid number.")
                continue
            }

            if (guess == actualTime) {
                println("✅ Correct! The preparation time is $actualTime minutes.")
                return
            } else if (guess < actualTime) {
                println("⬆️ Your guess is too low.")
            } else {
                println("⬇️ Your guess is too high.")
            }

            attempts--
            if (attempts > 0) println("📌 You have $attempts attempt(s) left.")
        }

        println("❌ No more attempts. The correct time was: $actualTime minutes.")
    }
}
