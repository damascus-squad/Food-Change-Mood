package org.damascus.presentation.io

import org.damascus.model.Meal
import org.damascus.utils.printTable

class ConsoleDisplay : Display {

    override fun displayMealsBy(
        meals: List<Meal>,
        label: String,
        contentSelector: ((Meal) -> Map<String, Any?>)?
    ) {
        if (meals.isEmpty()) {
            println("❌ No meals to display.")
            return
        }

        println("📋 Displaying meals by $label:")

        val headers = if (contentSelector != null) {
            listOf("ID") + contentSelector(meals.first()).keys.toList()
        } else {
            listOf(
                "ID",
                "Name",
                "Preparation Time",
                "Contributor Id",
                "Date",
                "Tags",
                "Nutritional Information",
                "Steps Count",
                "Steps",
                "Description",
                "Ingredients",
                "Ingredients Count"
            )
        }

        // Map the meal data into rows
        val data = meals.map { meal ->
            val contentMap = contentSelector?.let { it(meal) }

            if (contentMap != null) {
                listOf(meal.id.toString().take(8)) + headers.drop(1).map { contentMap[it] ?: "-" }
            } else {
                listOf(
                    meal.id.toString().take(8),
                    meal.name,
                    meal.minutes.toString() + " Min",
                    meal.contributorId,
                    meal.submitted,
                    meal.tags.joinToString(", "),
                    meal.nutrition,
                    meal.stepsCount.toString(),
                    meal.steps.joinToString(", "),
                    meal.description,
                    meal.ingredients.joinToString(", "),
                    meal.ingredients.size.toString()
                )
            }
        }

        // Print the table using the headers and the meal data
        printTable(headers, data)
    }

}