package com.example.calculator.presentation.state

import com.example.calculator.math.model.CalculatorMode

data class CalculatorState(
    val expression: String = "",
    val lastExpression: String = "",
    val errorStatus: Boolean = false,
    val errorMessage: String = "",
    val calculatorMode: CalculatorMode = CalculatorMode.BASIC,
)