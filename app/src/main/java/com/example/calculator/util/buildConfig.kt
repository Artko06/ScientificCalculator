package com.example.calculator.util

import androidx.compose.runtime.Composable
import com.example.calculator.BuildConfig

enum class BuildConfigType {
    DEMO,
    FULL
}

@Composable
fun getBuildConfig(): BuildConfigType {
    return when(BuildConfig.FLAVOR) {
        "demo" -> BuildConfigType.DEMO
        "full" -> BuildConfigType.FULL
        else -> BuildConfigType.DEMO
    }
}