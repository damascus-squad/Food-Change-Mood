package useCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.damascus.logic.MealRepository
import org.damascus.useCase.GetMealGameUtilsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.damascus.model.Meal
import org.damascus.model.MealOptions

class GetMealGameUtilsUseCaseTest {
    private lateinit var mealRepo: MealRepository
    private lateinit var useCase: GetMealGameUtilsUseCase

    @BeforeEach
    fun setUp() {
        mealRepo = mockk()
        useCase = GetMealGameUtilsUseCase(mealRepo)
    }

    @Test
    fun `getValidMeals with empty meal list returns empty list`() {
        // Given
        every { mealRepo.getAllMeals() } returns emptyList()

        // When
        val result = useCase.getValidMeals { true }

        // Then
        assertThat(result).isEmpty()
        verify { mealRepo.getAllMeals() }
    }

    @Test
    fun `getValidMeals with no matching meals returns empty list`() {
        // Given
        val meals = listOf(
            createTestMeal(name = "Meal1", ingredients = listOf("apple", "sugar")),
            createTestMeal(name = "Meal2", ingredients = listOf("banana", "honey"))
        )
        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = useCase.getValidMeals { it.name.contains("Pizza") }

        // Then
        assertThat(result).isEmpty()
        verify { mealRepo.getAllMeals() }
    }

    @Test
    fun `getValidMeals with some matching meals returns only matching meals`() {
        // Given
        val meal1 = createTestMeal(name = "Apple Pie", ingredients = listOf("apple", "sugar"))
        val meal2 = createTestMeal(name = "Banana Bread", ingredients = listOf("banana", "flour"))
        val meal3 = createTestMeal(name = "Apple Crumble", ingredients = listOf("apple", "oats"))
        val meals = listOf(meal1, meal2, meal3)

        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = useCase.getValidMeals { it.name.contains("Apple") }

        // Then
        assertThat(result).containsExactly(meal1, meal3)
        verify { mealRepo.getAllMeals() }
    }

    @Test
    fun `getValidMeals with all matching meals returns full list`() {
        // Given
        val meal1 = createTestMeal(name = "Meal1")
        val meal2 = createTestMeal(name = "Meal2")
        val meals = listOf(meal1, meal2)

        every { mealRepo.getAllMeals() } returns meals

        // When
        val result = useCase.getValidMeals { true }

        // Then
        assertThat(result).containsExactlyElementsIn(meals)
        verify { mealRepo.getAllMeals() }
    }

    @Test
    fun `getWrongIngredients with empty validMeals returns empty list`() {
        // Given
        val emptyMeals = emptyList<Meal>()
        val currentMeal = createTestMeal(ingredients = listOf("apple"))

        // When
        val result = useCase.getWrongIngredients(
            validMeals = emptyMeals,
            currentRandomMeal = currentMeal,
            correctIngredient = "apple"
        )

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getWrongIngredients with all meals same as currentRandomMeal returns empty list`() {
        // Given
        val currentMeal = createTestMeal(id = 1, ingredients = listOf("apple", "sugar"))
        val validMeals = listOf(
            currentMeal,
            createTestMeal(id = 1, ingredients = listOf("apple", "sugar")) // Same meal, different instance
        )

        // When
        val result = useCase.getWrongIngredients(
            validMeals = validMeals,
            currentRandomMeal = currentMeal,
            correctIngredient = "apple"
        )

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getWrongIngredients with not enough wrong ingredients returns all available`() {
        // Given
        val currentMeal = createTestMeal(id = 1, ingredients = listOf("apple", "sugar"))
        val otherMeal = createTestMeal(id = 2, ingredients = listOf("banana"))
        val validMeals = listOf(currentMeal, otherMeal)

        // When
        val result = useCase.getWrongIngredients(
            validMeals = validMeals,
            currentRandomMeal = currentMeal,
            correctIngredient = "apple",
            minNeededWrongIngredients = 3
        )

        // Then
        assertThat(result).containsExactly("banana")
    }

    @Test
    fun `getWrongIngredients excludes correctIngredient from other meals`() {
        // Given
        val currentMeal = createTestMeal(id = 1, ingredients = listOf("apple", "sugar"))
        val otherMeal = createTestMeal(id = 2, ingredients = listOf("apple", "banana", "flour"))
        val validMeals = listOf(currentMeal, otherMeal)

        // When
        val result = useCase.getWrongIngredients(
            validMeals = validMeals,
            currentRandomMeal = currentMeal,
            correctIngredient = "apple"
        )

        // Then
        assertThat(result).containsExactly("banana", "flour")
        assertThat(result).doesNotContain("apple")
    }

    @Test
    fun `getWrongIngredients returns distinct ingredients when duplicates exist`() {
        // Given
        val currentMeal = createTestMeal(id = 1, ingredients = listOf("apple"))
        val meal2 = createTestMeal(id = 2, ingredients = listOf("banana", "flour"))
        val meal3 = createTestMeal(id = 3, ingredients = listOf("banana", "cinnamon"))
        val validMeals = listOf(currentMeal, meal2, meal3)

        // When
        val result = useCase.getWrongIngredients(
            validMeals = validMeals,
            currentRandomMeal = currentMeal,
            correctIngredient = "apple",
            minNeededWrongIngredients = 3
        )

        // Then
        assertThat(result).containsExactly("banana", "flour", "cinnamon")
    }

    @Test
    fun `getWrongIngredients returns requested number of items when enough available`() {
        // Given
        val currentMeal = createTestMeal(id = 1, ingredients = listOf("apple"))
        val otherMeals = listOf(
            createTestMeal(id = 2, ingredients = listOf("banana", "flour")),
            createTestMeal(id = 3, ingredients = listOf("cinnamon", "sugar")),
            createTestMeal(id = 4, ingredients = listOf("milk", "eggs"))
        )
        val validMeals = listOf(currentMeal) + otherMeals

        // When
        val result = useCase.getWrongIngredients(
            validMeals = validMeals,
            currentRandomMeal = currentMeal,
            correctIngredient = "apple",
            minNeededWrongIngredients = 2
        )

        // Then
        assertThat(result).hasSize(2)
    }

    @Test
    fun `getShuffledOptions with only correctMealIngredient returns single item list`() {
        // Given
        val mealOptions = MealOptions(
            correctMealIngredient = "apple",
            wrongMealIngredients = emptyList()
        )

        // When
        val result = useCase.getShuffledOptions(mealOptions)

        // Then
        assertThat(result).containsExactly("apple")
    }

    @Test
    fun `getShuffledOptions with empty mealOptions returns empty list`() {
        // Given
        val mealOptions = MealOptions(
            correctMealIngredient = "",
            wrongMealIngredients = emptyList()
        )

        // When
        val result = useCase.getShuffledOptions(mealOptions)

        // Then
        assertThat(result).containsExactly("")
    }

    @Test
    fun `getShuffledOptions with correct and multiple wrong ingredients returns all ingredients`() {
        // Given
        val correctIngredient = "apple"
        val wrongIngredients = listOf("banana", "cinnamon", "sugar")
        val mealOptions = MealOptions(
            correctMealIngredient = correctIngredient,
            wrongMealIngredients = wrongIngredients
        )

        // When
        val result = useCase.getShuffledOptions(mealOptions)

        // Then
        assertThat(result).containsExactlyElementsIn(wrongIngredients + correctIngredient)
        assertThat(result).hasSize(wrongIngredients.size + 1)
    }

    @Test
    fun `getShuffledOptions with duplicates in wrongMealIngredients keeps duplicates`() {
        // Given
        val correctIngredient = "apple"
        val wrongIngredients = listOf("banana", "banana", "sugar")
        val mealOptions = MealOptions(
            correctMealIngredient = correctIngredient,
            wrongMealIngredients = wrongIngredients
        )

        // When
        val result = useCase.getShuffledOptions(mealOptions)

        // Then
        assertThat(result).hasSize(wrongIngredients.size + 1)

        val bananaCount = result.count { it == "banana" }
        assertThat(bananaCount).isEqualTo(2)

        assertThat(result).contains("apple")
        assertThat(result).contains("banana")
        assertThat(result).contains("sugar")
    }
}

