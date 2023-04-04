package com.tb.calculator.keyboard

import java.util.*

object CalculateUtil {

    fun calculateExpression(expression: String): Double {
        val tokens = expression.trim().split("\\s+".toRegex())
        val stack = Stack<Double>()
        var currentOperator = '+'
        for (token in tokens) {
            if (token.length == 1 && token[0] in listOf('+', '-', '*', '/')) {
                currentOperator = token[0]
            } else {
                val operand = token.toDouble()
                when (currentOperator) {
                    '+' -> stack.push(operand)
                    '-' -> stack.push(-operand)
                    '*' -> stack.push(stack.pop() * operand)
                    '/' -> stack.push(stack.pop() / operand)
                }
            }
        }
        var result = 0.0
        while (stack.isNotEmpty()) {
            result += stack.pop()
        }
        return result
    }
}