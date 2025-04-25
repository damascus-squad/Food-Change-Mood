import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import model.Nutrition
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.useCase.SearchMealByNameUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

class SearchMealByNameUseCaseTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var searchMealByNameUseCase: SearchMealByNameUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk()
        searchMealByNameUseCase = SearchMealByNameUseCase(mealRepository)
    }

    @Test
    fun `should return exact match`(){
        //Given
        every { mealRepository.getAllMeals() } returns fakeMeals

        //When
        val result = searchMealByNameUseCase("Chicken Burger")

        //then
        assertThat(result.first().name).isEqualTo("Chicken Burger")
    }

    @Test
    fun `should return fuzzy match with small typo`(){
        //Given
        every { mealRepository.getAllMeals() } returns fakeMeals

        //When
        val result = searchMealByNameUseCase("Burgr")

        //then
        assertThat(result.map { it.name }).containsExactly(
            "Chicken Burger",
            "Beef Burger"
        )
    }

    @Test
    fun `should return results are sorted by fewest errors then earliest index`(){
        //Given
        every { mealRepository.getAllMeals() } returns fakeMeals

        //When
        val result = searchMealByNameUseCase("Chicken")

        //then
        assertThat(result.map { it.name }).containsExactly(
            "Chicken Burger",
            "Chicken",
            "Grilled Chicken",
            "Chickan Tika",
            "Chikcen Curry"
        )
    }

    @Test
    fun `returns empty list when no match found`(){
        //Given
        every { mealRepository.getAllMeals() } returns fakeMeals

        //When
        val result = searchMealByNameUseCase("pasta")

        //then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when the pattern empty`(){
        //Given
        every { mealRepository.getAllMeals() } returns fakeMeals

        //When
        val result = searchMealByNameUseCase("")

        //then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throws exception for long search phrase`(){
        //Given
        every { mealRepository.getAllMeals() } returns fakeMeals

        //When
        val longPhrase = "x".repeat(32)
        val exception = assertThrows<IllegalArgumentException> {
            searchMealByNameUseCase(longPhrase)
        }

        //then
        assertThat(exception).hasMessageThat().isEqualTo("Search phrase too long: maximum length is 31 characters")
    }

    @ParameterizedTest
    @CsvSource(
        "Chicken Burger, 0, 1",
        "Chikcen Burger, 1, 0",
        "Chikcen Burger, 2, 1 ",
        "Chocken Borgar, 3, 1 ",
    )

    fun `should match meals based on varying maxErrors`(
        input: String,
        maxErrors: Int,
        expectedMatches: Int
    ) {
        //Given
        every { mealRepository.getAllMeals() } returns fakeMeals

        //When
        val result = searchMealByNameUseCase(input, maxErrors)

        //That
        assertThat(result.size).isEqualTo(expectedMatches)
    }

    private val fakeMeals = listOf(
        createMeal(name = "Chicken"),
        createMeal(name = "Chicken Burger"),
        createMeal(name = "Grilled Chicken"),
        createMeal(name = "Beef Burger"),
        createMeal(name = "Fish Fillet"),
        createMeal(name = "Chikcen Curry"),
        createMeal(name = "Chickan Tika"),
    )

    private fun fakeNutrition() = Nutrition(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    private fun createMeal( name: String ): Meal {
        return Meal(
            name = name,
            id = 1,
            minutes = 10,
            contributorId = 101,
            submitted = "2023-01-01",
            tags = listOf("tag1", "tag2"),
            nutrition = fakeNutrition(),
            stepsCount = 3,
            steps = List(3) { "Step ${it + 1}" },
            description = "$name description",
            ingredients = List(3) { "Ingredient ${it + 1}" },
            ingredientsCount = 3
        )
    }
}