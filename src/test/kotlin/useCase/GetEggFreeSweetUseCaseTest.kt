package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.damascus.logic.MealRepository
import org.damascus.useCase.GetEggFreeSweetUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


import model.Nutrition
import org.damascus.model.Meal


class GetEggFreeSweetUseCaseTest{
 private lateinit var mealRepo: MealRepository
 private lateinit var useCase: GetEggFreeSweetUseCase




 @BeforeEach
 fun setUp() {
  mealRepo = mockk()
  useCase = GetEggFreeSweetUseCase(mealRepo)
 }


 @Test
 fun `invoke should return egg-free sweet from repository`() {
  // Given
  val expectedMeal = createTestMeal(
   id = 1,
   name = "Sorbet",
   tags = listOf("sweet", "fruity"),
   ingredients = listOf("sugar", "fruit")
  )
  every { mealRepo.getEggFreeSweet() } returns expectedMeal


  // When
  val result = useCase()


  // Then
  assertThat(result).isEqualTo(expectedMeal)
  verify(exactly = 1) { mealRepo.getEggFreeSweet() }
 }


 @Test
 fun `invoke should throw NoSuchElementException when repository throws it`() {
  // Given
  every { mealRepo.getEggFreeSweet() } throws NoSuchElementException("No more egg-free sweets left.")


  // When/Then
  val exception = org.junit.jupiter.api.assertThrows<NoSuchElementException> {
   useCase()
  }


  assertThat(exception).hasMessageThat().contains("No more egg-free sweets left")
  verify(exactly = 1) { mealRepo.getEggFreeSweet() }
 }
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

