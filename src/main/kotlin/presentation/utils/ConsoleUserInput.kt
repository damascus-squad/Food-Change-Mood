package org.damascus.presentation.utils

import org.damascus.domain.InputException
import org.damascus.utils.TerminalColor
import org.damascus.utils.withStyle

class ConsoleUserInput : InputReader {
    override fun readString(prompt: String): String {
        print("$prompt ".withStyle(TerminalColor.Blue))
        while (true) {
            val input = readln().trim()

            if (input.isEmpty()) {
                println("❌ Input cannot be empty. Please try again.".withStyle(TerminalColor.Red))
                print("$prompt ".withStyle(TerminalColor.Blue))
            } else return input
        }
    }

    override fun readInt(prompt: String, min: Int?, max: Int?): Int {
        print("$prompt ")
        val input = readlnOrNull()?.trim()?.toIntOrNull() ?: throw InputException("Wrong input: ")

        if ((min != null && input < min) || (max != null && input > max)) {
            throw InputException("الإدخال خارج النطاق المسموح.")
        }

        return input
    }

    override fun readBoolean(): Boolean {
        while (true) {
            print("Do you like it? (y/n): ".withStyle(TerminalColor.Green))
            when (readlnOrNull()?.trim()?.lowercase()) {
                "y" -> return true
                "n" -> return false
                else -> println("Invalid input. Please enter 'y' or 'n'.".withStyle(TerminalColor.Red))
            }
        }
    }

    override fun readDouble(prompt: String): Double {
        println(prompt.withStyle(TerminalColor.Blue))
        while (true) {
            val input = readlnOrNull()?.toDoubleOrNull()

            if (input == null) {
                println("❌ Invalid number. Please try again.".withStyle(TerminalColor.Red))
                println(prompt.withStyle(TerminalColor.Blue))
            } else {
                return input
            }
        }
    }
}