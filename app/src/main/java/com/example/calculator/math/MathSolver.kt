package com.example.calculator.math

import com.example.calculator.math.model.MathResult
import com.example.calculator.math.model.MathToken
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt as ksqrt

class MathSolver(
    private val mathContext: MathContext = MathContext.UNLIMITED
) {
    companion object {
        private const val MAX_EXPONENT = 1_000
    }

    fun solve(expression: String): MathResult {
        return try {
            val tokens = Tokenizer.tokenize(expression)
            val rpn = convertToRPN(tokens)
            val result = evaluateRPN(rpn)
            val formatted = formatResult(result)
            MathResult.Success(formatted)
        } catch (e: Exception) {
            MathResult.Error(e.message ?: "Ошибка вычисления")
        }
    }

    private fun convertToRPN(tokens: List<MathToken>): List<MathToken> {
        val output = mutableListOf<MathToken>()
        val stack = mutableListOf<MathToken>()

        for (token in tokens) {
            when (token) {
                is MathToken.Number -> output.add(token)
                is MathToken.LeftParen -> stack.add(token)
                is MathToken.RightParen -> {
                    while (stack.isNotEmpty() && stack.last() !is MathToken.LeftParen) {
                        output.add(stack.removeAt(stack.size - 1))
                    }
                    if (stack.isEmpty()) throw IllegalArgumentException("Ошибка скобок")
                    stack.removeAt(stack.size - 1)
                }
                is MathToken.Operator -> {
                    while (stack.isNotEmpty() && stack.last() is MathToken.Operator) {
                        val lastOp = stack.last() as MathToken.Operator
                        if (lastOp.precedence > token.precedence ||
                            (lastOp.precedence == token.precedence && !token.isRightAssociative)
                        ) {
                            output.add(stack.removeAt(stack.size - 1))
                        } else break
                    }
                    stack.add(token)
                }
            }
        }
        while (stack.isNotEmpty()) {
            val last = stack.removeAt(stack.size - 1)
            if (last is MathToken.LeftParen) throw IllegalArgumentException("Ошибка скобок")
            output.add(last)
        }
        return output
    }

    private fun evaluateRPN(tokens: List<MathToken>): BigDecimal {
        val stack = mutableListOf<BigDecimal>()

        for (token in tokens) {
            when (token) {
                is MathToken.Number -> stack.add(token.value)
                is MathToken.Operator -> {
                    when (token.symbol) {
                        "u-" -> {
                            if (stack.isEmpty()) throw IllegalArgumentException("Недостаточно операндов")
                            stack.add(stack.removeAt(stack.size - 1).negate())
                        }
                        "√" -> {
                            if (stack.isEmpty()) throw IllegalArgumentException("Недостаточно операндов")
                            val a = stack.removeAt(stack.size - 1)
                            stack.add(a.sqrt(context = mathContext))
                        }
                        "!" -> {
                            if (stack.isEmpty()) throw IllegalArgumentException("Недостаточно операндов")
                            val a = stack.removeAt(stack.size - 1)
                            if (a.stripTrailingZeros().scale() <= 0) {
                                stack.add(factorial(a.toInt()))
                            } else {
                                throw ArithmeticException("Факториал только для целых чисел")
                            }
                        }
                        "^" -> {
                            if (stack.size < 2) throw IllegalArgumentException("Недостаточно операндов")
                            val exponent = stack.removeAt(stack.size - 1)
                            val base = stack.removeAt(stack.size - 1)

                            val isExponentInteger = exponent.stripTrailingZeros().scale() <= 0
                            if (isExponentInteger) {
                                val expInt = exponent.toInt()
                                if (expInt > MAX_EXPONENT) {
                                    throw ArithmeticException("Слишком большая степень")
                                }
                                try {
                                    stack.add(base.pow(expInt, mathContext))
                                } catch (e: ArithmeticException) {
                                    throw ArithmeticException("Результат слишком велик")
                                }
                            } else {
                                val baseDouble = base.toDouble()
                                val expDouble = exponent.toDouble()
                                val resultDouble = baseDouble.pow(expDouble)
                                if (resultDouble.isInfinite() || resultDouble.isNaN()) {
                                    throw ArithmeticException("Результат слишком велик или неопределён")
                                }
                                val result = BigDecimal(resultDouble.toString()).round(mathContext)
                                stack.add(result)
                            }
                        }
                        "%" -> {
                            if (stack.isEmpty()) throw IllegalArgumentException("Недостаточно операндов")
                            val a = stack.removeAt(stack.size - 1)
                            stack.add(a.divide(BigDecimal(100), mathContext))
                        }
                        else -> {
                            if (stack.size < 2) throw IllegalArgumentException("Недостаточно операндов")
                            val b = stack.removeAt(stack.size - 1)
                            val a = stack.removeAt(stack.size - 1)
                            val result = when (token.symbol) {
                                "+" -> a.add(b)
                                "-" -> a.subtract(b)
                                "*" -> a.multiply(b)
                                "/" -> {
                                    if (b == BigDecimal.ZERO) throw ArithmeticException("Деление на ноль")
                                    a.divide(b, mathContext)
                                }
                                else -> throw IllegalArgumentException("Неизвестный оператор: ${token.symbol}")
                            }
                            stack.add(result)
                        }
                    }
                }
                else -> throw IllegalArgumentException("Недопустимый токен в RPN: $token")
            }
        }
        if (stack.size != 1) throw IllegalArgumentException("Ошибка в выражении")
        return stack.last()
    }

    private fun BigDecimal.sqrt(context: MathContext): BigDecimal {
        if (this < BigDecimal.ZERO) throw ArithmeticException("Корень из отрицательного числа")
        if (this == BigDecimal.ZERO) return BigDecimal.ZERO

        val precision = context.precision
        var x = BigDecimal.valueOf(ksqrt(this.toDouble()))
        val two = BigDecimal(2)
        while (true) {
            val nextX = (x + this.divide(x, context)).divide(two, context)
            if (nextX.subtract(x).abs() < BigDecimal.ONE.movePointLeft(precision - 1)) {
                return nextX.round(context)
            }
            x = nextX
        }
    }

    private fun factorial(n: Int): BigDecimal {
        var result = BigDecimal.ONE
        if (n > 150) throw Exception("Inf factorial")
        for (i in 2..abs(n)) {
            result = result.multiply(BigDecimal.valueOf(i.toLong()))
        }
        return if (n < 0) result.multiply(BigDecimal(-1)) else result
    }

    private fun formatResult(value: BigDecimal): String {
        val stripped = value.stripTrailingZeros()
        return if (stripped.scale() <= 0) {
            stripped.toPlainString()
        } else {
            val rounded = stripped.setScale(9, RoundingMode.HALF_UP).stripTrailingZeros()
            rounded.toPlainString()
        }
    }
}