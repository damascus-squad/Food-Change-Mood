package org.damascus.presentation.game

import org.damascus.useCase.game.GetRandomMealUseCase
import org.damascus.utils.TerminalColor
import org.damascus.utils.withStyle
import java.util.*

class GuessGameUI(
    private val getRandomMealUseCase: GetRandomMealUseCase
) : Game {

    override fun start() {
        val meal = getRandomMealUseCase.getRandomMeal()
        println("🎮 Welcome to the Guess Game!".withStyle(TerminalColor.Blue))
        println("Meal name: ${meal.name}".withStyle(TerminalColor.Green))
        println("Guess the preparation time in minutes (you have 3 attempts)".withStyle(TerminalColor.Blue))

        val scanner = Scanner(System.`in`)
        var attempts = 3

        while (attempts > 0) {
            print("Enter your guess: ".withStyle(TerminalColor.Blue))
            val guess = scanner.nextLine().toIntOrNull()

            if (guess == null) {
                println("⚠️ Please enter a valid number.".withStyle(TerminalColor.Red))
                continue
            }

            when {
                guess == meal.minutes -> {
                    println("✅ Correct! The preparation time is ${meal.minutes} minutes.".withStyle(TerminalColor.Green))
                    return
                }

                guess < meal.minutes -> println("⬆️ Your guess is too low.".withStyle(TerminalColor.Green))
                else -> println("⬇️ Your guess is too high.".withStyle(TerminalColor.Green))
            }

            attempts--
            if (attempts > 0) println("📌 You have $attempts attempt(s) left.".withStyle(TerminalColor.Red))
        }

        println("❌ No more attempts. The correct time was: ${meal.minutes} minutes.".withStyle(TerminalColor.Red))
    }
}