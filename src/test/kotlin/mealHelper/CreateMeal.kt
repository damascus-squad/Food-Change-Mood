package mealHelper

import model.Nutrition
import org.damascus.model.Meal

fun createMeal(
    name: String = "Test Meal",
    id: Int = 1234,
    minutes: Int = 30,
    contributorId: Int = 999,
    submitted: String = "2024-04-23",
    tags: List<String> = listOf("test", "default"),
    protein: Double = 0.0,
    carbohydrates: Double = 0.0,
    totalFat: Double = 0.0,
    calories: Double = 0.0,
    sugar: Double = 0.0,
    sodium: Double = 0.0,
    saturatedFat: Double = 0.0,
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
    nutrition = Nutrition(
        protein = protein,
        carbohydrates = carbohydrates,
        totalFat = totalFat,
        calories = calories,
        sugar = sugar,
        sodium = sodium,
        saturatedFat = saturatedFat,
    ),
    stepsCount = stepsCount,
    steps = steps,
    description = description,
    ingredients = ingredients,
    ingredientsCount = ingredientsCount
)

