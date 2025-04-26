package org.damascus.presentation

import org.damascus.domain.InputException
import org.damascus.presentation.game.GuessGameUI
import org.damascus.presentation.game.IngredientGameUi
import org.damascus.presentation.io.ConsoleUserInput
import org.damascus.presentation.retrieve.MealRetrieveUi
import org.damascus.presentation.search.MealSearchUi
import org.damascus.presentation.suggest.MealSuggestUi
import org.damascus.utils.TerminalColor
import org.damascus.utils.withStyle

class FoodChangeMoodUi(
    private val mealSearchUI: MealSearchUi,
    private val mealSuggestUi: MealSuggestUi,
    private val mealRetrieveUi: MealRetrieveUi,
    private val ingredientGameUi: IngredientGameUi,
    private val guessGameUI: GuessGameUI,
    private val consoleUserInput: ConsoleUserInput
) {

    fun start() {
        showMenu(
            uiActionList = listOf(
                UiAction(
                    name = "🇮🇶 Identify iraqi meals",
                    action = { mealRetrieveUi.displayNationalityMeals() }
                ),
                UiAction(
                    name = "🗓️ Search Meals By Date",
                    action = { mealSearchUI.displayByDate() }
                ),
                UiAction(
                    name = "🎮 Play Ingredient Game",
                    action = { ingredientGameUi.start() }
                ),
                UiAction(
                    name = "🎮 Play Guess Meal Game",
                    action = { guessGameUI.start() }
                ),
                UiAction(
                    name = "🔍Search Meals by Name",
                    action = { mealSearchUI.displayByName() }
                ),
                UiAction(
                    name = "🥩 Display a Keto Diet Meal",
                    action = { mealSuggestUi.displayKetoMeals() }
                ),
                UiAction(
                    name = "🔋 Show High Calorie Meals",
                    action = { mealSuggestUi.displayHighCalorieMeal() }
                ),
                UiAction(
                    name = "🍬 Get Egg-Free Sweet",
                    action = { mealSuggestUi.displayEggFreeSweet() }
                ),
                UiAction(
                    name = "🍳 Easy Food Suggestion",
                    action = { mealRetrieveUi.displayEasyFood() }
                ),
                UiAction(
                    name = "🥔 Display Random 10 Meals That Contain Potato",
                    action = { mealRetrieveUi.displayPotatoMeals() }
                ),
                UiAction(
                    name = "🌍 Explore Country Meals",
                    action = { mealSearchUI.displayByCountry() }
                ),
                UiAction(
                    name = "🍝 Display Italian Meals",
                    action = { mealRetrieveUi.displayItalianMeals() }
                ),
                UiAction(
                    name = "🍔 Display Healthy Meals (Low fat, low carbs, <15 min)",
                    action = { mealRetrieveUi.displayHealthyMeal() }
                ),
                UiAction(
                    name = "🐟 Display Seafood Meals",
                    action = { mealRetrieveUi.displaySeafoodMeal() }
                ),
                UiAction(
                    name = "🏋️ Gym Helper Find meals by calories and protein",
                    action = { mealSearchUI.displayByCaloriesAndProtein() }
                )))
    }

    private fun showMenu(uiActionList: List<UiAction>) {
        while (true) {
            print("=".repeat(40))
            print("\n🍽️ Welcome to Food Change Mood App! 🍽️\n".withStyle(TerminalColor.Green))
            println("=".repeat(40))

            uiActionList.forEachIndexed { index, action ->
                val number = (index + 1).toString().padStart(2, '0')
                println("${number}. ${action.name}".withStyle(TerminalColor.entries.random()))
            }

            try {
                val input = consoleUserInput.readInt(
                    prompt = "\n👉 Enter your choice (0 to Exit): ".withStyle(TerminalColor.Yellow),
                    min = 0,
                    max = uiActionList.size
                )

                if (input == 0) {
                    println("\n👋 Exiting... Stay healthy!".withStyle(TerminalColor.Green))
                    return
                }

                println("\n✨ You selected: ${uiActionList[input - 1].name}".withStyle(TerminalColor.Cyan))
                uiActionList[input - 1].action()

            } catch (e: InputException) {
                println("⚠️ ${e.message}".withStyle(TerminalColor.Red))
            }

            println("\n🔄 Press Enter to return to menu...".withStyle(TerminalColor.Reset))
            readlnOrNull()
        }
    }
}
