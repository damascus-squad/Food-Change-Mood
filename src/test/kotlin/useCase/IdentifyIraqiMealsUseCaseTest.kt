package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import model.Nutrition
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.useCase.IdentifyIraqiMealsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IdentifyIraqiMealsUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: IdentifyIraqiMealsUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk()
        useCase = IdentifyIraqiMealsUseCase(mealRepository)
    }

    @Test
    fun `should return meals that match nationality in name, description or tags`() {
        //Given
        every { mealRepository.getAllMeals() } returns fakeMealsWithNationalityInVariousFields

        //when
        val result = useCase(nationality = "iraqi")

        //Then
        assertThat(result.map { it.name }).containsExactly(
            "bahraini sweet rice muhammar",
            "dolma stuffed grape leaves",
            "iraqi summag salad",
            "barmia"
        )
    }

    @Test
    fun `should match nationality case-insensitively`() {
        //Given
        every { mealRepository.getAllMeals() } returns fakeCaseInsensitiveNationalityMeals

        //when
        val result = useCase(nationality = "iraqi")

        //Then
        assertThat(result.map { it.name }).containsExactly(
            "IRAQI KOFTA",
            "Grilled Meat",
            "Tagine"
        )
    }

    @Test
    fun `should use default nationality 'iraqi' when no parameter is provided`() {
        //Given
        every { mealRepository.getAllMeals() } returns fakeMealsWithDefaultNationality

        //When
        val result = useCase()

        //Then
        assertThat(result.map { it.name }).containsExactly("Iraqi Stew")
    }

    @Test
    fun `should return empty list if no meals match nationality`() {
        //Given
        every { mealRepository.getAllMeals() } returns fakeMealsWithNoMatchingNationality

        //when
        val result = useCase(nationality = "iraqi")

        //Then
        assertThat(result).isEmpty()
    }

    private val fakeMealsWithNationalityInVariousFields = listOf(
        createMeal("bahraini sweet rice muhammar", "delicious", listOf("iraqi")),
        createMeal("dolma stuffed grape leaves", "authentic iraqi dish", listOf("gluten-free", "iraqi")),
        createMeal("iraqi summag salad", "classic", listOf("cuisine", "iraqi")),
        createMeal("barmia", "tasty iraqi okra stew", listOf("iraqi")),
        createMeal("mexican squash", "spicy and sweet", listOf("mexican"))
    )

    private val fakeCaseInsensitiveNationalityMeals = listOf(
        createMeal("IRAQI KOFTA", "", emptyList()),
        createMeal("Grilled Meat", "iraqi style", emptyList()),
        createMeal("arriba baked", "delicious", listOf("mexican")),
        createMeal("Tagine", "", listOf("IRaQi"))
    )

    private val fakeMealsWithDefaultNationality = listOf(
        createMeal("Iraqi Stew", "", emptyList())
    )

    private val fakeMealsWithNoMatchingNationality = listOf(
        createMeal("Burger", "american style", listOf("fast-food")),
        createMeal("Sushi", "fresh", listOf("japanese"))
    )

    private fun fakeNutrition() = Nutrition(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    private fun createMeal(
        name: String,
        description: String,
        tags: List<String>,
    ): Meal {
        return Meal(
            name = name,
            id = 1,
            minutes = 10,
            contributorId = 101,
            submitted = "2023-01-01",
            tags = tags,
            nutrition = fakeNutrition(),
            stepsCount = 3,
            steps = listOf("Step 1", "Step 2", "Step 3"),
            description = description,
            ingredients = listOf("Rice", "Meat", "Spices"),
            ingredientsCount = 3
        )
    }
}