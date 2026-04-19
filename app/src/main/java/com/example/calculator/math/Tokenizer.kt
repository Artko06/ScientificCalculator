package com.example.calculator.math

import com.example.calculator.math.model.MathToken
import javax.xml.xpath.XPathExpression

object Tokenizer {
    private val operators = mapOf<String, MathToken>(
        "+" to MathToken.Operator(
            symbol = "+",
            precedence = 1
        ),
        "-" to MathToken.Operator(
            symbol = "-",
            precedence = 1
        ),
        "*" to MathToken.Operator(
            symbol = "*",
            precedence = 2
        ),
        "/" to MathToken.Operator(
            symbol = "/",
            precedence = 2
        ),
        "^" to MathToken.Operator(
            symbol = "^",
            precedence = 3,
            isRightAssociative = true
        ),
        "√" to MathToken.Operator(
            symbol = "√",
            precedence = 4
        ),
        "!" to MathToken.Operator(
            symbol = "!",
            precedence = 4
        ),
        "u-" to MathToken.Operator(
            symbol = "u-",
            precedence = 5
        ),
        "%" to MathToken.Operator(
            symbol = "%",
            precedence = 5
        ),
    )

    fun tokenize(expression: String): List<MathToken> {
        val tokens = mutableListOf<MathToken>()
        var i = 0
        val s = expression.replace(" ", "")

        while (i < s.length) {
            val c = s[i]
            when {
                c.isDigit() || c == '.' -> {
                    val sb = StringBuilder()
                    while (i < s.length && (s[i].isDigit() || s[i] == '.')) {
                        sb.append(s[i++])
                    }

                    tokens.add(MathToken.Number(sb.toString().toBigDecimal()))
                    continue
                }
                c == '(' -> tokens.add(MathToken.LeftParen)
                c == ')' -> tokens.add(MathToken.RightParen)
                c == '-' -> {
                    val isUnary = tokens.isEmpty() || tokens.last() is MathToken.LeftParen
                    if (isUnary) {
                        tokens.add(operators["u-"]!!)
                    } else {
                        tokens.add(operators["-"]!!)
                    }
                }
                c == '√' -> {
                    if (tokens.isNotEmpty()) {
                        if (tokens.last() is MathToken.Number) {
                            tokens.add(operators["*"]!!)
                        }
                    }
                    tokens.add(operators["√"]!!)
                }
                else -> {
                    val op = operators[c.toString()] ?: throw Exception("Symbol: $c")
                    tokens.add(op)
                }
            }
            i++
        }
        return tokens
    }
}