package org.damascus.model

data class TextMatchResult(
    val startIndex: Int,
    val errors: Int
) : Comparable<TextMatchResult> {
    override fun compareTo(other: TextMatchResult): Int {
        val errorComparison = errors.compareTo(other.errors)
        return if (errorComparison != 0) errorComparison else startIndex.compareTo(other.startIndex)
    }
}