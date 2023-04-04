package com.tb.calculator.keyboard

import androidx.annotation.StringDef


@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    CalculatorButtonType.ONE,
    CalculatorButtonType.TWO,
    CalculatorButtonType.THREE,
    CalculatorButtonType.FOUR,
    CalculatorButtonType.FIVE,
    CalculatorButtonType.SIX,
    CalculatorButtonType.SEVEN,
    CalculatorButtonType.EIGHT,
    CalculatorButtonType.NINE,
    CalculatorButtonType.ZERO,
    CalculatorButtonType.CLEAR,
    CalculatorButtonType.DELETE,
    CalculatorButtonType.PERCENT,
    CalculatorButtonType.PLUS,
    CalculatorButtonType.SUBTRACT,
    CalculatorButtonType.MULTIPLY,
    CalculatorButtonType.DIVIDE,
    CalculatorButtonType.POINT,
    CalculatorButtonType.EQUAL,
    CalculatorButtonType.UNKNOWN
)
annotation class CalculatorButtonType {
    companion object {
        const val ONE = "1"
        const val TWO = "2"
        const val THREE = "3"
        const val FOUR = "4"
        const val FIVE = "5"
        const val SIX = "6"
        const val SEVEN = "7"
        const val EIGHT = "8"
        const val NINE = "9"
        const val ZERO = "0"
        const val CLEAR = "C"
        const val DELETE = "DEL"
        const val PERCENT = "%"
        const val PLUS = "+"
        const val SUBTRACT = "-"
        const val MULTIPLY = "*"
        const val DIVIDE = "÷"
        const val POINT = "."
        const val EQUAL = "="
        const val UNKNOWN = "null"
    }
}
