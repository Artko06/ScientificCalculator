package com.example.calculator.math.model

sealed class MathResult {
    data class Success(val answer: String): MathResult()
    data class Error(val message: String): MathResult()
}