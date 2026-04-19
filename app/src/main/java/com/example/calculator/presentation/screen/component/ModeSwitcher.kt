package com.example.calculator.presentation.screen.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.calculator.R
import com.example.calculator.math.model.CalculatorMode

@Composable
fun ModeSwitcher(
    modifier: Modifier = Modifier,
    currentMode: CalculatorMode,
    onSwitchMode: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onSwitchMode
    ) {
        Icon(
            painter = painterResource(
                when (currentMode) {
                    CalculatorMode.BASIC -> R.drawable.arrow_down
                    CalculatorMode.ENGINEERING -> R.drawable.arrow_up
                }
            ),
            contentDescription = null
        )
    }
}

@Composable
@Preview
fun ModeSwitcherPreview() {
    ModeSwitcher(
        currentMode = CalculatorMode.BASIC,
        onSwitchMode = {}
    )
}