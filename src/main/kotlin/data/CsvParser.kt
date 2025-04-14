package org.damascus.data

import model.*
import org.damascus.model.Meal


class CsvParser {
    fun parseLine(row: Map<String,String>): Meal {
        val tokenizedList:List<String> = row.values.toList()
        return Meal(
            name = tokenizedList[FoodColumnIndex.NAME],
            id = tokenizedList[FoodColumnIndex.ID].toIntOrNull(),
            minutes = tokenizedList[FoodColumnIndex.MINUTES].toIntOrNull(),
            contributorId = tokenizedList[FoodColumnIndex.CONTRIBUTOR_ID].toIntOrNull(),
            submitted = tokenizedList[FoodColumnIndex.SUBMITTED],
            tags = parseListOfData(tokenizedList[FoodColumnIndex.TAGS]),
            nutrition = constructNutritionFromToken(tokenizedList[FoodColumnIndex.NUTRITION]),
            nSteps = tokenizedList[FoodColumnIndex.N_STEPS].toIntOrNull(),
            steps = parseListOfData(tokenizedList[FoodColumnIndex.STEPS]),
            description = tokenizedList[FoodColumnIndex.DESCRIPTION],
            ingredients = parseListOfData(tokenizedList[FoodColumnIndex.INGREDIENTS]),
            nIngredients = tokenizedList[FoodColumnIndex.N_INGREDIENTS].toIntOrNull()
        )
    }

    private fun parseListOfData(raw: String): List<String> {
        return raw.removePrefix("[")
            .removeSuffix("]")
            .split(",")
    }

    private fun constructNutritionFromToken(raw: String): Nutrition {
        val nutrition = parseListOfData(raw).map { it.trim().toDoubleOrNull() }
        return Nutrition(
            calories = nutrition[NutritionIndex.CALORIES],
            totalFat = nutrition[NutritionIndex.TOTAL_FAT],
            sugar = nutrition[NutritionIndex.SUGAR],
            sodium = nutrition[NutritionIndex.SODIUM],
            protein = nutrition[NutritionIndex.PROTEIN],
            saturatedFat = nutrition[NutritionIndex.SATURATED_FAT],
            carbohydrates = nutrition[NutritionIndex.CARBOHYDRATES],
        )
    }

}