package org.damascus.data

import model.*
import org.damascus.data.utils.FoodColumnIndex
import org.damascus.data.utils.NutritionIndex
import org.damascus.model.Meal


class MealDataParser {
    fun parseLine(row: String): Meal {
        val tokenizedList: List<String> = formatLineOfData(row)
        return Meal(
            name = tokenizedList[FoodColumnIndex.NAME],
            id = tokenizedList[FoodColumnIndex.ID].toIntOrNull() ?: throw IllegalArgumentException("Missing id"),
            minutes = tokenizedList[FoodColumnIndex.MINUTES].toIntOrNull() ?: 0,
            contributorId = tokenizedList[FoodColumnIndex.CONTRIBUTOR_ID].toIntOrNull()
                ?: throw IllegalArgumentException("Missing id"),
            submitted = tokenizedList[FoodColumnIndex.SUBMITTED],
            tags = parseListOfData(tokenizedList[FoodColumnIndex.TAGS]),
            nutrition = constructNutritionFromToken(tokenizedList[FoodColumnIndex.NUTRITION]),
            stepsCount = tokenizedList[FoodColumnIndex.N_STEPS].toIntOrNull() ?: 0,
            steps = parseListOfData(tokenizedList[FoodColumnIndex.STEPS]),
            description = tokenizedList[FoodColumnIndex.DESCRIPTION],
            ingredients = parseListOfData(tokenizedList[FoodColumnIndex.INGREDIENTS]),
            ingredientsCount = tokenizedList[FoodColumnIndex.N_INGREDIENTS].toIntOrNull() ?: 0
        )
    }

    private fun formatLineOfData(str: String): List<String> {
        val result = mutableListOf<String>()
        val sb = StringBuilder()
        var insideQuotes = false

        for (char in str) {
            when (char) {
                '"' -> {
                    insideQuotes = !insideQuotes
                    sb.append(char)
                }

                ',' -> {
                    if (insideQuotes) sb.append(char)
                    else {
                        result.add(sb.toString())
                        sb.clear()
                    }
                }

                else -> sb.append(char)
            }
        }
        if (sb.isNotEmpty()) result.add(sb.toString())
        return result
    }

    private fun parseListOfData(raw: String): List<String> {
        return  raw
            .split(",")
            .map {
                it.replace("'", "")
                .replace("\"", "")
                .replace("[","")
                .replace("]","").trim() }
            .filter { it.isNotBlank() }
    }

    private fun constructNutritionFromToken(raw: String): Nutrition {
        val nutrition = parseListOfData(raw).map { it.trim().toDoubleOrNull() }
        return Nutrition(
            calories = nutrition[NutritionIndex.CALORIES] ?: 0.0,
            totalFat = nutrition[NutritionIndex.TOTAL_FAT] ?: 0.0,
            sugar = nutrition[NutritionIndex.SUGAR] ?: 0.0,
            sodium = nutrition[NutritionIndex.SODIUM] ?: 0.0,
            protein = nutrition[NutritionIndex.PROTEIN] ?: 0.0,
            saturatedFat = nutrition[NutritionIndex.SATURATED_FAT] ?: 0.0,
            carbohydrates = nutrition[NutritionIndex.CARBOHYDRATES] ?: 0.0,
        )
    }

}