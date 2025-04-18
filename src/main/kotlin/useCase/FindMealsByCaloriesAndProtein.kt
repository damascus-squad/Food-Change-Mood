package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class FindMealsByCaloriesAndProtein(
    private val mealRepository: MealRepository
) {

    operator fun invoke(): List<Meal> {

        val targetCalories: Double = getInputs("Enter Calories amount: ")
        val targetProtein: Double = getInputs("Enter Protein amount: ")

        val caloriesTolerance = 15.0
        val proteinTolerance = 3.0

        val targetMeal = mealRepository.getAllMeals()
            .filter {
                it.nutrition.calories in targetCalories - caloriesTolerance..targetCalories + caloriesTolerance &&
                        it.nutrition.protein in targetProtein - proteinTolerance..targetProtein + proteinTolerance
            }

        return targetMeal

    }

    private fun getInputs(title: String): Double {
        print(title)
        var userInput: Double? = readLine()?.toDoubleOrNull()
        while (userInput == null) {
            print("Wrong input: ")
            userInput = readLine()?.toDoubleOrNull()
        }
        return userInput
    }


}
