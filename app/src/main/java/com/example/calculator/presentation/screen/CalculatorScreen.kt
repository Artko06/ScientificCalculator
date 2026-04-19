package com.example.calculator.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculator.R
import com.example.calculator.math.model.CalculatorMode
import com.example.calculator.presentation.action.CalculatorAction
import com.example.calculator.presentation.screen.component.CalculatorKeyboard
import com.example.calculator.presentation.screen.component.DisplaySection
import com.example.calculator.presentation.screen.component.ModeSwitcher
import com.example.calculator.presentation.state.CalculatorState
import com.example.calculator.presentation.viewModel.CalculatorViewModel
import com.example.calculator.util.BuildConfigType
import com.example.calculator.util.getBuildConfig
import com.example.calculator.util.isLandscape

@Composable
fun CalculatorScreen() {
    val calculatorViewModel = viewModel<CalculatorViewModel>()

    CalculatorView(
        calculatorState = calculatorViewModel.calculatorState.collectAsState().value,
        onAction = calculatorViewModel::onAction
    )
}

@Composable
fun CalculatorView(
    calculatorState: CalculatorState = CalculatorState(),
    onAction: (CalculatorAction) -> Unit = {},
) {
    val isLandscape = isLandscape()
    val buildConfig = getBuildConfig()

    val showModeSwitch = remember {
        derivedStateOf {
            buildConfig == BuildConfigType.FULL && !isLandscape
        }
    }

    val onlyEngineeringMode = remember {
        derivedStateOf {
            buildConfig == BuildConfigType.FULL && isLandscape
        }
    }

    LaunchedEffect(onlyEngineeringMode) {
        if (calculatorState.calculatorMode == CalculatorMode.BASIC && onlyEngineeringMode.value) {
            onAction(CalculatorAction.ToggleCalculatorMode)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier.weight(3f)
            ) {
                DisplaySection(
                    expression = calculatorState.expression,
                    result = calculatorState.errorMessage.takeIf { calculatorState.errorStatus }
                        ?: calculatorState.errorMessage,
                    isError = calculatorState.errorStatus,
                )
            }

            Row(
                modifier = Modifier.padding(6.dp)
            ) {
                if (showModeSwitch.value) {
                    ModeSwitcher(
                        currentMode = calculatorState.calculatorMode,
                        onSwitchMode = {
                            onAction(CalculatorAction.ToggleCalculatorMode)
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        onAction(CalculatorAction.ReturnToLastExpression)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.history_time),
                        contentDescription = null
                    )
                }
            }

            CalculatorKeyboard(
                modifier = Modifier.weight(5f),
                mode = calculatorState.calculatorMode,
                isLandscape = isLandscape,
                onAddSymbolAction = { symbol ->
                    onAction(CalculatorAction.AddSymbolAction(symbol))
                },
                onBackspaceAction = {
                    onAction(CalculatorAction.BackspaceAction)
                },
                onClearAction = {
                    onAction(CalculatorAction.ClearAction)
                },
                onEqualAction = {
                    onAction(CalculatorAction.EqualAction)
                }
            )
        }
    }
}

@Composable
@Preview
fun CalculatorScreenPreview() {
    CalculatorView()
}