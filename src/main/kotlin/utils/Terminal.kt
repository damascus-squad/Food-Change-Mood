package org.damascus.utils


enum class TerminalColor(val code: String) {
    Red("\u001B[31m"),
    Green("\u001B[32m"),
    Yellow("\u001B[33m"),
    Blue("\u001B[34m"),
    Magenta("\u001B[35m"),
    Cyan("\u001B[36m"),
    Reset("\u001B[0m");

    fun wrap(text: String) = "$code$text${Reset.code}"
}

fun String.withStyle(color: TerminalColor) = color.wrap(this)
