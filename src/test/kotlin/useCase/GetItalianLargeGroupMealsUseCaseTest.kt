package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import model.Nutrition
import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.useCase.GetItalianLargeGroupMealsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GetItalianLargeGroupMealsUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var getItalianMealsForLargeGroups: GetItalianLargeGroupMealsUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk(relaxed = true)
        getItalianMealsForLargeGroups = GetItalianLargeGroupMealsUseCase(mealRepository)
    }

    @Test
    fun `should return empty list when repository returns empty list`() {
        // Given
        every { mealRepository.getAllMeals() } returns emptyList()

        // When
        val matches = getItalianMealsForLargeGroups()

        // Then
        assertThat(matches).isEmpty()
    }

    @Test
    fun `should return a list of meals only when matching name, tags & description are found`() {
        // Given
        every { mealRepository.getAllMeals() } returns getListOfMealsWithTags()

        // When
        val matches = getItalianMealsForLargeGroups()

        // Then
        assertThat(matches).isEqualTo(
            listOf(
                defaultMeal(name = "italian pasta", tags = listOf("for-large-groups", "old"), description = "Original"),
                defaultMeal(tags = listOf("for-large-groups", "trendy"), description = "Original ITALIAN meal")
            )
        )
    }

    @ParameterizedTest
    @CsvSource("10", "50", "100", "150")
    fun `should return all possible meals when they are matched`(numberOfMeals: Int) {
        // Given
        every { mealRepository.getAllMeals() } returns getListOfMatchingMeals(numberOfMeals)

        // When
        val matches = getItalianMealsForLargeGroups()

        // Then
        assertThat(matches.size).isEqualTo(mealRepository.getAllMeals().size)
    }

    @Test
    fun `should ignore meals when the meal is not italian`() {
        // Given
        every { mealRepository.getAllMeals() } returns getListOfMealsWithoutItalianTag()

        // When
        val matches = getItalianMealsForLargeGroups()

        // Then
        assertThat(matches).hasSize(0)
    }

    @Test
    fun `should ignore meals when the meal is not for large groups`() {
        // Given
        every { mealRepository.getAllMeals() } returns getListOfMealsWithoutLargeGroupsTag()

        // When
        val matches = getItalianMealsForLargeGroups()

        // Then
        assertThat(matches).hasSize(0)
    }

    @Test
    fun `should ignore meals when the meal is not original`() {
        // Given
        every { mealRepository.getAllMeals() } returns getListOfMealsWithoutOriginalDescription()

        // When
        val matches = getItalianMealsForLargeGroups()

        // Then
        assertThat(matches).hasSize(0)
    }

    @Test
    fun `should return all meals when 'for-large-groups' tag matches alongside italian keyword in the description`() {
        // Given
        every { mealRepository.getAllMeals() } returns getListOfMealsWithMatchingDescriptionAndLargeGroupsTag()

        // When
        val matches = getItalianMealsForLargeGroups()

        // Then
        assertThat(matches).containsExactlyElementsIn(
            listOf(
                defaultMeal(tags = listOf("for-large-groups"), description = "original italian  meal"),
                defaultMeal(tags = listOf("for-large-groups"), description = "original meal from italy"),
                defaultMeal(tags = listOf("for-large-groups"), description = "best italian original meal")
            )
        )
    }

    private fun defaultMeal(
        name: String = "Koshry",
        id: Int = 1,
        minutes: Int = 15,
        contributorId: Int = 1,
        submitted: String = "2024-12-31",
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

    private fun getListOfMealsWithTags(): List<Meal> {
        return listOf(
            defaultMeal(tags = listOf("spicy", "small")),
            defaultMeal(name = "italian pasta", tags = listOf("for-large-groups", "old"), description = "Original"),
            defaultMeal(tags = listOf("salty", "for-large-groups")),
            defaultMeal(tags = listOf("for-large-groups", "trendy"), description = "Original ITALIAN meal")
        )
    }

    private fun getListOfMatchingMeals(numberOfItems: Int): List<Meal> {
        return List(numberOfItems) {
            defaultMeal(tags = listOf("italian", "for-large-groups", "old"), description = "Good original italian")
        }
    }

    private fun getListOfMealsWithoutItalianTag(): List<Meal> =
        List(4) { defaultMeal(tags = listOf("for-large-groups"), description = "original meal") }

    private fun getListOfMealsWithoutLargeGroupsTag(): List<Meal> =
        List(4) { defaultMeal(tags = listOf("italian"), description = "original meal") }

    private fun getListOfMealsWithoutOriginalDescription(): List<Meal> =
        List(4) { defaultMeal(name = "italian pasta", tags = listOf("for-large-groups")) }

    private fun getListOfMealsWithMatchingDescriptionAndLargeGroupsTag(): List<Meal> {
        return listOf(
            defaultMeal(tags = listOf("for-large-groups"), description = "italian  meal"),
            defaultMeal(tags = listOf("for-large-groups"), description = "original italian  meal"),
            defaultMeal(tags = listOf("for-large-groups"), description = "original meal from italy"),
            defaultMeal(tags = listOf("for-large-groups"), description = "best italian original meal")
        )
    }

}