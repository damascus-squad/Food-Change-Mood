package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.damascus.logic.MealRepository
import org.damascus.useCase.SortSeafoodMealsByContentUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SortSeafoodMealsByContentUseCaseTest {

    private lateinit var mealsRepository: MealRepository
    private lateinit var sortSeafoodMealsByContentUseCase: SortSeafoodMealsByContentUseCase

    @BeforeEach
    fun setup() {
        mealsRepository = mockk(relaxed = true)
        sortSeafoodMealsByContentUseCase = SortSeafoodMealsByContentUseCase(mealsRepository)
    }

    @Test
    fun `should return seafood meals when contain tag seafood sorted by descending protein`() {

        val meals = listOf(
            createSeafoodMeal(name = "Shrimp", protein = 30.0, tags = listOf("seafood")),
            createSeafoodMeal(name = "Fish", protein = 25.0, tags = listOf("seafood")),
            createSeafoodMeal(name = "Crab", protein = 10.0, tags = listOf("seafood"))
        )
        every { mealsRepository.getAllMeals() } returns meals

        val result = sortSeafoodMealsByContentUseCase.invoke()

        assertThat(result.map { it.name to it.nutrition.protein }).containsExactly(
            "Shrimp" to 30.0,
            "Fish" to 25.0,
            "Crab" to 10.0
        ).inOrder()
    }

    @Test
    fun `should return seafood meals When it contains multiple tags, one of which includes seafood`() {
        val meals = listOf(
            createSeafoodMeal(name = "Shrimp Curry", protein = 32.0, tags = listOf("spicy", "seafood", "indian")),
            createSeafoodMeal(name = "Grilled Fish", protein = 28.0, tags = listOf("grilled", "SeaFood", "healthy")),
            createSeafoodMeal(name = "Beef Steak", protein = 50.0, tags = listOf("meat", "grilled"))
        )

        every { mealsRepository.getAllMeals() } returns meals

        val result = sortSeafoodMealsByContentUseCase()

        assertThat(result.map { it.name to it.nutrition.protein }).containsExactly(
            "Shrimp Curry" to 32.0,
            "Grilled Fish" to 28.0,
        ).inOrder()
    }


    @Test
    fun `should return seafood meals when tag contain seafood regardless of case`() {
        val meals = listOf(
            createSeafoodMeal(name = "Shrimp", protein = 30.0, tags = listOf("SeaFood")),
            createSeafoodMeal(name = "Fish", protein = 20.0, tags = listOf("SEAFOOD")),
            createSeafoodMeal(name = "Crab", protein = 10.0, tags = listOf("seafOOd"))
        )
        every { mealsRepository.getAllMeals() } returns meals

        val result = sortSeafoodMealsByContentUseCase.invoke()

        assertThat(result.map { it.name to it.nutrition.protein }).containsExactly(
            "Shrimp" to 30.0,
            "Fish" to 20.0,
            "Crab" to 10.0
        ).inOrder()
    }

    @Test
    fun `should ignore non-seafood meals when tag not contain seafood`() {
        val meals = listOf(
            createSeafoodMeal(name = "Beef", protein = 40.0, tags = listOf("meat")),
            createSeafoodMeal(name = "Chicken", protein = 35.0, tags = listOf("poultry"))
        )
        every { mealsRepository.getAllMeals() } returns meals

        val result = sortSeafoodMealsByContentUseCase.invoke()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should ignore seafood meals when empty tags`() {
        val meals = listOf(
            createSeafoodMeal(name = "Shrimp", protein = 20.0, tags = emptyList()),
        )
        every { mealsRepository.getAllMeals() } returns meals

        val result = sortSeafoodMealsByContentUseCase.invoke()

        assertThat(result).isEmpty()
    }


}