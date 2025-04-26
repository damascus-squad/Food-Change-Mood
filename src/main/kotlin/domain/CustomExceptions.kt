package org.damascus.domain

class NoSuchMealException(message: String) : NoSuchElementException(message)
class IllegalDateFormatException(message: String) : IllegalArgumentException(message)
class InputException(message: String) : IllegalArgumentException(message)