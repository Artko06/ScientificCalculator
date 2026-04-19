package com.example.calculator.presentation.action

sealed interface CalculatorAction {
    data class AddSymbolAction(val symbol: String): CalculatorAction
    object BackspaceAction: CalculatorAction
    object ClearAction: CalculatorAction
    object EqualAction: CalculatorAction
    object ToggleCalculatorMode: CalculatorAction
    object ReturnToLastExpression: CalculatorAction
}