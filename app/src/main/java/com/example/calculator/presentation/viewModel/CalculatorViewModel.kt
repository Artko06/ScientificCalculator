package com.example.calculator.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.calculator.math.MathSolver
import com.example.calculator.math.model.CalculatorMode
import com.example.calculator.math.model.MathResult
import com.example.calculator.presentation.action.CalculatorAction
import com.example.calculator.presentation.state.CalculatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {
    private val _calculatorState = MutableStateFlow(CalculatorState())
    val calculatorState = _calculatorState.asStateFlow()

    private val mathSolver = MathSolver()

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.AddSymbolAction -> {
                _calculatorState.update {
                    it.copy(
                        expression = it.expression + action.symbol,
                        errorStatus = false,
                    )
                }

                clearError()
            }

            CalculatorAction.BackspaceAction -> {
                if (_calculatorState.value.expression.isNotEmpty()) {
                    _calculatorState.update {
                        it.copy(
                            expression = it.expression.dropLast(1)
                        )
                    }

                    clearError()
                }
            }

            CalculatorAction.ClearAction -> {
                saveLastExpression()

                _calculatorState.update {
                    it.copy(
                        expression = ""
                    )
                }

                clearError()
            }

            CalculatorAction.EqualAction -> {
                val result = mathSolver.solve(expression = _calculatorState.value.expression)

                saveLastExpression()

                when (result) {
                    is MathResult.Error -> {
                        _calculatorState.update {
                            it.copy(
                                errorStatus = true,
                                errorMessage = result.message
                            )
                        }
                    }

                    is MathResult.Success -> {
                        _calculatorState.update {
                            it.copy(
                                expression = result.answer
                            )
                        }
                    }
                }
            }

            CalculatorAction.ToggleCalculatorMode -> {
                _calculatorState.update {
                    it.copy(
                        calculatorMode = when (_calculatorState.value.calculatorMode) {
                            CalculatorMode.BASIC -> CalculatorMode.ENGINEERING
                            CalculatorMode.ENGINEERING -> CalculatorMode.BASIC
                        }
                    )
                }
            }

            CalculatorAction.ReturnToLastExpression -> {
                val tempExpression = _calculatorState.value.expression

                _calculatorState.update {
                    it.copy(
                        expression = it.lastExpression,
                        lastExpression = tempExpression
                    )
                }
            }
        }
    }

    private fun saveLastExpression() {
        _calculatorState.update {
            it.copy(
                lastExpression = it.expression
            )
        }
    }

    private fun clearError() {
        _calculatorState.update {
            it.copy(
                errorStatus = false,
                errorMessage = ""
            )
        }
    }
}