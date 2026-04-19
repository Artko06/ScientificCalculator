package com.example.calculator.math.model

import java.math.BigDecimal

sealed class MathToken {
    data class Number(val value: BigDecimal): MathToken()
    data class Operator(
        val symbol: String,
        val precedence: Int,
        val isRightAssociative: Boolean = false
    ): MathToken()
    object LeftParen: MathToken()
    object RightParen: MathToken()
}