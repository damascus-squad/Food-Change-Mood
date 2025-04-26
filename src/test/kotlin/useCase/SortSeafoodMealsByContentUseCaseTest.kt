package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import mealHelper.createMeal
import org.damascus.logic.MealRepository
import org.damascus.useCase.retrieve.SortSeafoodMealsByContentUseCase
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
        //given
        val meals = listOf(
            createMeal(name = "Shrimp", protein = 30.0, tags = listOf("seafood")),
            createMeal(name = "Fish", protein = 25.0, tags = listOf("seafood")),
            createMeal(name = "Crab", protein = 10.0, tags = listOf("seafood"))
        )
        every { mealsRepository.getAllMeals() } returns meals
        //when
        val result = sortSeafoodMealsByContentUseCase()
        //then
        assertThat(result.map { it.name to it.nutrition.protein }).containsExactly(
            "Shrimp" to 30.0,
            "Fish" to 25.0,
            "Crab" to 10.0
        ).inOrder()
    }

    @Test
    fun `should return seafood meals When it contains multiple tags, one of which includes seafood`() {
        //given
        val meals = listOf(
            createMeal(name = "Shrimp Curry", protein = 32.0, tags = listOf("spicy", "seafood", "indian")),
            createMeal(name = "Grilled Fish", protein = 28.0, tags = listOf("grilled", "SeaFood", "healthy")),
            createMeal(name = "Beef Steak", protein = 50.0, tags = listOf("meat", "grilled"))
        )

        every { mealsRepository.getAllMeals() } returns meals
        //when
        val result = sortSeafoodMealsByContentUseCase()
        //then
        assertThat(result.map { it.name to it.nutrition.protein }).containsExactly(
            "Shrimp Curry" to 32.0,
            "Grilled Fish" to 28.0,
        ).inOrder()
    }


    @Test
    fun `should return seafood meals when tag contain seafood regardless of case`() {
        //given
        val meals = listOf(
            createMeal(name = "Shrimp", protein = 30.0, tags = listOf("SeaFood")),
            createMeal(name = "Fish", protein = 20.0, tags = listOf("SEAFOOD")),
            createMeal(name = "Crab", protein = 10.0, tags = listOf("seafOOd"))
        )
        every { mealsRepository.getAllMeals() } returns meals
        //when
        val result = sortSeafoodMealsByContentUseCase()
        //then
        assertThat(result.map { it.name to it.nutrition.protein }).containsExactly(
            "Shrimp" to 30.0,
            "Fish" to 20.0,
            "Crab" to 10.0
        ).inOrder()
    }

    @Test
    fun `should ignore non-seafood meals when tag not contain seafood`() {
        //given
        val meals = listOf(
            createMeal(name = "Beef", protein = 40.0, tags = listOf("meat")),
            createMeal(name = "Chicken", protein = 35.0, tags = listOf("poultry"))
        )
        every { mealsRepository.getAllMeals() } returns meals
        //when
        val result = sortSeafoodMealsByContentUseCase()
        //then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should ignore seafood meals when empty tags`() {
        //given
        val meals = listOf(
            createMeal(name = "Shrimp", protein = 20.0, tags = emptyList()),
        )
        every { mealsRepository.getAllMeals() } returns meals
        //when
        val result = sortSeafoodMealsByContentUseCase()
        //then
        assertThat(result).isEmpty()
    }

}