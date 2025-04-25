package useCase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import model.Nutrition
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.model.MealOptions
import org.damascus.useCase.GetMealGameUtilsUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test


class GetMealGameUtilsUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: GetMealGameUtilsUseCase

    @BeforeEach
    fun setUp() {
        mealRepository = mockk()
        useCase = GetMealGameUtilsUseCase(mealRepository)
    }

    @Test
    fun `getValidMeals returns meals matching predicate`() {
        // Given
        val meal1 = createMeal(name = "Salad", ingredients = listOf("Tomato", "Lettuce"))
        val meal2 = createMeal(name = "Pizza", ingredients = listOf("Cheese", "Tomato"))
        every { mealRepository.getAllMeals() } returns listOf(meal1, meal2)

        // When
        val result = useCase.getValidMeals { it.ingredients.contains("Tomato") }
        verify { mealRepository.getAllMeals() }

        // Then
        assertEquals(2, result.size)
    }

    @Test
    fun `getWrongIngredients returns distinct wrong ingredients`() {
        // Given
        val currentMeal = createMeal(name = "Burger", ingredients = listOf("Beef", "Lettuce"))
        val meal1 = createMeal(name = "Salad", ingredients = listOf("Tomato", "Lettuce"))
        val meal2 = createMeal(name = "Pizza", ingredients = listOf("Cheese", "Tomato", "Basil"))
        val validMeals = listOf(meal1, meal2)

        // When
        val result = useCase.getWrongIngredients(
            validMeals = validMeals,
            currentRandomMeal = currentMeal,
            correctIngredient = "Tomato",
            minNeededWrongIngredients = 2
        )

        // Then
        assertEquals(2, result.size)
        assertFalse(result.contains("Tomato"))
    }

    @Test
    fun `getShuffledOptions returns mixed options`() {
        // Given
        val options = MealOptions(
            correctMealIngredient = "Cheese",
            wrongMealIngredients = listOf("Lettuce", "Beef")
        )

        // When
        val result = useCase.getShuffledOptions(options)

        // Then
        assertEquals(3, result.size)
        assertTrue(result.contains("Cheese"))
        assertTrue(result.contains("Lettuce"))
        assertTrue(result.contains("Beef"))
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
