package com.example.calculator.presentation.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.math.model.CalculatorMode

@Composable
fun CalculatorKeyboard(
    modifier: Modifier,
    mode: CalculatorMode,
    isLandscape: Boolean,
    onAddSymbolAction: (String) -> Unit,
    onBackspaceAction: () -> Unit,
    onClearAction: () -> Unit,
    onEqualAction: () -> Unit,
) {
    val buttonRows = getButtonRows(mode, isLandscape)

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        buttonRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                row.forEach { symbol ->
                    val buttonModifier = if (symbol == "AC") {
                        Modifier.fillMaxHeight().weight(2f)
                    } else {
                        Modifier.fillMaxHeight().weight(1f)
                    }

                    val (bgColor, textColor) = when {
                        symbol == "=" -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
                        symbol in listOf("AC") -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
                        symbol in listOf("+", "-", "*", "/", "%", "√", "^", "!", "(", ")") ->
                            MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    CalculatorButton(
                        modifier = buttonModifier,
                        text = symbol,
                        fontText = if (isLandscape) 24.sp else 32.sp,
                        backgroundColor = bgColor,
                        textColor = textColor,
                        onClick = {
                            when (symbol) {
                                "=" -> onEqualAction()
                                "⌫" -> onBackspaceAction()
                                "AC" -> onClearAction()
                                else -> onAddSymbolAction(symbol)
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun getButtonRows(mode: CalculatorMode, isLandscape: Boolean): List<List<String>> {
    return if (mode == CalculatorMode.BASIC) {
        if (isLandscape) {
            listOf(
                listOf("7", "8", "9", "AC"),
                listOf("4", "5", "6", "*", "-"),
                listOf("1", "2", "3", "/", "+"),
                listOf("0", ".", "⌫", "%", "=")
            )
        } else {
            listOf(
                listOf("AC", "%", "/"),
                listOf("7", "8", "9", "*"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf("0", ".", "⌫", "=")
            )
        }
    } else {
        if (isLandscape) {
            listOf(
                listOf("^", "(", ")", "√", "!"),
                listOf("7", "8", "9", "AC"),
                listOf("4", "5", "6", "*", "-"),
                listOf("1", "2", "3", "/", "+"),
                listOf("0", ".", "⌫", "%", "=")
            )
        } else {
            listOf(
                listOf("^", "(", ")", "√", "!"),
                listOf("AC", "%", "/"),
                listOf("7", "8", "9", "*"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf("0", ".", "⌫", "="),
            )
        }
    }
}