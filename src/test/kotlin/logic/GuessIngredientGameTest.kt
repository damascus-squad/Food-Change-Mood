package logic

import io.mockk.every
import io.mockk.mockk
import model.Nutrition
import org.damascus.logic.GuessIngredientGame
import org.damascus.model.Meal
import org.damascus.model.MealOptions
import org.damascus.useCase.suggest.GetMealGameUtilsUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class GuessIngredientGameTest {


    @Test
    fun `playIngredientGame - all correct answers`() {
        val getMealGameUtilsUseCase = mockk<GetMealGameUtilsUseCase>()

        val meal1 = createMeal("Pizza", ingredients = listOf("Cheese", "Flour", "Tomato"))
        val meal2 = createMeal("Burger", ingredients = listOf("Bun", "Lettuce", "Beef"))

        every { getMealGameUtilsUseCase.getValidMeals(any()) } returns listOf(meal1, meal2)

        every {
            getMealGameUtilsUseCase.getWrongIngredients(any(), any(), any())
        } answers {
            val correct = thirdArg<String>()
            listOf("Wrong1", "Wrong2").filter { it != correct }
        }

        every {
            getMealGameUtilsUseCase.getShuffledOptions(any())
        } answers {
            val correct = firstArg<MealOptions>().correctMealIngredient
            val wrongs = firstArg<MealOptions>().wrongMealIngredients
            (wrongs + correct).shuffled()
        }

        val game = GuessIngredientGame(getMealGameUtilsUseCase)

        val result = game.playIngredientGame(
            getUserChoice = { _, options ->
                val correctIngredient = listOf("Cheese", "Flour", "Tomato", "Bun", "Lettuce", "Beef")
                    .firstOrNull { it in options }
                options.indexOf(correctIngredient) + 1
            },
            onFeedback = {}
        )

        // Verifying the score and correct answers
        assertEquals(2000, result.score)
        assertEquals(2, result.correctAnswers)
    }

    @Test
    fun `playIngredientGame - wrong answer ends game`() {
        val getMealGameUtilsUseCase = mockk<GetMealGameUtilsUseCase>()

        val meal = createMeal("Pizza", ingredients = listOf("Cheese", "Flour", "Tomato"))

        every { getMealGameUtilsUseCase.getValidMeals(any()) } returns listOf(meal)

        every {
            getMealGameUtilsUseCase.getWrongIngredients(any(), any(), any())
        } returns listOf("Wrong1", "Wrong2")

        every {
            getMealGameUtilsUseCase.getShuffledOptions(any())
        } returns listOf("Wrong1", "Wrong2", "Cheese")

        val game = GuessIngredientGame(getMealGameUtilsUseCase)

        val result = game.playIngredientGame(
            getUserChoice = { _, _ -> 1 }, // Choosing the wrong answer (Wrong1)
            onFeedback = {}
        )

        assertEquals(0, result.score)
        assertEquals(0, result.correctAnswers)
    }

    private fun createMeal(
        name: String = "Koshry",
        id: Int = 1,
        minutes: Int = 15,
        contributorId: Int = 1,
        submitted: String = "1-1-2023",
        tags: List<String> = listOf("original", "egyptian"),
        nutrition: Nutrition = Nutrition(
            calories = 1.0,
            totalFat = 1.0,
            sugar = 1.0,
            sodium = 1.0,
            protein = 1.0,
            saturatedFat = 1.0,
            carbohydrates = 1.0
        ),
        stepsCount: Int = 1,
        steps: List<String> = listOf("cook"),
        description: String = "Best Meal Ever",
        ingredients: List<String> = listOf("Perfection"),
        ingredientsCount: Int = 1
    ): Meal {
        return Meal(
            name = name,
            id = id,
            minutes = minutes,
            contributorId = contributorId,
            submitted = submitted,
            tags = tags,
            nutrition = nutrition,
            stepsCount = stepsCount,
            steps = steps,
            description = description,
            ingredients = ingredients,
            ingredientsCount = ingredientsCount
        )
    }
}
