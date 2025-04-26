package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.damascus.domain.InputException
import org.damascus.presentation.FoodChangeMoodUi
import org.damascus.presentation.UiAction
import org.damascus.presentation.io.ConsoleUserInput
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

class FoodChangeMoodUiTest {
    private val consoleUserInput = mockk<ConsoleUserInput>(relaxed = true)

    @ParameterizedTest
    @CsvSource(
        "1",  // select 1 -> actionMock1
        "2",  // select 2 -> actionMock2
        "3",  // select 3 -> actionMock3
        "4",  // select 4 -> actionMock4
        "0"   // NO Action Selected
    )
    fun `should invoke correct specific action when user selects option`(selectedOption: Int) {
        // Given
        every { consoleUserInput.readInt(any(), any(), any()) } returnsMany listOf(selectedOption, 0)

        val actionMock1 = mockk<() -> Unit>(relaxed = true)
        val actionMock2 = mockk<() -> Unit>(relaxed = true)
        val actionMock3 = mockk<() -> Unit>(relaxed = true)
        val actionMock4 = mockk<() -> Unit>(relaxed = true)

        val ui = FoodChangeMoodUi(
            mealSearchUI = mockk(relaxed = true),
            mealSuggestUi = mockk(relaxed = true),
            mealRetrieveUi = mockk(relaxed = true),
            ingredientGameUi = mockk(relaxed = true),
            guessGameUI = mockk(relaxed = true),
            consoleUserInput = consoleUserInput
        )

        val testUiActions = listOf(
            UiAction(name = "Test Action 1", action = actionMock1),
            UiAction(name = "Test Action 2", action = actionMock2),
            UiAction(name = "Test Action 3", action = actionMock3),
            UiAction(name = "Test Action 4", action = actionMock4),
        )

        // When
        val showMenuMethod = FoodChangeMoodUi::class.java.getDeclaredMethod("showMenu", List::class.java)
        showMenuMethod.isAccessible = true
        showMenuMethod.invoke(ui, testUiActions)

        // Then
        when (selectedOption) {
            1 -> {
                verify(exactly = 1) { actionMock1.invoke() }
                verify(exactly = 0) { actionMock2.invoke() }
                verify(exactly = 0) { actionMock3.invoke() }
                verify(exactly = 0) { actionMock4.invoke() }
            }
            2 -> {
                verify(exactly = 0) { actionMock1.invoke() }
                verify(exactly = 1) { actionMock2.invoke() }
                verify(exactly = 0) { actionMock3.invoke() }
                verify(exactly = 0) { actionMock4.invoke() }
            }
            3 -> {
                verify(exactly = 0) { actionMock1.invoke() }
                verify(exactly = 0) { actionMock2.invoke() }
                verify(exactly = 1) { actionMock3.invoke() }
                verify(exactly = 0) { actionMock4.invoke() }
            }
            4 -> {
                verify(exactly = 0) { actionMock1.invoke() }
                verify(exactly = 0) { actionMock2.invoke() }
                verify(exactly = 0) { actionMock3.invoke() }
                verify(exactly = 1) { actionMock4.invoke() }
            }
            0 -> {
                verify(exactly = 0) { actionMock1.invoke() }
                verify(exactly = 0) { actionMock2.invoke() }
                verify(exactly = 0) { actionMock3.invoke() }
                verify(exactly = 0) { actionMock4.invoke() }
            }
        }
    }

    @Test
    fun `should handle InputException and continue`() {
        //Given
        val ui = FoodChangeMoodUi(
            mealSearchUI = mockk(relaxed = true),
            mealSuggestUi = mockk(relaxed = true),
            mealRetrieveUi = mockk(relaxed = true),
            ingredientGameUi = mockk(relaxed = true),
            guessGameUI = mockk(relaxed = true),
            consoleUserInput = consoleUserInput
        )

        //When
        ui.start()

        //Then
        every { consoleUserInput.readInt(any(), any(), any()) } throws InputException("Invalid input") andThen 0
    }
}