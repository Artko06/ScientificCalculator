package com.example.calculator.presentation.screen.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.calculator.util.isLandscape

@Composable
fun DisplaySection(
    expression: String,
    result: String,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val isLandscape = isLandscape()

    val verticalPadding = if (isLandscape) 4.dp else 64.dp
    val horizontalPadding = if (isLandscape) 16.dp else 12.dp
    val expressionTextStyle = if (isLandscape) {
        MaterialTheme.typography.displayMedium
    } else {
        when(expression.length) {
            in 0..9 -> MaterialTheme.typography.displayLarge
            in 10..13 -> MaterialTheme.typography.displayMedium
            else -> MaterialTheme.typography.displaySmall
        }

    }
    val resultTextStyle = if (isLandscape) {
        MaterialTheme.typography.displaySmall
    } else {
        MaterialTheme.typography.displayMedium
    }

    val scrollExpressionState = rememberScrollState()
    val scrollResultState = rememberScrollState()

    LaunchedEffect(expression) {
        scrollExpressionState.animateScrollTo(scrollExpressionState.maxValue)
    }

    LaunchedEffect(result) {
        scrollResultState.animateScrollTo(scrollResultState.maxValue)
    }

    val spacerHeight = if (isLandscape) 4.dp else 16.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = verticalPadding, horizontal = horizontalPadding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollExpressionState)
        ) {
            Text(
                text = expression.ifEmpty { "0" },
                style = expressionTextStyle,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth().align(alignment = Alignment.CenterEnd)
            )
        }

        Spacer(modifier = Modifier.height(spacerHeight))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollResultState)
        ) {
            if (isError) {
                Text(
                    text = result,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth().align(alignment = Alignment.CenterEnd)
                )
            } else {
                Text(
                    text = result,
                    style = resultTextStyle,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth().align(alignment = Alignment.CenterEnd)
                )
            }
        }
    }
}