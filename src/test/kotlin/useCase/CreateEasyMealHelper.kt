package useCase

import model.Nutrition
import org.damascus.model.Meal

fun createEasyMeal(
    name: String = "Easy Meal",
    id: Int = 1,
    minutes: Int = 5,
    contributorId: Int = 100,
    submitted: String = "2024-04-23",
    tags: List<String> = listOf("easy", "quick"),
    protein: Double = 10.0,
    carbohydrates: Double = 15.0,
    totalFat: Double = 5.0,
    calories: Double = 180.0,
    sugar: Double = 3.0,
    sodium: Double = 200.0,
    saturatedFat: Double = 1.0,
    stepsCount: Int = 2,
    steps: List<String> = listOf("Mix ingredients", "Serve chilled"),
    description: String = "A simple and quick meal",
    ingredients: List<String> = listOf("Yogurt", "Fruits", "Honey"),
    ingredientsCount: Int = ingredients.size
): Meal {
    return Meal(
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
}
