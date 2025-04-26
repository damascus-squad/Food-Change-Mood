package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import model.Nutrition
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.useCase.suggest.GetKetoMealUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GetKetoMealUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var getKetoMealUseCase: GetKetoMealUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk()
        getKetoMealUseCase = GetKetoMealUseCase(mealRepository)
    }

    @Test
    fun `should return empty list when repository returns empty list`() {
        // Given
        every { mealRepository.getAllMeals() } returns emptyList()

        // When
        val result = getKetoMealUseCase()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return only keto friendly meals when repository returns mixed meals`() {
        // Given
        every { mealRepository.getAllMeals() } returns mixedMealsList()

        // When
        val result = getKetoMealUseCase()

        // Then
        assertThat(result).hasSize(1)
    }

    @ParameterizedTest
    @CsvSource(
        "0.0, 10.0, 40.0, 20.0",
        "-1.0, 10.0, 40.0, 20.0",
        "500.0, 10.0, -1.0, 20.0",
        "500.0, 10.0, 40.0, -1.0",
    )
    fun `should filter out meals with invalid nutrition values`(
        calories: Double, totalFat: Double, protein: Double, carbohydrates: Double
    ) {
        // Given
        val meals = listOf(
            defaultMeal(
                name = "Valid Keto Meal", nutrition = defaultNutrition(
                    calories = 500.0, totalFat = 45.0, protein = 20.0, carbohydrates = 10.0
                )
            ), defaultMeal(
                name = "Invalid Meal", nutrition = defaultNutrition(
                    calories = calories, totalFat = totalFat, protein = protein, carbohydrates = carbohydrates
                )
            )
        )

        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = getKetoMealUseCase()

        // Then
        assertThat(result).hasSize(1)
    }

    @Test
    fun `should filter out all meals when none are keto friendly`() {
        // Given
        every { mealRepository.getAllMeals() } returns nonKetoMealsList()

        // When
        val result = getKetoMealUseCase()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should handle meals at the boundary conditions of keto requirements`() {
        // Given
        every { mealRepository.getAllMeals() } returns boundaryConditionMealsList()

        // When
        val result = getKetoMealUseCase()

        // Then
        assertThat(result).doesNotContain(
            defaultMeal(
                name = "Just Over Max Carbs", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 15.1, totalFat = 40.0, protein = 15.0
                )
            )
        )
    }

    @Test
    fun `should handle meals at the boundary of nutrition`() {
        // Given
        every { mealRepository.getAllMeals() } returns boundaryBreakingConditionsMealsList()

        // When
        val result = getKetoMealUseCase()

        // Then
        assertThat(result).hasSize(0)
    }

    private fun defaultMeal(
        name: String = "Default Meal",
        id: Int = 1,
        minutes: Int = 15,
        contributorId: Int = 1,
        submitted: String = "2023-01-01",
        tags: List<String> = listOf("test"),
        nutrition: Nutrition = defaultNutrition(),
        stepsCount: Int = 1,
        steps: List<String> = listOf("cook"),
        description: String = "Test meal description",
        ingredients: List<String> = listOf("ingredient"),
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

    private fun defaultNutrition(
        calories: Double = 500.0,
        totalFat: Double = 40.0,
        sugar: Double = 1.0,
        sodium: Double = 1.0,
        protein: Double = 20.0,
        saturatedFat: Double = 10.0,
        carbohydrates: Double = 10.0
    ): Nutrition {
        return Nutrition(
            calories = calories,
            totalFat = totalFat,
            sugar = sugar,
            sodium = sodium,
            protein = protein,
            saturatedFat = saturatedFat,
            carbohydrates = carbohydrates
        )
    }

    private fun mixedMealsList(): List<Meal> {
        return listOf(
            defaultMeal(
                name = "Keto Meal", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 10.0, totalFat = 45.0, protein = 20.0
                )
            ), defaultMeal(
                name = "Non-Keto Meal 1", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 20.0, totalFat = 35.0, protein = 20.0
                )
            ), defaultMeal(
                name = "Non-Keto Meal 2", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 10.0, totalFat = 25.0, protein = 50.0
                )
            ), defaultMeal(
                name = "Non-Keto Meal 3", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 10.0, totalFat = 40.0, protein = 60.0
                )
            )
        )
    }

    private fun nonKetoMealsList(): List<Meal> {
        return listOf(
            defaultMeal(
                name = "High Carb Meal", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 100.0, totalFat = 20.0, protein = 30.0
                )
            ), defaultMeal(
                name = "Low Fat Meal", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 10.0, totalFat = 15.0, protein = 70.0
                )
            )
        )
    }

    private fun boundaryConditionMealsList(): List<Meal> {
        return listOf(
            defaultMeal(
                name = "Exact Max Carbs", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 15.0, totalFat = 40.0, protein = 15.0
                )
            ), defaultMeal(
                name = "Exact Min Fat Percentage", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 10.0, totalFat = 38.89, protein = 15.0
                )
            ), defaultMeal(
                name = "Exact Min Protein Percentage", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 10.0, totalFat = 40.0, protein = 12.5
                )
            ), defaultMeal(
                name = "Exact Max Protein Percentage", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 10.0, totalFat = 40.0, protein = 25.0
                )
            ), defaultMeal(
                name = "Just Over Max Carbs", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 15.1, totalFat = 40.0, protein = 15.0
                )
            )
        )
    }

    private fun boundaryBreakingConditionsMealsList(): List<Meal> {
        return listOf(
            defaultMeal(
                name = "Less Than Min Total Fat", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 10.0, totalFat = -1.0, protein = 25.0
                )
            ),
            defaultMeal(
                name = "Less Than Min Protein", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 10.0, totalFat = 40.0, protein = -1.0
                )
            ),
            defaultMeal(
                name = "Less Than Min Calories", nutrition = defaultNutrition(
                    calories = -1.0, carbohydrates = 10.0, totalFat = 40.0, protein = 25.0
                )
            ),
            defaultMeal(
                name = "More Than Max Carbs", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 20.0, totalFat = 40.0, protein = 25.0
                )
            ),
            defaultMeal(
                name = "Less Than Min Protein", nutrition = defaultNutrition(
                    calories = 500.0, carbohydrates = 10.0, totalFat = 40.0, protein = 5.0
                )
            ),
        )
    }
}