package useCase


import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import model.Nutrition
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.useCase.suggest.GetEggFreeSweetUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetEggFreeSweetUseCaseTest {
    private lateinit var mealRepo: MealRepository
    private lateinit var getEggFreeSweet: GetEggFreeSweetUseCase // Use Case under test

    @BeforeEach
    fun setUp() {
        mealRepo = mockk(relaxed = true)
        getEggFreeSweet = GetEggFreeSweetUseCase(mealRepo)
    }

    @Test
    fun `should return egg-free sweet when available`() {
        // Given: One eligible meal (sorbet), one ineligible (custard), one irrelevant (steak)
        val meals = listOf(eggFreeSweet1, eggSweet, notSweet)
        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = getEggFreeSweet()

        // Then
        assertThat(result).isEqualTo(eggFreeSweet1)
        verify(exactly = 1) { mealRepo.getAllMeals() }
    }

    @Test
    fun `should return one of the egg-free sweets when multiple are available`() {
        // Given: Two eligible meals (sorbet, fruitSalad), one ineligible (custard)
        val meals = listOf(eggFreeSweet1, eggFreeSweet2, eggSweet)
        every { mealRepo.getAllMeals() } returns meals
        val expectedEligible = setOf(eggFreeSweet1, eggFreeSweet2)

        // When
        val result = getEggFreeSweet()

        // Then: Result must be one of the eligible ones
        assertThat(result).isIn(expectedEligible)
        verify(exactly = 1) { mealRepo.getAllMeals() }
    }

    @Test
    fun `should throw NoSuchElementException when repository is empty`() {
        // Given
        every { mealRepo.getAllMeals() } returns emptyList()

        // When && Then
        assertThrows<NoSuchElementException> {
            getEggFreeSweet()
        }

        verify(exactly = 1) { mealRepo.getAllMeals() }
    }

    @Test
    fun `should throw NoSuchElementException when no meals are sweet`() {
        // Given
        val meals = listOf(notSweet) // Only savory meal
        every { mealRepo.getAllMeals() } returns meals

        // When && Then
        assertThrows<NoSuchElementException> {
            getEggFreeSweet()
        }

        verify(exactly = 1) { mealRepo.getAllMeals() }
    }

    @Test
    fun `should throw NoSuchElementException when all sweet meals contain eggs`() {
        // Given
        val meals = listOf(eggSweet) // Sweet but has egg
        every { mealRepo.getAllMeals() } returns meals

        // When && Then
        assertThrows<NoSuchElementException> {
            getEggFreeSweet()
        }

        verify(exactly = 1) { mealRepo.getAllMeals() }
    }

    @Test
    fun `should throw NoSuchElementException when all eligible meals have been suggested`() {
        // Given: Two eligible meals
        val meals = listOf(eggFreeSweet1, eggFreeSweet2, eggSweet)
        every { mealRepo.getAllMeals() } returns meals

        // When: Call twice to suggest both eligible meals
        val result1 = getEggFreeSweet()
        val result2 = getEggFreeSweet()

        // Then: Ensure the first two calls returned the two distinct eligible meals
        val results = setOf(result1, result2)
        assertThat(results).containsExactly(eggFreeSweet1, eggFreeSweet2)

        // When: Call a third time
        // Then: Should throw because both eligible meals were already suggested
        assertThrows<NoSuchElementException> {
            getEggFreeSweet()
        }

        verify(exactly = 3) { mealRepo.getAllMeals() }
    }

    private val eggFreeSweet1 = createTestMeal(
        id = 1,
        name = "Sorbet",
        tags = listOf("sweet", "fruity"),
        ingredients = listOf("sugar", "fruit")
    )

    private val eggFreeSweet2 = createTestMeal(
        id = 2,
        name = "Fruit Salad",
        tags = listOf("SWEET"),
        ingredients = listOf("apple", "banana")
    )

    private val eggSweet = createTestMeal(
        id = 3,
        name = "Custard",
        tags = listOf("sweet"),
        ingredients = listOf("milk", "EgG")
    )

    private val notSweet = createTestMeal(
        id = 5,
        name = "Steak",
        tags = listOf("savory"),
        ingredients = listOf("beef")
    )
}

fun createTestMeal(
    name: String = "Test Meal",
    id: Int = 1234,
    minutes: Int = 30,
    contributorId: Int = 999,
    submitted: String = "2024-04-23",
    tags: List<String> = listOf("test", "default"),
    nutrition: Nutrition = createDefaultNutrition(),
    stepsCount: Int = 3,
    steps: List<String> = listOf("Step 1", "Step 2", "Step 3"),
    description: String = "A test meal for unit testing",
    ingredients: List<String> = listOf("ingredient1", "ingredient2", "salt"),
    ingredientsCount: Int = ingredients.size
) = Meal(
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

fun createDefaultNutrition(
    calories: Double = 300.0,
    totalFat: Double = 10.0,
    sugar: Double = 5.0,
    sodium: Double = 200.0,
    protein: Double = 15.0,
    saturatedFat: Double = 2.0,
    carbohydrates: Double = 40.0
) = Nutrition(
    calories = calories,
    totalFat = totalFat,
    sugar = sugar,
    sodium = sodium,
    protein = protein,
    saturatedFat = saturatedFat,
    carbohydrates = carbohydrates
)

