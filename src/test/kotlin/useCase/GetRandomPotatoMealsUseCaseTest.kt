package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import model.Nutrition
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.useCase.GetRandomPotatoMealsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetRandomPotatoMealsUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var getRandomPotatoMealsUseCase: GetRandomPotatoMealsUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk(relaxed = true)
        getRandomPotatoMealsUseCase = GetRandomPotatoMealsUseCase(mealRepository)
    }

    @Test
    fun `should return exactly 10 meals when more than 10 meals exist`() {
        //given
        every { mealRepository.getAllMeals() } returns generatePotatoMeals(15)
        //when
        val result = getRandomPotatoMealsUseCase()
        //then
        assertThat(result.size).isEqualTo(10)
    }

    @Test
    fun `should return meals when meals contain potato in their ingredients`() {
        //given
        every { mealRepository.getAllMeals() } returns generatePotatoMeals(10)
        //when
        val result = getRandomPotatoMealsUseCase()
        //then
        assertThat(containsPotatoInIngredients(result)).isTrue()
    }

    @Test
    fun `should return all meals if count is 10 or less`() {
        //given
        every { mealRepository.getAllMeals() } returns generatePotatoMeals(7)
        //when
        val result = getRandomPotatoMealsUseCase()
        //then
        assertThat(result.size).isEqualTo(7)
    }

    @Test
    fun `should return only meals that contain potato case-insensitively`() {
        //given
        every { mealRepository.getAllMeals() } returns mealsWithMixedCasingPotatoes()
        //when
        val result = getRandomPotatoMealsUseCase()
        //then
        assertThat(result.map { meal -> meal.name }).containsExactly("Mashed", "Fries", "Hash Browns")
    }

    @Test
    fun `should ignore meals with empty ingredient list`() {
        //give
        every { mealRepository.getAllMeals() } returns getMealsWithValidPotatoIngredient()
        //when
        val result = getRandomPotatoMealsUseCase()
        //then
        assertThat(result.map { meal -> meal.name }).containsExactly("Baked Potato")
    }

    @Test
    fun `should return empty list when no meals contain potato`() {
        // given
        every { mealRepository.getAllMeals() } returns generateNonPotatoMeals()
        // when
        val result = getRandomPotatoMealsUseCase()
        // then
        assertThat(result).isEmpty()
    }

    private fun createMeal(
        name: String,
        ingredients: List<String>
    ): Meal {
        return Meal(
            name = name,
            id = name.hashCode(),
            minutes = 30,
            contributorId = 1,
            submitted = "2025-04-23",
            tags = emptyList(),
            nutrition = Nutrition(
                calories = 0.0,
                protein = 0.0,
                totalFat = 0.0,
                sugar = 0.0,
                sodium = 0.0,
                saturatedFat = 0.0,
                carbohydrates = 0.0
            ),
            stepsCount = 0,
            steps = emptyList(),
            description = "",
            ingredients = ingredients,
            ingredientsCount = ingredients.size
        )
    }

    private fun mealsWithMixedCasingPotatoes(): List<Meal> {
        return listOf(
            createMeal("Mashed", listOf("POTATO")),
            createMeal("Fries", listOf("potato")),
            createMeal("Hash Browns", listOf("PoTaTo")),
            createMeal("Pasta", listOf("wheat"))
        )
    }

    private fun generatePotatoMeals(count: Int): List<Meal> {
        return List(count) { index ->
            createMeal("Meal $index", listOf("potato", "salt"))
        }
    }

    private fun generateNonPotatoMeals(): List<Meal> {
        return listOf(
            createMeal("Salad", listOf("lettuce", "tomato")),
            createMeal("Steak", listOf("beef"))
        )
    }

    private fun getMealsWithValidPotatoIngredient(): List<Meal> {
        return listOf(
            createMeal("Empty Ingredients Meal", emptyList()),
            createMeal("Baked Potato", listOf("potato"))
        )
    }

    private fun containsPotatoInIngredients(meals: List<Meal>): Boolean {
        return meals.all { meal ->
            meal.ingredients.any { ingredient ->
                ingredient.contains(POTATO, ignoreCase = true)
            }
        }
    }

    companion object {
        const val POTATO = "potato"
    }
}
