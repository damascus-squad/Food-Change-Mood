package org.damascus.presentation.utils

import org.damascus.model.Meal
import org.damascus.utils.TerminalColor
import org.damascus.utils.withStyle

class ConsoleDisplay: Display{

    override fun meals(meals: List<Meal>) {
        meals.forEachIndexed { _, meal ->
            println("\n🍽️ Meal Name: ${meal.name}".withStyle(TerminalColor.Green))
            println("\n🍽️ Meal Id: ${meal.id}".withStyle(TerminalColor.Green))
            println("ℹ️ Description: ${meal.description}".withStyle(TerminalColor.Green))

            if (meal.minutes >= 60) {
                val hours = meal.minutes / 60
                val remainingMinutes = meal.minutes % 60
                val timeText = if (remainingMinutes > 0) "${hours}h ${remainingMinutes}m" else "${hours}h"
                println("⌚ Preparation Time: $timeText".withStyle(TerminalColor.Cyan))
            } else {
                println("⌚ Preparation Time: ${meal.minutes}m".withStyle(TerminalColor.Cyan))
            }
            println("📅 Submitted On: ${meal.submitted}".withStyle(TerminalColor.Green))
            println("🍴 Ingredients: ${meal.ingredients.joinToString(", ")}".withStyle(TerminalColor.Green))
            println("🔢 ${meal.stepsCount} Steps to Prepare:".withStyle(TerminalColor.Green))
            meal.steps.forEachIndexed { index, step ->
                println("  ${index + 1}. $step")
            }

            println("\n🍏 Nutritional Information:")
            with(meal.nutrition) {
                println("🔸 Calories: $calories kcal".withStyle(TerminalColor.Green))
                println("🔸 Total Fat: $totalFat g".withStyle(TerminalColor.Green))
                println("🔸 Saturated Fat: $saturatedFat g".withStyle(TerminalColor.Green))
                println("🔸 Carbohydrates: $carbohydrates g".withStyle(TerminalColor.Green))
                println("🔸 Sugar: $sugar g".withStyle(TerminalColor.Green))
                println("🔸 Sodium: $sodium mg".withStyle(TerminalColor.Green))
                println("🔸 Protein: $protein g".withStyle(TerminalColor.Green))
            }

            println("\n🏷️ Tags: ${meal.tags.joinToString(" || ")}".withStyle(TerminalColor.Green))
        }
    }

    override fun mealsName(meals: List<Meal>) {
        meals.forEach { meal ->
            println("🍽 #[${meal.id}] ${meal.name}".withStyle(TerminalColor.Green))
            println("—".repeat(40).withStyle(TerminalColor.Magenta))
        }
    }

    override fun specificMeal(meal: Meal) {
        TODO("Not yet implemented")
    }
}