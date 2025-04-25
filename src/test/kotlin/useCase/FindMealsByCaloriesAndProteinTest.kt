package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import model.Nutrition
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.useCase.FindMealsByCaloriesAndProtein
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FindMealsByCaloriesAndProteinTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var findMealsByCaloriesAndProtein: FindMealsByCaloriesAndProtein

    @BeforeEach
    fun setup() {
        mealRepository = mockk(relaxed = true)
        findMealsByCaloriesAndProtein = FindMealsByCaloriesAndProtein(mealRepository)
    }

    @Test
    fun `should return meals within calorie and protein tolerance when valid meals exist`() {
        //given
        every { mealRepository.getAllMeals() } returns getMealsWithinTolerance()
        //when
        val result = findMealsByCaloriesAndProtein(500.0, 30.0)
        //then
        assertThat(result.map { it.name }).isEqualTo(listOf("Grilled Chicken Salad", "Beef Stir-fry with Vegetables"))
    }

    @Test
    fun `should return valid meals when meals are on the boundary of tolerance`() {
        //given
        every { mealRepository.getAllMeals() } returns getMealsAtToleranceBoundaries()
        //when
        val result = findMealsByCaloriesAndProtein(500.0, 30.0)
        //then
        assertThat(result.map { it.name }).isEqualTo(listOf("Tuna Salad", "Grilled Salmon"))
    }

    @Test
    fun `should  return empty list when meals just outside the tolerance range`() {
        //given
        every { mealRepository.getAllMeals() } returns getMealsOutsideTolerance()
        //when
        val result = findMealsByCaloriesAndProtein(500.0, 30.0)
        //then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when repository returns no meals`() {
        //given
        every { mealRepository.getAllMeals() } returns emptyList()
        //when
        val result = findMealsByCaloriesAndProtein(500.0, 30.0)
        //then
        assertThat(result).isEmpty()
    }

    private fun createMeal(
        name: String,
        calories: Double,
        protein: Double
    ): Meal {
        return Meal(
            name = name,
            id = name.hashCode(),
            minutes = 30,
            contributorId = 1,
            submitted = "2025-04-23",
            tags = emptyList(),
            nutrition = Nutrition(
                calories = calories,
                protein = protein,
                totalFat = 0.0,
                sugar = 0.0,
                sodium = 0.0,
                saturatedFat = 0.0,
                carbohydrates = 0.0
            ),
            stepsCount = 0,
            steps = emptyList(),
            description = "",
            ingredients = emptyList(),
            ingredientsCount = 0
        )
    }

    private fun getMealsWithinTolerance(): List<Meal> {
        return listOf(
            createMeal("Grilled Chicken Salad", 490.0, 28.0),
            createMeal("Beef Stir-fry with Vegetables", 510.0, 32.0)
        )
    }

    private fun getMealsOutsideTolerance(): List<Meal> {
        return listOf(
            createMeal("Fruit Smoothie", 484.9, 30.0),
            createMeal("Cheese Burger", 515.1, 30.0),
            createMeal("Chicken Breast", 500.0, 26.9),
            createMeal("Salmon Fillet", 500.0, 33.1)
        )
    }

    private fun getMealsAtToleranceBoundaries(): List<Meal> {
        return listOf(
            createMeal("Tuna Salad", 485.0, 27.0),
            createMeal("Grilled Salmon", 515.0, 33.0)
        )
    }

}
