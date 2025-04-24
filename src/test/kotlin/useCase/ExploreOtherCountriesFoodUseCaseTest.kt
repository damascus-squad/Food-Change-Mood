package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import mealHelper.createMeal
import org.damascus.logic.MealRepository
import org.damascus.useCase.ExploreOtherCountriesFoodUseCase
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
    fun `should return random meals that contain the specified country in tags`() {
        val meals = listOf(
            createMeal(name = "Pizza", tags = listOf("italian", "pizza")),
            createMeal(name = "Pasta", tags = listOf("Italian", "pasta")),
            createMeal(name = "Sushi", tags = listOf("japanese", "fish")),
            createMeal(name = "Lasagna", tags = listOf("ITALIAN", "cheese")),
        )
        every { mealsRepository.getAllMeals() } returns meals

        val result = exploreOtherCountriesFoodUseCase.getCountryFood("italian", 2)

        val expected = result.size == 2 && result.all { it.tags.any { tag -> tag.lowercase().contains("italian") } }
        assertThat(expected).isTrue()
    }


    @Test
    fun `should return empty list when no meals contain the country in tags`() {
        val meals = listOf(
            createMeal(name = "Tacos", tags = listOf("mexican", "spicy")),
            createMeal(name = "Sushi", tags = listOf("japanese")),
        )
        every { mealsRepository.getAllMeals() } returns meals

        val result = exploreOtherCountriesFoodUseCase.getCountryFood("italian", 3)

        assertThat(result.isEmpty()).isTrue()
    }


    @Test
    fun `should return all matching meals if limit is greater than available meals`() {
        val meals = listOf(
            createMeal(name = "Tandoori Chicken", tags = listOf("indian")),
            createMeal(name = "Butter Chicken", tags = listOf("Indian")),
        )
        every { mealsRepository.getAllMeals() } returns meals

        val result = exploreOtherCountriesFoodUseCase.getCountryFood("indian", 10)

        val names = result.map { it.name }
        assertThat(names.containsAll(listOf("Tandoori Chicken", "Butter Chicken")) && names.size == 2).isTrue()
    }



    @Test
    fun `should return different results on repeated calls due to shuffling`() {
        val meals = listOf(
            createMeal(name = "Meal1", tags = listOf("turkish")),
            createMeal(name = "Meal2", tags = listOf("turkish")),
            createMeal(name = "Meal3", tags = listOf("turkish")),
        )
        every { mealsRepository.getAllMeals() } returns meals

        val result = exploreOtherCountriesFoodUseCase.getCountryFood("turkish", 2)

        val validResults = result.size == 2 && result.all { it.tags.any { t -> t.lowercase().contains("turkish") } }
        assertThat(validResults).isTrue()
    }

}



