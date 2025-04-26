package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import org.damascus.model.MealMatchResult
import org.damascus.model.TextMatchResult

class SearchMealByNameUseCase(
    private val mealRepo: MealRepository,
) {
    operator fun invoke(searchPhrase: String, maxErrors: Int = 2): List<Meal> {

        if (maxErrors < 0) return emptyList()

        if (searchPhrase.length > MAX_ALLOWED_SEARCH_PHRASE_LENGTH) {
            throw IllegalArgumentException("Search phrase too long: maximum length is 31 characters")
        }

        val results = mutableListOf<MealMatchResult>()

        mealRepo.getAllMeals().forEach { meal ->
            val searchMatches = bitapFuzzySearch(
                text = meal.name,
                pattern = searchPhrase,
                maxErrors = maxErrors
            )

            if (searchMatches.isNotEmpty()) {
                results.add(
                    MealMatchResult(
                        meal = meal,
                        textMatchResult = searchMatches.first()
                    )
                )
            }
        }

        return results.sorted().map { it.meal }
    }

    /**
     * Performs a fuzzy search using the Bitap algorithm with bitwise operations.
     * @param text The text to search within
     * @param pattern The pattern to search for
     * @param maxErrors The maximum number of allowed errors/differences
     * @return List of starting indices where matches were found
     */
    private fun bitapFuzzySearch(text: String, pattern: String, maxErrors: Int): List<TextMatchResult> {

        val patternLength = pattern.length

        // Initialize state array (k+1 states, each initially set to ~1)
        val states = LongArray(maxErrors + 1) { -2L }

        // If pattern length is m, we need the m-th bit (position m) to be 0 after the shift
        val finalBitMask = 1L shl patternLength

        val characterMasks = buildCharacterMasks(pattern)

        val matches = mutableListOf<TextMatchResult>()
        for (i in text.indices) {
            updateStates(states, characterMasks[text[i].code], maxErrors)

            for (j in 0..maxErrors) {
                if ((states[j] and finalBitMask) == 0L) {
                    val startIndex = i - patternLength + 1
                    matches.add(TextMatchResult(startIndex = startIndex, errors = j))

                    // no need to continue checking the text after a perfect match
                    if (j == 0) return matches

                    break // no need to check for higher error counts (d+1, d+2...).
                }
            }
        }

        return matches
    }

    /**
     * Creates bit masks for each character in the alphabet based on the pattern.
     * @param pattern The search pattern
     * @return Array of bit masks for all possible characters
     */
    private fun buildCharacterMasks(pattern: String): LongArray {
        val masks = LongArray(256) { -1L }  // Initialize all masks to ~0 (all bits set)

        for (i in pattern.indices) {
            // Clear bit 'i' for the corresponding character in the pattern
            val charCode = pattern[i].code
            masks[charCode] = masks[charCode] and (1L shl i).inv()
        }

        return masks
    }

    /**
     * Updates the states array for the current character.
     * @param states The array of states to update
     * @param charMask The bit mask for the current character
     * @param maxErrors The maximum number of allowed errors
     */
    private fun updateStates(states: LongArray, charMask: Long, maxErrors: Int) {
        // Save initial state for use in the inner loop
        var previousState = states[0]

        // Update state 0 (exact match state)
        states[0] = (states[0] or charMask) shl 1

        // Update states for 1 to k errors
        for (i in 1..maxErrors) {
            val currentState = states[i]

            // Update considering substitution errors
            states[i] = (previousState and (states[i] or charMask)) shl 1

            previousState = currentState
        }
    }

    private companion object {
        const val MAX_ALLOWED_SEARCH_PHRASE_LENGTH = 31
    }

}