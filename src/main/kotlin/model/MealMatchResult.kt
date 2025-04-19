package org.damascus.model

data class MealMatchResult(
    val meal: Meal,
    val textMatchResult: TextMatchResult
) : Comparable<MealMatchResult> {
    override fun compareTo(other: MealMatchResult): Int {
        return textMatchResult.compareTo(other.textMatchResult)
    }
}

