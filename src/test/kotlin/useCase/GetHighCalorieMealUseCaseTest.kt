package useCase

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.damascus.logic.MealRepository
import org.damascus.useCase.GetHighCalorieMealUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import mealHelper.createMeal
import org.damascus.model.Meal

class GetHighCalorieMealUseCaseTest {

 private lateinit var repository: MealRepository
 private lateinit var getHighCalorieMealUseCase: GetHighCalorieMealUseCase

 @BeforeEach
 fun setup() {
  repository = mockk(relaxed = true)
  getHighCalorieMealUseCase = GetHighCalorieMealUseCase(repository)
 }

 @Test
 fun `should filter meals above 700 calories`() {
  // Given
  val highCalorieMail1 = createMeal(id = 1, calories = 800.0)
  val highCalorieMail2 = createMeal(id = 2, calories = 900.0)
  val lowCalorieMeal = createMeal(id = 3, calories = 600.0)

  val meals = listOf(highCalorieMail1, highCalorieMail2, lowCalorieMeal)
  every { repository.getAllMeals() } returns meals

  // When
  val result = getHighCalorieMealUseCase()

  // Then
  assertThat(result).containsExactly(highCalorieMail1, highCalorieMail2)
 }

 @Test
 fun `should return empty list when no high calorie meals exist`() {
  // Given
  val lowCalorieMeal1 = createMeal(id = 1, calories = 500.0)
  val lowCalorieMeal2 = createMeal(id = 2, calories = 400.0)
  val lowCalorieMeal3 = createMeal(id = 3, calories = 600.0)

  val meals = listOf(lowCalorieMeal1, lowCalorieMeal2, lowCalorieMeal3)
  every { repository.getAllMeals() } returns meals

  // When
  val result = getHighCalorieMealUseCase()

  // Then
  assertThat(result).isEmpty()
 }

 @Test
 fun `should exclude meals with exactly 700 calories`() {
  // Given
  val highCalorieMail1 = createMeal(id = 1, calories = 700.0)
  val highCalorieMail2 = createMeal(id = 2, calories = 701.0)

  val meals = listOf(highCalorieMail1, highCalorieMail2)
  every { repository.getAllMeals() } returns meals

  // When
  val result = getHighCalorieMealUseCase()

  // Then
  assertThat(result).containsExactly(highCalorieMail2)
 }

 @Test
 fun `should return empty list when meals list is empty`() {
  // Given
  val meals = emptyList<Meal>()
  every { repository.getAllMeals() } returns meals

  // When
  val result = getHighCalorieMealUseCase()

  // Then
  assertThat(result).isEmpty()
 }
}