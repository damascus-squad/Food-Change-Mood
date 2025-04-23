package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import model.Nutrition
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.useCase.GetMealsByDateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMealsByDateUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var getMealsByDateUseCase: GetMealsByDateUseCase

    private fun defaultMeal(
        name: String = "Koshry",
        id: Int = 1,
        minutes: Int = 15,
        contributorId: Int = 1,
        submitted: String,
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

    private fun listOfMealsWithRecentDates(): List<Meal> {
        return listOf(
            defaultMeal(submitted = "2022-11-12"),
            defaultMeal(submitted = "2023-12-11"),
            defaultMeal(submitted = "2024-12-31")
        )
    }

    private fun listOfMealsWithIncorrectlyFormattedDates(): List<Meal> {
        return listOf(
            defaultMeal(submitted = "2024-12-31"),
            defaultMeal(submitted = "12-11-2023"),
            defaultMeal(submitted = "2024-12-31")
        )
    }

    @BeforeEach
    fun setup() {
        mealRepository = mockk(relaxed = true)
        getMealsByDateUseCase = GetMealsByDateUseCase(mealRepository)
    }

    @Test
    fun `invoke should throw an IllegalArgumentException exception when date format is incorrect`() {
        // Given
        val inputDate = "11-11-2011"

        // When && Then
        assertThrows<IllegalArgumentException> {
            getMealsByDateUseCase(inputDate)
        }
    }

    @Test
    fun `invoke should throw a NoSuchElementException when no matches are found`() {
        // Given
        val inputDate = "1011-11-11"
        every { mealRepository.getAllMeals() } returns listOfMealsWithRecentDates()

        // When && Then
        assertThrows<NoSuchElementException> {
            getMealsByDateUseCase(inputDate)
        }
    }

    @Test
    fun `invoke should ignore existing meals with incorrect Format`() {
        // Given
        val inputDate = "2024-12-31"
        every { mealRepository.getAllMeals() } returns listOfMealsWithIncorrectlyFormattedDates()

        // When
        val matches = getMealsByDateUseCase(inputDate)

        // Then
        assertThat(matches).isEqualTo(
            listOf(
                defaultMeal(submitted = "2024-12-31"),
                defaultMeal(submitted = "2024-12-31")
            )
        )
    }
    
    @Test
    fun `invoke should return a list of matches when they exist` () {
        // Given
        val inputDate = "2024-12-31"
        every { mealRepository.getAllMeals() } returns listOfMealsWithRecentDates()

        // When
        val matches = getMealsByDateUseCase(inputDate)

        // Then
        assertThat(matches).isEqualTo(
            listOf(
                defaultMeal(submitted = "2024-12-31")
            )
        )
    }

}