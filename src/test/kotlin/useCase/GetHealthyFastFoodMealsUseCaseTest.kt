package useCase


import io.mockk.every
import io.mockk.mockk
import model.Nutrition
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.useCase.GetHealthyFastFoodMealsUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GetHealthyFastFoodMealsUseCaseTest {
    private lateinit var repository: MealRepository
    private lateinit var getHealthyFastFoodMealsUseCase: GetHealthyFastFoodMealsUseCase

    @BeforeEach
    fun setup() {
        repository = mockk()
        getHealthyFastFoodMealsUseCase = GetHealthyFastFoodMealsUseCase(repository)
    }

    @Test
    fun `should return healthy fast food meals under 15 minutes and better nutrition`() {
        // Given
        val meals = listOfHealthyAndNonHealthyMeals()
        every { repository.getAllMeals() } returns meals

        // When
        val result = getHealthyFastFoodMealsUseCase()

        // Then
        val expected = meals
            .filter { it.minutes <= 15 }
            .let { filtered ->
                val avgFat = meals.map { it.nutrition.totalFat }.average()
                val avgSaturated = meals.map { it.nutrition.saturatedFat }.average()
                val avgCarbs = meals.map { it.nutrition.carbohydrates }.average()
                filtered.filter {
                    it.nutrition.totalFat < avgFat &&
                            it.nutrition.saturatedFat < avgSaturated &&
                            it.nutrition.carbohydrates < avgCarbs
                }
            }

        assertEquals(expected, result)
    }

    @Test
    fun `should return empty list if no meal meets healthy fast food criteria`() {
        // Given
        val unhealthyMeals = listOfHealthyAndNonHealthyMeals().map { it.copy(minutes = 100) }
        every { repository.getAllMeals() } returns unhealthyMeals

        // When
        val result = getHealthyFastFoodMealsUseCase()

        // Then
        assertEquals(emptyList<Meal>(), result)
    }


    private fun createMeal(
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

    private fun listOfHealthyAndNonHealthyMeals(): List<Meal> {
        return listOf(
            createMeal(
                name = "Grilled Chicken Wrap",
                minutes = 10,
                nutrition = healthyNutrition(),
                submitted = "2025-01-01"
            ),

            // healthy
            createMeal(
                name = "Vegan Salad Bowl",
                minutes = 12,
                nutrition = healthyNutrition(),
                submitted = "2025-01-02"
            ),

            // healthy
            createMeal(
                name = "Lean Turkey Sandwich",
                minutes = 15,
                nutrition = healthyNutrition(),
                submitted = "2025-01-03"
            ),

            //non healthy
            createMeal(
                name = "Cheeseburger",
                minutes = 10,
                nutrition = Nutrition(
                    calories = 600.0,
                    totalFat = 20.0,// high
                    sugar = 5.0,
                    sodium = 900.0,
                    protein = 30.0,
                    saturatedFat = 10.0,
                    carbohydrates = 60.0
                ),
                submitted = "2025-01-04"
            ),

            // non healthy
            createMeal(
                name = "Loaded Nachos",
                minutes = 20, // > 15
                nutrition = Nutrition(
                    calories = 700.0,
                    totalFat = 15.0,
                    sugar = 6.0,
                    sodium = 1000.0,
                    protein = 15.0,
                    saturatedFat = 7.0,
                    carbohydrates = 70.0
                ),
                submitted = "2025-01-05"
            )
        )
    }

    private fun healthyNutrition() = Nutrition(
        calories = 250.0,
        totalFat = 5.0,
        sugar = 3.0,
        sodium = 200.0,
        protein = 15.0,
        saturatedFat = 2.0,
        carbohydrates = 30.0
    )

}