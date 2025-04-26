package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.damascus.logic.MealRepository
import org.damascus.useCase.GetHighCalorieMealUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetHighCalorieMealUseCaseTest {
    private lateinit var repository: MealRepository
    private lateinit var getHighCalorieMealUseCase: GetHighCalorieMealUseCase

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = true)
        getHighCalorieMealUseCase = GetHighCalorieMealUseCase(repository)
    }

    @Test
    fun `should return only high calorie meals`() {
        // Given
        val highMeal1 = createTestMeal(id = 1, nutrition = createDefaultNutrition(calories = 900.0))
        val highMeal2 = createTestMeal(id = 2, nutrition = createDefaultNutrition(calories = 800.0))
        val lowMeal = createTestMeal(id = 3, nutrition = createDefaultNutrition(calories = 500.0))
        val meals = listOf(highMeal1, highMeal2, lowMeal)

        every { repository.getHighCalorieMeal() } returns meals

        // When
        val result = getHighCalorieMealUseCase()

        // Then
        assertThat(result).containsExactly(highMeal1, highMeal2)
    }

    @Test
    fun `should return empty list when no high calorie meals exist`() {
        // Given
        val lowMeal1 = createTestMeal(id = 1, nutrition = createDefaultNutrition(calories = 900.0))
        val lowMeal2 = createTestMeal(id = 2, nutrition = createDefaultNutrition(calories = 800.0))
        val lowMeal3 = createTestMeal(id = 3, nutrition = createDefaultNutrition(calories = 500.0))
        val meals = listOf(lowMeal1, lowMeal2, lowMeal3)

        every { repository.getHighCalorieMeal() } returns meals

        // When
        val result = getHighCalorieMealUseCase()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when no meals exist`() {
        // Given
        every { repository.getHighCalorieMeal() } returns emptyList()

        // When
        val result = getHighCalorieMealUseCase()

        // Then
        assertThat(result).isEmpty()
    }


    @Test
    fun `should throw NoSuchElementException when no high calorie meals are available`() {
        // Given
        every { repository.getHighCalorieMeal() } throws NoSuchElementException("No more high-calorie meals.")

        // When/Then
        val exception = assertThrows<NoSuchElementException> {
            getHighCalorieMealUseCase()
        }

        assertThat(exception).hasMessageThat().contains("No more high-calorie meals")
    }
}
