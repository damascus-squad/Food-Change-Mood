package org.damascus.presentation


import org.damascus.data.FoodHelper
import org.damascus.useCase.GetEggFreeSweetUseCase
import org.damascus.useCase.GetHighCalorieMealUseCase

class FoodChangeMoodUI(
    private val getEggFreeSweetUseCase: GetEggFreeSweetUseCase,
    private val getHighCalorieMealUseCase: GetHighCalorieMealUseCase
) {

    fun start() {
        FoodHelper.getEggFreeSweetUseCase = getEggFreeSweetUseCase
        FoodHelper.getHighCalorieMealUseCase = getHighCalorieMealUseCase

        while (true) {
            println("===== Food & Mood CLI =====".withStyle(TerminalColor.Blue))
            listOf(
                "Get Egg-Free Sweet",
                "Get High Calorie Meal",
                "Exit"
            ).forEachIndexed { index, item ->
                println("${index + 1}. $item".withStyle(TerminalColor.entries.random()))
            }
            println("==================================".withStyle(TerminalColor.Blue))
            print("Choose an option: ".withStyle(TerminalColor.Cyan))

            when (readlnOrNull()?.toIntOrNull()) {
                1 -> FoodHelper.printEggFreeSweet()
                2 -> FoodHelper.printHighCalorieMeal()
                3 -> {
                    println("Exiting... Goodbye!".withStyle(TerminalColor.Blue))
                    return
                }

                else -> println("Invalid input. Please select a valid number.".withStyle(TerminalColor.Red))
            }

            println("Press Enter to continue...".withStyle(TerminalColor.Yellow))
            readlnOrNull()
        }
    }
}
