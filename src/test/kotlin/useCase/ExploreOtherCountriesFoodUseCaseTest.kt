package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import mealHelper.createMeal
import org.damascus.logic.MealRepository
import org.damascus.useCase.search.ExploreOtherCountriesFoodUseCase
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class ExploreOtherCountriesFoodUseCaseTest {

    private lateinit var mealsRepository: MealRepository
    private lateinit var exploreOtherCountriesFoodUseCase: ExploreOtherCountriesFoodUseCase

    @BeforeEach
    fun setup() {
        mealsRepository = mockk(relaxed = true)
        exploreOtherCountriesFoodUseCase = ExploreOtherCountriesFoodUseCase(mealsRepository)
    }

    @Test
    fun `should return meals that contain the specified country in tags regardless of case`() {
        // Given
        val meals = listOf(
            createMeal(name = "Pizza", tags = listOf("italian", "pizza")),
            createMeal(name = "Pasta", tags = listOf("Italian", "pasta")),
            createMeal(name = "Sushi", tags = listOf("japanese", "fish")),
            createMeal(name = "Lasagna", tags = listOf("ITALIAN", "cheese")),
        )
        every { mealsRepository.getAllMeals() } returns meals

        // When
        val result = exploreOtherCountriesFoodUseCase.getCountryFood("Italian", 3)

        // Then
        assertThat(result.map { it.name }).containsExactlyElementsIn(
            listOf("Pizza", "Pasta", "Lasagna")
        )
    }

    @Test
    fun `should return empty list when no meal contains the country in tags`() {
        // Given
        val meals = listOf(
            createMeal(name = "Tacos", tags = listOf("mexican", "spicy")),
            createMeal(name = "Sushi", tags = listOf("japanese")),
        )
        every { mealsRepository.getAllMeals() } returns meals

        // When
        val result = exploreOtherCountriesFoodUseCase.getCountryFood("italian", 3)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return all matching meals when limit is greater than available`() {
        // Given
        val meals = listOf(
            createMeal(name = "Tandoori Chicken", tags = listOf("indian")),
            createMeal(name = "Butter Chicken", tags = listOf("Indian")),
        )
        every { mealsRepository.getAllMeals() } returns meals

        // When
        val result = exploreOtherCountriesFoodUseCase.getCountryFood("indian", 10)

        // Then
        assertThat(result.map { it.name }).containsExactly(
            "Tandoori Chicken",
            "Butter Chicken"
        )
    }

    @Test
    fun `should return different meals on repeated calls due to shuffling`() {
        // Given
        val meals = listOf(
            createMeal(name = "Kebab", tags = listOf("turkish")),
            createMeal(name = "Lamb Kofte", tags = listOf("turkish")),
            createMeal(name = "Patzaria Salata", tags = listOf("turkish"))
        )
        every { mealsRepository.getAllMeals() } returns meals

        // When
        val result = exploreOtherCountriesFoodUseCase.getCountryFood("turkish", 3)

        // Then
        assertThat(result.map { it.name }).containsAtLeastElementsIn(
            listOf("Lamb Kofte", "Kebab", "Patzaria Salata")
        )
    }

    @Test
    fun `should return empty list when limit is zero`() {
        // Given
        val meals = listOf(
            createMeal(name = "Pizza", tags = listOf("italian")),
            createMeal(name = "Pasta", tags = listOf("italian"))
        )
        every { mealsRepository.getAllMeals() } returns meals

        // When
        val result = exploreOtherCountriesFoodUseCase.getCountryFood("italian", 0)

        // Then
        assertThat(result).isEmpty()
    }
}
