package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.damascus.logic.MealRepository
import org.damascus.useCase.GetEasyFoodSuggestionsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetEasyFoodSuggestionsUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: GetEasyFoodSuggestionsUseCase

    @BeforeEach
    fun setUp() {
        mealRepository = mockk()
        useCase = GetEasyFoodSuggestionsUseCase(mealRepository)
    }

    @Test
    fun `should return only meals with time less than or equal 30 minutes, ingredients less than or equal 5 and steps less than or equal 6 when filtering meals`() {
        // Given
        val validMeal = createEasyMeal(
            name = "Valid Meal",
            minutes = 30,
            ingredientsCount = 5,
            stepsCount = 6
        )
        val invalidTimeMeal = createEasyMeal(
            name = "Too Long",
            minutes = 31,
            ingredientsCount = 5,
            stepsCount = 6
        )
        val invalidIngredientsMeal = createEasyMeal(
            name = "Too Many Ingredients",
            minutes = 30,
            ingredientsCount = 6,
            stepsCount = 6
        )
        val invalidStepsMeal = createEasyMeal(
            name = "Too Many Steps",
            minutes = 30,
            ingredientsCount = 5,
            stepsCount = 7
        )

        every { mealRepository.getAllMeals() } returns listOf(
            validMeal,
            invalidTimeMeal,
            invalidIngredientsMeal,
            invalidStepsMeal
        )

        // When
        val result = useCase()

        // Then
        assertThat(result).containsExactly(validMeal)
    }

    @Test
    fun `should return empty list when no meals meet the criteria`() {
        // Given
        val invalidMeal = createEasyMeal(
            minutes = 31,
            ingredientsCount = 6,
            stepsCount = 7
        )
        every { mealRepository.getAllMeals() } returns listOf(invalidMeal)

        // When
        val result = useCase()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return maximum 10 meals when there are more than 10 matching meals`() {
        // Given
        val validMeals = (1..15).map {
            createEasyMeal(
                name = "Meal $it",
                minutes = 30,
                ingredientsCount = 5,
                stepsCount = 6
            )
        }
        every { mealRepository.getAllMeals() } returns validMeals

        // When
        val result = useCase()

        // Then
        assertThat(result).hasSize(10)
    }

    @Test
    fun `should return meals in random order when multiple meals exist`() {
        // Given
        val validMeals = (1..15).map {
            createEasyMeal(
                name = "Meal $it",
                minutes = 30,
                ingredientsCount = 5,
                stepsCount = 6
            )
        }
        every { mealRepository.getAllMeals() } returns validMeals

        // When
        val firstCall = useCase().map { it.name }
        val secondCall = useCase().map { it.name }

        // Then
        assertThat(firstCall).isNotEqualTo(secondCall)
    }

    @Test
    fun `should include meals when they have exactly 30 minutes, 5 ingredients and 6 steps`() {
        // Given
        val edgeCaseMeal = createEasyMeal(
            minutes = 30,
            ingredientsCount = 5,
            stepsCount = 6
        )
        every { mealRepository.getAllMeals() } returns listOf(edgeCaseMeal)

        // When
        val result = useCase()

        // Then
        assertThat(result).containsExactly(edgeCaseMeal)
    }
}