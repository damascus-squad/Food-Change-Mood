package org.damascus.presentation.game

import org.damascus.logic.GuessIngredientGame
import org.damascus.model.GameResult
import org.damascus.utils.TerminalColor
import org.damascus.utils.withStyle

class IngredientGameUi(
    private val gameLogic: GuessIngredientGame
) : Game {

    override fun start() {
        println("🎉 Welcome to the Ultimate Ingredient Game! 🍔🌮🥗")

        val gameResult = gameLogic.playIngredientGame(
            getUserChoice = ::getUserChoice,
            onFeedback = ::print
        )

        printGameMessages(gameResult.gameMessages)
        printGameResult(gameResult)
    }

    private fun getUserChoice(mealName: String, options: List<String>): Int? {
        println("\n\n🍽️ Meal: $mealName")
        println("🥳 Choose the correct ingredient:")

        options.forEachIndexed { index, option ->
            println("${index + 1}. $option")
        }

        print("Your answer (1-${options.size}): ")
        return readlnOrNull()?.toIntOrNull()
    }

    private fun printGameMessages(messages: List<String>) {
        println("\n📜 Game Progress:")
        messages.forEach { println(it) }
    }

    private fun printGameResult(gameResult: GameResult) {
        if (gameResult.correctAnswers == GuessIngredientGame.MAX_ALLOWED_CORRECT_ANSWERS) {
            println("\n🎉 YOU WON! Score: ${gameResult.score} 👑".withStyle(TerminalColor.Green))
        } else {
            println(
                "\n🥲 Game Over. Correct answers: ${gameResult.correctAnswers}. Score: ${gameResult.score}".withStyle(
                    TerminalColor.Red
                )
            )
        }
    }
}